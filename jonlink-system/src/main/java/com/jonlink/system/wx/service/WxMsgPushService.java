package com.jonlink.system.wx.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.jonlink.common.utils.DateUtils;
import com.jonlink.common.utils.StringUtils;
import com.jonlink.system.domain.WxMpTemplate;
import com.jonlink.system.domain.WxMpTemplateMsg;
import com.jonlink.system.domain.WxMpUser;
import com.jonlink.system.mapper.WxBizMapper;
import com.jonlink.system.mapper.WxMpTemplateMsgMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 模板消息推送链路。
 * 规则 R2(每日≤3条/超限次日补发/逾期3天放弃)、R3(transpose_no 幂等 + 送达回执)。
 * 失败不阻塞业务(R17)：任何异常只记记录，不向上抛。
 *
 * @author jonlink
 */
@Service
public class WxMsgPushService
{
    private static final Logger log = LoggerFactory.getLogger(WxMsgPushService.class);

    /** 每日限流条数 */
    public static final int DAILY_LIMIT = 3;
    /** 超限待发(次日补发)状态 */
    public static final String STATUS_PENDING = "0";
    public static final String STATUS_SUCCESS = "1";
    public static final String STATUS_FAIL = "2";
    public static final String STATUS_OVER_LIMIT = "3";
    public static final String STATUS_EXPIRED = "4";
    public static final String STATUS_NO_USER = "5";

    @Autowired
    private WxBizMapper wxBizMapper;
    @Autowired
    private WxMpTemplateMsgMapper wxMpTemplateMsgMapper;
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private WxLedgerService wxLedgerService;

    /**
     * 按手机号推送（主入口：openid 转换 + 限流 + 幂等 + 台账流水）。
     *
     * @param phone 客户手机号
     * @param templateId 微信模板ID
     * @param data 模板关键词数据 keyword1..keywordN
     * @param url 跳转地址(可空)
     * @param bizNo 业务单号(幂等键, 可空=每次必发)
     * @param bizType 业务类型(推送记录+台账)
     * @return 结果 map {ok, status, msg}
     */
    public Map<String, Object> sendByPhone(String phone, String templateId, Map<String, Object> data,
            String url, String bizNo, String bizType)
    {
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(templateId))
        {
            return Map.of("ok", false, "msg", "手机号或模板为空");
        }
        // 1. openid 转换 (R1: phone↔openid 1:1)
        WxMpUser user = wxBizMapper.selectUserByPhone(phone);
        if (user == null || StringUtils.isEmpty(user.getOpenid()))
        {
            // 幂等防重: 同 bizNo+模板未找到用户只记一条
            if (StringUtils.isNotEmpty(bizNo))
            {
                saveMsg(null, phone, templateId, data, url, bizNo, bizType, STATUS_NO_USER, null, "未找到粉丝");
            }
            return Map.of("ok", false, "status", STATUS_NO_USER, "msg", "手机号未绑定粉丝");
        }
        return sendByOpenid(user.getOpenid(), phone, templateId, data, url, bizNo, bizType);
    }

    /**
     * 手机号查粉丝（供规则执行器生成 H5 ticket 使用）。
     */
    public com.jonlink.system.domain.WxMpUser findUserByPhone(String phone)
    {
        if (StringUtils.isEmpty(phone))
        {
            return null;
        }
        return wxBizMapper.selectUserByPhone(phone);
    }

    /**
     * 按 openid 推送（内部与手动触发共用）
     */
    public Map<String, Object> sendByOpenid(String openid, String phone, String templateId,
            Map<String, Object> data, String url, String bizNo, String bizType)
    {
        // 2. 幂等 (R3: transpose_no 唯一)
        if (StringUtils.isNotEmpty(bizNo))
        {
            WxMpTemplateMsg exist = selectByBizNo(bizNo);
            if (exist != null)
            {
                log.info("[push] 同业务单号已推送过 bizNo={} status={}", bizNo, exist.getStatus());
                return Map.of("ok", exist.getStatus().equals(STATUS_SUCCESS), "status", exist.getStatus(),
                        "msg", "重复推送已拦截(幂等)");
            }
        }
        // 3. 限流 (R2: 当日成功 ≤3 条, 超限转待发次日补发)
        int today = wxBizMapper.countMsgTodayByPhone(phone == null ? "" : phone, DateUtils.getDate());
        String status = STATUS_PENDING;
        if (today >= DAILY_LIMIT)
        {
            status = STATUS_OVER_LIMIT;
            saveMsg(openid, phone, templateId, data, url, bizNo, bizType, status, null, "超限(次日补发)");
            return Map.of("ok", false, "status", status, "msg", "当日已达上限，已转待发");
        }
        // 4. 调微信
        Map<String, Object> r = wxMpService.sendTemplateMsg(openid, templateId, data, url);
        boolean ok = (Boolean) r.get("ok");
        saveMsg(openid, phone, templateId, data, url, bizNo, bizType,
                ok ? STATUS_SUCCESS : STATUS_FAIL, ok ? String.valueOf(r.get("msgid")) : null,
                ok ? null : String.valueOf(r.get("err")));
        // 5. 台账流水(类型3推送, 中性)
        try
        {
            wxLedgerService.write(WxLedgerService.TYPE_PUSH, bizNo, phone, openid, BigDecimal.ZERO, BigDecimal.ZERO, "2",
                    ok ? "模板消息推送成功" : "推送失败");
        }
        catch (Exception e)
        {
            log.warn("[push] 台账流水写入失败(不阻断): {}", e.getMessage());
        }
        return Map.of("ok", ok, "status", status, "msg", ok ? "推送成功" : "推送失败(已记录)");
    }

    private WxMpTemplateMsg selectByBizNo(String bizNo)
    {
        return wxBizMapper.selectMsgByBizId(bizNo);
    }

    private void saveMsg(String openid, String phone, String templateId, Map<String, Object> data,
            String url, String bizNo, String bizType, String status, String msgId, String errMsg)
    {
        WxMpTemplateMsg m = new WxMpTemplateMsg();
        m.setBatchNo("S" + DateUtils.dateTimeNow("yyyyMMddHHmmss"));
        m.setTransposeNo(bizNo == null ? UUID.randomUUID().toString().replace("-", "") : bizNo);
        m.setOpenid(openid);
        m.setPhone(phone);
        m.setTemplateId(templateId);
        m.setKeywords(data == null ? "{}" : JSON.toJSONString(data));
        m.setUrl(url);
        m.setMsgId(msgId);
        m.setStatus(status);
        m.setErrMsg(errMsg);
        m.setBizType(bizType);
        m.setBizId(bizNo);
        m.setSendTime(new Date());
        m.setCreateBy("SYSTEM");
        wxMpTemplateMsgMapper.insertWxMpTemplateMsg(m);
        log.info("[push] 发送记录落库: phone={} tpl={} status={} bizNo={}", phone, templateId, status, bizNo);
    }

    /**
     * 送达回执处理 (TEMPLATESENDJOBFINISH: success/failed:user block/failed:system failed)
     * 规则 R3：回执只更新状态，失败不重发(业务侧 retry 手动)
     */
    public void handleSendResult(String msgId, String status)
    {
        WxMpTemplateMsg m = wxBizMapper.selectMsgByMsgId(msgId);
        if (m != null)
        {
            m.setStatus("success".equals(status) ? STATUS_SUCCESS : STATUS_FAIL);
            m.setErrMsg(status);
            m.setUpdateTime(new Date());
            wxMpTemplateMsgMapper.updateWxMpTemplateMsg(m);
            log.info("[push] 送达回执: msgId={} status={}", msgId, status);
        }
    }

    /** 次日补发超限待发(每日00:00定时任务调用); 逾期3天标记放弃 */
    public int catchUp()
    {
        List<WxMpTemplateMsg> list = wxBizMapper.selectMsgForCatchUp(STATUS_OVER_LIMIT, 200);
        int done = 0;
        for (WxMpTemplateMsg m : list)
        {
            // 逾期3天放弃 (R2)
            if (m.getCreateTime() != null
                    && System.currentTimeMillis() - m.getCreateTime().getTime() > 3L * 24 * 3600 * 1000)
            {
                m.setStatus(STATUS_EXPIRED);
                m.setErrMsg("逾期3天未补发, 放弃");
                wxMpTemplateMsgMapper.updateWxMpTemplateMsg(m);
                continue;
            }
            Map<String, Object> data = parseKeywords(m.getKeywords());
            Map<String, Object> r = sendByOpenid(m.getOpenid(), m.getPhone(), m.getTemplateId(), data, m.getUrl(), m.getBizId(), m.getBizType());
            if (Boolean.TRUE.equals(r.get("ok")))
            {
                done++;
            }
        }
        log.info("[push] 补发完成: total={} done={}", list.size(), done);
        return done;
    }

    /** 解析模板关键词 JSON -> Map */
    @SuppressWarnings("unchecked")
    private Map<String, Object> parseKeywords(String json)
    {
        if (StringUtils.isEmpty(json))
        {
            return Map.of();
        }
        try
        {
            JSONObject obj = JSON.parseObject(json);
            return obj;
        }
        catch (Exception e)
        {
            return Map.of();
        }
    }

    /** 重试失败记录 */
    public int retryFailed()
    {
        WxMpTemplateMsg q = new WxMpTemplateMsg();
        q.setStatus(STATUS_FAIL);
        List<WxMpTemplateMsg> list = wxMpTemplateMsgMapper.selectWxMpTemplateMsgList(q);
        int done = 0;
        for (WxMpTemplateMsg m : list)
        {
            Map<String, Object> r = sendByOpenid(m.getOpenid(), m.getPhone(), m.getTemplateId(),
                    parseKeywords(m.getKeywords()), m.getUrl(), m.getBizId(), m.getBizType());
            if (Boolean.TRUE.equals(r.get("ok")))
            {
                done++;
            }
        }
        return done;
    }

    /** 校验模板是否本地启用 */
    public boolean templateEnabled(String templateId)
    {
        WxMpTemplate t = wxBizMapper.selectTemplateByTplId(templateId);
        return t != null && "1".equals(t.getStatus());
    }
}
