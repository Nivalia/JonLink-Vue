package com.jonlink.system.wx.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.jonlink.system.domain.WxBizOrder;
import com.jonlink.system.domain.WxMpSendBatch;
import com.jonlink.system.domain.WxMpTemplate;
import com.jonlink.system.domain.WxMpTemplateMsg;
import com.jonlink.system.mapper.WxBizMapper;
import com.jonlink.system.mapper.WxMpSendBatchMapper;
import com.jonlink.system.mapper.WxMpTemplateMapper;
import com.jonlink.system.mapper.WxMpTemplateMsgMapper;

/**
 * 推送核对: 业务规则: 只对 Excel 批量导入(source=1)的推送数据做核对,核对依据是
 * 模板的 keyword_meta.bizField 映射,从消息数据取值与 wx_biz_order 的真实业务字段比对。
 *
 * 通过: check_status=1,失败: check_status=2 + check_diff 写明差异。仅通过的可以发送。
 *
 * @author jonlink
 */
@Service
public class WxMsgCheckService
{
    private static final Logger log = LoggerFactory.getLogger(WxMsgCheckService.class);

    @Autowired
    private WxMpTemplateMsgMapper wxMpTemplateMsgMapper;
    @Autowired
    private WxMpTemplateMapper wxMpTemplateMapper;
    @Autowired
    private WxBizMapper wxBizMapper;
    @Autowired
    private WxMpSendBatchMapper wxMpSendBatchMapper;

    /** 核对结果明细 */
    public static class CheckResult
    {
        public int total;
        public int passed;
        public int failed;
    }

    /**
     * 批次核对: 仅处理 source=1 且 check_status=0/null 的消息。
     * @return 核对结果
     */
    public CheckResult checkBatch(String batchNo)
    {
        CheckResult r = new CheckResult();
        List<WxMpTemplateMsg> list = wxMpTemplateMsgMapper.selectByBatchNoForCheck(batchNo);
        r.total = list.size();
        for (WxMpTemplateMsg m : list)
        {
            String diff = checkOne(m);
            m.setCheckTime(new Date());
            if (diff == null)
            {
                m.setCheckStatus("1");
                m.setCheckDiff(null);
                r.passed++;
            }
            else
            {
                m.setCheckStatus("2");
                m.setCheckDiff(diff);
                r.failed++;
            }
            wxMpTemplateMsgMapper.updateCheckResult(m);
        }
        // 写回批次统计
        WxMpSendBatch upd = new WxMpSendBatch();
        upd.setBatchNo(batchNo);
        upd.setCheckPassed((long) r.passed);
        upd.setCheckFailed((long) r.failed);
        upd.setCheckTime(new Date());
        wxMpSendBatchMapper.updateCheckStats(upd);
        log.info("[check] 批次 {} 核对完成: 候选 {} 通过 {} 失败 {}", batchNo, r.total, r.passed, r.failed);
        return r;
    }

    private String checkOne(WxMpTemplateMsg m)
    {
        WxMpTemplate tpl = wxMpTemplateMapper.selectWxMpTemplateByTemplateId(m.getTemplateId());
        if (tpl == null || tpl.getKeywordMeta() == null || tpl.getKeywordMeta().isEmpty())
        {
            return "模板未配置 keyword_meta,跳过";
        }
        JSONArray meta;
        try
        {
            meta = JSON.parseArray(tpl.getKeywordMeta());
        }
        catch (Exception e)
        {
            return "keyword_meta 格式错误";
        }
        JSONObject keywords;
        try
        {
            keywords = m.getKeywords() == null ? new JSONObject() : JSON.parseObject(m.getKeywords());
        }
        catch (Exception e)
        {
            return "keywords 格式错误";
        }
        WxBizOrder order = m.getBizId() == null ? null : wxBizMapper.selectWxBizOrderByOrderNo(m.getBizId());
        if (order == null)
        {
            return "找不到业务订单 bizId=" + m.getBizId();
        }
        for (int i = 0; i < meta.size(); i++)
        {
            JSONObject kw = meta.getJSONObject(i);
            String bizField = kw.getString("bizField");
            if (bizField == null || bizField.isEmpty() || "none".equalsIgnoreCase(bizField))
            {
                continue;
            }
            String key = kw.getString("key");
            String sentVal = normalize(keywords.getString(key));
            String realVal = normalize(extractBizValue(order, bizField));
            if (sentVal == null || realVal == null)
            {
                continue;
            }
            if (!sentVal.equals(realVal))
            {
                return kw.getString("name") + "不一致:推送 " + sentVal + " vs 实际 " + realVal;
            }
        }
        return null;
    }

    private String extractBizValue(WxBizOrder order, String bizField)
    {
        if (order == null)
        {
            return null;
        }
        switch (bizField)
        {
            case "order_no":
                return order.getOrderNo();
            case "amount":
                BigDecimal a = order.getAmount();
                return a == null ? null : a.toPlainString();
            case "car_no":
                return order.getCarNo();
            case "customer_name":
                return order.getCustomerName();
            case "phone":
                return order.getPhone();
            case "service_type":
                return order.getServiceType();
            case "verify_time":
                Date t = order.getVerifyTime();
                return t == null ? null : new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(t);
            default:
                return null;
        }
    }

    private String normalize(String s)
    {
        if (s == null)
        {
            return null;
        }
        return s.trim();
    }
}
