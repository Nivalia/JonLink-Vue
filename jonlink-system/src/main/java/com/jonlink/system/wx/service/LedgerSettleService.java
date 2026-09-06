package com.jonlink.system.wx.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.jonlink.common.utils.DateUtils;
import com.jonlink.common.utils.StringUtils;
import com.jonlink.system.domain.JonlinkInsuranceLedger;
import com.jonlink.system.domain.JonlinkSettleRecord;
import com.jonlink.system.domain.WxMpUser;
import com.jonlink.system.mapper.JonlinkInsuranceLedgerMapper;
import com.jonlink.system.mapper.JonlinkSettleRecordMapper;
import com.jonlink.system.mapper.WxBizMapper;
import com.jonlink.system.mapper.JonlinkChannelUserMapper;
import com.jonlink.system.domain.JonlinkChannelUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 保险台账结算服务 (R16/R17)。
 * - 单笔结算：up=上游结费(收入,金额=上游税后佣金) / down=下游结费(支出,金额=下游佣金)
 * - up/down 状态独立；同方向同台账幂等(uk_direction_ledger)
 * - 结算完成触发推送规则 SETTLE_UP/SETTLE_DOWN（失败不影响结算）
 *
 * @author jonlink
 */
@Service
public class LedgerSettleService
{
    private static final Logger log = LoggerFactory.getLogger(LedgerSettleService.class);

    @Autowired
    private JonlinkInsuranceLedgerMapper ledgerMapper;
    @Autowired
    private JonlinkSettleRecordMapper settleRecordMapper;
    @Autowired
    private WxBizMapper wxBizMapper;
    @Autowired
    private com.jonlink.system.mapper.WxMpUserMapper wxMpUserMapper;
    @Autowired
    private JonlinkChannelUserMapper jonlinkChannelUserMapper;
    @Autowired
    private WxMsgRuleExecutor ruleExecutor;

    /**
     * 结算
     *
     * @param ledgerId 台账行 id
     * @param direction 0上游结费 1下游结费
     * @param opUser 经手人
     * @return {ok, code, msg, settleNo}
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> settle(Long ledgerId, String direction, String opUser)
    {
        if (ledgerId == null)
        {
            return Map.of("ok", false, "code", "BAD_PARAM", "msg", "台账ID不能为空");
        }
        if (!"0".equals(direction) && !"1".equals(direction))
        {
            return Map.of("ok", false, "code", "BAD_PARAM", "msg", "结算方向不正确");
        }
        JonlinkInsuranceLedger ledger = ledgerMapper.selectJonlinkInsuranceLedgerById(ledgerId);
        if (ledger == null)
        {
            return Map.of("ok", false, "code", "NOT_FOUND", "msg", "台账不存在: " + ledgerId);
        }
        // 幂等: 同方向已结算
        if ("0".equals(direction) && "1".equals(ledger.getUpSettleStatus()))
        {
            return Map.of("ok", false, "code", "SETTLED", "msg", "上游已结算, 请勿重复", "settleNo", ledger.getUpSettleNo());
        }
        if ("1".equals(direction) && "1".equals(ledger.getDownSettleStatus()))
        {
            return Map.of("ok", false, "code", "SETTLED", "msg", "下游已结算, 请勿重复", "settleNo", ledger.getDownSettleNo());
        }
        JonlinkSettleRecord exist = wxBizMapper.selectSettleByDirectionLedger(direction, ledgerId);
        if (exist != null)
        {
            return Map.of("ok", false, "code", "SETTLED", "msg", "结算单已存在(幂等)", "settleNo", exist.getSettleNo());
        }
        // 结算金额: 上游=上游税后佣金, 下游=下游佣金
        BigDecimal amount = "0".equals(direction)
                ? (ledger.getUpCommission() == null ? BigDecimal.ZERO : ledger.getUpCommission())
                : (ledger.getDownCommission() == null ? BigDecimal.ZERO : ledger.getDownCommission());
        String settleNo = ("0".equals(direction) ? "S" : "X") + DateUtils.dateTimeNow("yyyyMMddHHmmss")
                + String.format("%04d", ledgerId % 10000);

        JonlinkSettleRecord record = new JonlinkSettleRecord();
        record.setSettleNo(settleNo);
        record.setDirection(direction);
        record.setLedgerId(ledgerId);
        record.setPolicyNo(ledger.getPolicyNo());
        record.setAmount(amount);
        record.setSettleStatus("1");
        record.setOpUser(StringUtils.isEmpty(opUser) ? "SYSTEM" : opUser);
        record.setSettleDate(new Date());
        record.setCreateBy(opUser == null ? "SYSTEM" : opUser);
        settleRecordMapper.insertJonlinkSettleRecord(record);
        // 更新台账状态
        wxBizMapper.updateLedgerSettle(ledgerId, direction, "1", settleNo);
        log.info("[settle] 结算完成: ledger={} direction={} no={} amount={}", ledgerId, direction, settleNo, amount);

        // 触发推送规则(非阻塞): 渠道手机号
        try
        {
            String channelPhone = resolveChannelPhone(ledger);
            Map<String, Object> params = new HashMap<>();
            params.put("保单号", ledger.getPolicyNo());
            params.put("金额", amount.stripTrailingZeros().toPlainString());
            params.put("结算单号", settleNo);
            params.put("时间", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
            ruleExecutor.execute("0".equals(direction) ? WxMsgRuleExecutor.RULE_SETTLE_UP : WxMsgRuleExecutor.RULE_SETTLE_DOWN,
                    channelPhone, params, settleNo);
        }
        catch (Exception e)
        {
            log.warn("[settle] 结算推送失败(不影响结算): {}", e.getMessage());
        }
        return Map.of("ok", true, "code", "OK", "msg", "结算成功", "settleNo", settleNo, "amount", amount);
    }

    /**
     * 渠道手机号解析:
     *   channel_type=1 公众号粉丝 -> wx_mp_user.phone (channel_ref=粉丝表id)
     *   channel_type=2 渠道/业务员 -> jonlink_channel_user.phone (channel_ref=档案id)
     *   其他 -> null
     */
    public String resolveChannelPhone(JonlinkInsuranceLedger ledger)
    {
        if (ledger.getChannelRef() == null)
        {
            return null;
        }
        try
        {
            if ("1".equals(ledger.getChannelType()))
            {
                WxMpUser u = wxMpUserMapper.selectWxMpUserById(ledger.getChannelRef());
                return u == null ? null : u.getPhone();
            }
            else if ("2".equals(ledger.getChannelType()))
            {
                JonlinkChannelUser u = jonlinkChannelUserMapper.selectJonlinkChannelUserById(ledger.getChannelRef());
                return u == null ? null : u.getPhone();
            }
        }
        catch (Exception e)
        {
            log.warn("[settle] 渠道手机号解析失败: {}", e.getMessage());
        }
        return null;
    }
}
