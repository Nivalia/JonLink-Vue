package com.jonlink.system.wx.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.jonlink.common.utils.StringUtils;
import com.jonlink.system.domain.WxBizOrder;
import com.jonlink.system.domain.WxDistCommission;
import com.jonlink.system.domain.WxDistMember;
import com.jonlink.system.domain.WxMpUser;
import com.jonlink.system.mapper.WxBizMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 核销服务 (R5)：三要素核销(order_no + phone + amount) + 幂等(verify_status=0 仅一次)。
 * 核销成功同事务联动：佣金入账(R9 100%直接上级) + 电子台账(核销流水+积分流水)；
 * 推送通知走规则表(非阻塞, 失败不影响核销结果)。
 *
 * @author jonlink
 */
@Service
public class WxVerifyService
{
    private static final Logger log = LoggerFactory.getLogger(WxVerifyService.class);

    /** 佣金比例: 100% 直接上级 (R9) */
    private static final BigDecimal RATIO = new BigDecimal("1.00");

    @Autowired
    private WxBizMapper wxBizMapper;
    @Autowired
    private WxLedgerService wxLedgerService;
    @Autowired
    private WxMsgRuleExecutor ruleExecutor;

    /**
     * 核销接口。
     *
     * @param orderNo 订单号(唯一)
     * @param phone 客户手机号(核销要素)
     * @param carNo 车牌号(核销要素, 可空——按单配置)
     * @param amount 核销金额(核销要素)
     * @return {ok, code, msg} code: VERIFIED已核销 / NOT_FOUND不存在 / MISMATCH要素不符 / OK成功
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> verify(String orderNo, String phone, String carNo, BigDecimal amount)
    {
        if (StringUtils.isEmpty(orderNo))
        {
            return Map.of("ok", false, "code", "BAD_PARAM", "msg", "订单号不能为空");
        }
        // 1. 查单
        WxBizOrder order = wxBizMapper.selectOrderByNo(orderNo);
        if (order == null)
        {
            return Map.of("ok", false, "code", "NOT_FOUND", "msg", "订单不存在: " + orderNo);
        }
        // 2. 幂等: 已核销拦截 (R5)
        if ("1".equals(order.getVerifyStatus()))
        {
            log.warn("[verify] 同单二次核销被拦截: {} (已核销 {})", orderNo, order.getVerifyTime());
            return Map.of("ok", false, "code", "VERIFIED", "msg", "该订单已核销, 请勿重复操作");
        }
        // 3. 三要素校验
        boolean phoneOk = StringUtils.isEmpty(phone) || phone.equals(order.getPhone());
        boolean carOk = StringUtils.isEmpty(carNo) || StringUtils.isEmpty(order.getCarNo()) || carNo.equals(order.getCarNo());
        boolean amtOk = amount == null || order.getAmount() == null || amount.compareTo(order.getAmount()) == 0;
        if (!phoneOk || !carOk || !amtOk)
        {
            log.warn("[verify] 核销要素不符: order={} phone={} carNo={} amount={}", orderNo, phone, carNo, amount);
            return Map.of("ok", false, "code", "MISMATCH", "msg", "核销信息与订单不符");
        }
        // 4. 更新核销状态
        order.setVerifyStatus("1");
        order.setVerifyTime(new Date());
        order.setVerifyMsg("核销成功");
        order.setUpdateTime(new Date());
        wxBizMapper.updateOrderVerify(order);
        log.info("[verify] 核销成功: order={} phone={} amount={}", orderNo, order.getPhone(), order.getAmount());

        // 5. 同事务: 电子台账核销流水
        wxLedgerService.write(WxLedgerService.TYPE_VERIFY, orderNo, order.getPhone(), null,
                order.getAmount(), BigDecimal.ZERO, "2", "订单核销");

        // 6. 同事务: 分销佣金入账 (R9: 100% 直接上级)
        creditCommission(order);

        // 7. 推送通知(非阻塞): 核销成功模板
        try
        {
            Map<String, Object> params = new HashMap<>();
            params.put("订单号", orderNo);
            params.put("金额", order.getAmount() == null ? "0" : order.getAmount().stripTrailingZeros().toPlainString());
            params.put("时间", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
            ruleExecutor.execute(WxMsgRuleExecutor.RULE_ORDER_VERIFY, order.getPhone(), params, orderNo + "_VERIFY");
        }
        catch (Exception e)
        {
            log.warn("[verify] 推送通知失败(不影响核销): {}", e.getMessage());
        }
        return Map.of("ok", true, "code", "OK", "msg", "核销成功", "orderNo", orderNo);
    }

    /**
     * 佣金入账：客户粉丝的上级分销员获得 100% 佣金积分。
     * R10: 分销员停用时本人停积分(跳过入账), 不影响其下级。
     */
    private void creditCommission(WxBizOrder order)
    {
        WxMpUser customer = wxBizMapper.selectUserByPhone(order.getPhone());
        if (customer == null || customer.getDistributorId() == null)
        {
            log.info("[dist] 客户无上级分销员, 跳过佣金: order={}", order.getOrderNo());
            return;
        }
        Long distributorUserId = customer.getDistributorId();
        WxDistMember dist = wxBizMapper.selectDistByUserId(distributorUserId);
        if (dist == null)
        {
            log.info("[dist] 上级分销员档案不存在(user_id={}), 跳过佣金", distributorUserId);
            return;
        }
        if (!"1".equals(dist.getStatus()))
        {
            log.info("[dist] 分销员停用, 本人停积分: {}", dist.getUserName());
            return;
        }
        // 佣金 = 订单金额 × 100%
        BigDecimal commission = order.getAmount() == null ? BigDecimal.ZERO : order.getAmount().multiply(RATIO);
        if (commission.compareTo(BigDecimal.ZERO) <= 0)
        {
            return;
        }
        // 幂等: 同订单不重复入账(uk? wx_dist_commission 无唯一键, 用台账流水防重)
        String ledgerBizNo = order.getOrderNo() + "_DIST";
        if (wxBizMapper.selectLedgerByBizNoType(ledgerBizNo, WxLedgerService.TYPE_DIST) != null)
        {
            log.info("[dist] 佣金已入账, 幂等跳过: order={}", order.getOrderNo());
            return;
        }
        WxDistCommission c = new WxDistCommission();
        c.setOrderNo(order.getOrderNo());
        c.setCustomerPhone(order.getPhone());
        c.setCustomerOpenid(null);
        c.setBeneficiaryId(distributorUserId);
        c.setBeneficiaryName(dist.getUserName());
        c.setCommissionAmt(commission);
        c.setRatio(RATIO);
        c.setPoints(commission); // 积分 1:1
        c.setStatus("1");
        wxBizMapper.insertDistCommission(c);
        // 积分累计
        wxBizMapper.addDistPoints(distributorUserId, commission);
        // 电子台账积分流水
        wxLedgerService.write(WxLedgerService.TYPE_DIST, ledgerBizNo, order.getPhone(), customer.getOpenid(),
                commission, commission, "1", "核销佣金入账(直接上级)");
        log.info("[dist] 佣金入账: order={} beneficiary={} amt={}", order.getOrderNo(), dist.getUserName(), commission);
    }
}
