package com.jonlink.system.wx.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.jonlink.common.utils.DateUtils;
import com.jonlink.common.utils.StringUtils;
import com.jonlink.system.domain.WxLedgerItem;
import com.jonlink.system.mapper.WxBizMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 电子台账服务：5 个业务点(核销/分销积分/推送/扫码/绑定)统一写入流水。
 * 规则 R12/R13：同事务写入 + uk(biz_no+type) 防重复。
 *
 * @author jonlink
 */
@Service
public class WxLedgerService
{
    private static final Logger log = LoggerFactory.getLogger(WxLedgerService.class);

    /** 台账类型：1核销 2分销积分 3模板推送 4扫码 5粉丝绑定 6手动调整 */
    public static final String TYPE_VERIFY = "1";
    public static final String TYPE_DIST = "2";
    public static final String TYPE_PUSH = "3";
    public static final String TYPE_SCAN = "4";
    public static final String TYPE_BIND = "5";
    public static final String TYPE_ADJUST = "6";

    @Autowired
    private WxBizMapper wxBizMapper;

    /**
     * 写入流水（幂等：同 bizNo+type 重复调用直接忽略返回 false）
     *
     * @param type 台账类型
     * @param bizNo 关联业务单号
     * @param phone 客户手机号
     * @param openid 客户 openid
     * @param amount 金额
     * @param points 分值(积分入账=amount)
     * @param direction 0流出 1流入 2中性
     * @param remark 备注
     * @return 是否新增(true=新写入 false=重复忽略)
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean write(String type, String bizNo, String phone, String openid, BigDecimal amount,
            BigDecimal points, String direction, String remark)
    {
        if (StringUtils.isEmpty(bizNo) || StringUtils.isEmpty(type))
        {
            log.warn("[ledger] bizNo/type 为空, 跳过: bizNo={} type={}", bizNo, type);
            return false;
        }
        // R13 幂等: uk(biz_no+ledger_type)
        WxLedgerItem exist = wxBizMapper.selectLedgerByBizNoType(bizNo, type);
        if (exist != null)
        {
            log.info("[ledger] 流水已存在(bizNo={} type={}), 幂等跳过", bizNo, type);
            return false;
        }
        WxLedgerItem item = new WxLedgerItem();
        item.setLedgerNo("L" + DateUtils.dateTimeNow("yyyyMMddHHmmss") + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        item.setLedgerType(type);
        item.setBizNo(bizNo);
        item.setPhone(phone);
        item.setOpenid(openid);
        item.setAmount(amount == null ? BigDecimal.ZERO : amount);
        item.setPoints(points == null ? BigDecimal.ZERO : points);
        item.setDirection(StringUtils.isEmpty(direction) ? "2" : direction);
        item.setBizUser("SYSTEM");
        item.setStatus("1");
        item.setOccurredTime(new Date());
        item.setRemark(remark);
        item.setCreateBy("SYSTEM");
        wxBizMapper.insertLedgerItem(item);
        log.info("[ledger] 写入流水: no={} type={} bizNo={} amount={} points={}", item.getLedgerNo(), type, bizNo, amount, points);
        return true;
    }

    /** 每日汇总(定时任务 00:05 跑昨日) */
    public int dailySummary(String date)
    {
        List<Map<String, Object>> groups = wxBizMapper.summaryGroupByType(date);
        int n = 0;
        for (Map<String, Object> g : groups)
        {
            String type = String.valueOf(g.get("type"));
            int cnt = ((Number) g.get("cnt")).intValue();
            BigDecimal amt = new BigDecimal(String.valueOf(g.get("amt")));
            wxBizMapper.upsertLedgerSummary(date, type, cnt, amt);
            n++;
        }
        log.info("[ledger] 日汇总完成 date={} types={}", date, n);
        return n;
    }
}
