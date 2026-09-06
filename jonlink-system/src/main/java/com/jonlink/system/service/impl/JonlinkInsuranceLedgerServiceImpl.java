package com.jonlink.system.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import com.jonlink.common.exception.ServiceException;
import com.jonlink.common.utils.DateUtils;
import com.jonlink.common.utils.SecurityUtils;
import com.jonlink.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.JonlinkInsuranceLedgerMapper;
import com.jonlink.system.mapper.WxBizMapper;
import com.jonlink.system.domain.JonlinkInsuranceLedger;
import com.jonlink.system.domain.JonlinkProduct;
import com.jonlink.system.service.IJonlinkInsuranceLedgerService;

/**
 * 保险台账Service业务层处理
 *
 * 自动算 (R14/R15)：
 *  - up_commission 上游税后佣金 = 保费/1.06×up_rate% (扣税) 或 保费×up_rate% (不扣税)
 *  - down_commission 下游佣金   = 保费×down_rate%
 *  - net_fee 净费 = 保费 - 下游佣金
 *  - profit 利润 = 上游税后佣金 - 下游佣金
 *  选产品自动带出 险别/公司/返利/是否扣税/上游渠道。
 *  审计字段: insert 写 createBy/createTime, update 写 updateBy/updateTime。
 *  结算保护: up/down 任一已结算的行 禁止修改保费/含税/费率类字段。
 *
 * @author jonlink
 * @date 2026-08-13
 */
@Service
public class JonlinkInsuranceLedgerServiceImpl implements IJonlinkInsuranceLedgerService
{
    @Autowired
    private JonlinkInsuranceLedgerMapper jonlinkInsuranceLedgerMapper;
    @Autowired
    private WxBizMapper wxBizMapper;
    @Autowired
    private com.jonlink.system.service.IFinLedgerBookEngine ledgerBookEngine;
    @Autowired
    private com.jonlink.system.service.IFinPeriodService periodService;
    @Autowired
    private com.jonlink.system.wx.service.WxMsgRuleExecutor wxMsgRuleExecutor;

    /**
     * 查询保险台账
     *
     * @param id 保险台账主键
     * @return 保险台账
     */
    @Override
    public JonlinkInsuranceLedger selectJonlinkInsuranceLedgerById(Long id)
    {
        return jonlinkInsuranceLedgerMapper.selectJonlinkInsuranceLedgerById(id);
    }

    /**
     * 查询保险台账列表
     *
     * @param jonlinkInsuranceLedger 保险台账
     * @return 保险台账
     */
    @Override
    public List<JonlinkInsuranceLedger> selectJonlinkInsuranceLedgerList(JonlinkInsuranceLedger jonlinkInsuranceLedger)
    {
        return jonlinkInsuranceLedgerMapper.selectJonlinkInsuranceLedgerList(jonlinkInsuranceLedger);
    }

    /**
     * 新增保险台账
     *
     * @param jonlinkInsuranceLedger 保险台账
     * @return 结果
     */
    @Override
    public int insertJonlinkInsuranceLedger(JonlinkInsuranceLedger jonlinkInsuranceLedger)
    {
        // 选产品联动带出
        fillFromProduct(jonlinkInsuranceLedger);
        // 自动算 4 列 (R14/R15)
        calcCommission(jonlinkInsuranceLedger);
        // 日期兜底:空则默认当日
        if (jonlinkInsuranceLedger.getLedgerDate() == null)
        {
            jonlinkInsuranceLedger.setLedgerDate(DateUtils.getNowDate());
        }
        // 审计字段 (P1-3)
        String username = currentUsername();
        jonlinkInsuranceLedger.setCreateBy(username);
        jonlinkInsuranceLedger.setCreateTime(DateUtils.getNowDate());
        jonlinkInsuranceLedger.setUpdateBy(username);
        jonlinkInsuranceLedger.setUpdateTime(DateUtils.getNowDate());
        int rows = jonlinkInsuranceLedgerMapper.insertJonlinkInsuranceLedger(jonlinkInsuranceLedger);
        // 自动记账: 新增后生成凭证
        if (rows > 0 && jonlinkInsuranceLedger.getId() != null) {
            autoBook(jonlinkInsuranceLedger, username);
            // 触发出单通知推送
            triggerPolicyCreated(jonlinkInsuranceLedger);
        }
        return rows;
    }

    /**
     * 修改保险台账
     *
     * @param jonlinkInsuranceLedger 保险台账
     * @return 结果
     */
    @Override
    public int updateJonlinkInsuranceLedger(JonlinkInsuranceLedger jonlinkInsuranceLedger)
    {
        if (jonlinkInsuranceLedger.getId() == null)
        {
            throw new ServiceException("缺少台账主键,无法修改");
        }
        // 结算保护 (P1-1): up/down 任一已结算的行禁止改费率/保费类
        JonlinkInsuranceLedger existed = jonlinkInsuranceLedgerMapper.selectJonlinkInsuranceLedgerById(jonlinkInsuranceLedger.getId());
        if (existed == null)
        {
            throw new ServiceException("台账记录不存在");
        }
        if (isSettled(existed))
        {
            throw new ServiceException("该台账已结算,禁止修改保费/费率/含税字段");
        }
        // 选产品联动带出
        fillFromProduct(jonlinkInsuranceLedger);
        // 自动算 4 列
        calcCommission(jonlinkInsuranceLedger);
        // 审计字段 (P1-3)
        jonlinkInsuranceLedger.setUpdateBy(currentUsername());
        jonlinkInsuranceLedger.setUpdateTime(DateUtils.getNowDate());
        return jonlinkInsuranceLedgerMapper.updateJonlinkInsuranceLedger(jonlinkInsuranceLedger);
    }

    /**
     * 触发出单完成推送。
     * 将台账数据转为 Map，调用 WxMsgRuleExecutor 执行 POLICY_CREATED 规则。
     */
    private void triggerPolicyCreated(JonlinkInsuranceLedger ledger)
    {
        try
        {
            java.util.Map<String, Object> data = new java.util.HashMap<>();
            data.put("id", ledger.getId());
            data.put("policy_no", ledger.getPolicyNo());
            data.put("applicant", ledger.getApplicant());
            data.put("insured", ledger.getInsured());
            data.put("insurance_type", ledger.getInsuranceType());
            data.put("insurance_company", ledger.getInsuranceCompany());
            data.put("product_name", ledger.getProductName());
            data.put("premium", ledger.getPremium());
            data.put("channel_ref", ledger.getChannelRef());
            data.put("channel_name", ledger.getChannelName());
            data.put("channel_type", ledger.getChannelType());
            data.put("up_channel", ledger.getUpChannel());
            data.put("up_rate", ledger.getUpRate());
            data.put("down_rate", ledger.getDownRate());
            data.put("up_commission", ledger.getUpCommission());
            data.put("down_commission", ledger.getDownCommission());
            data.put("net_fee", ledger.getNetFee());
            data.put("profit", ledger.getProfit());
            String dateStr = ledger.getLedgerDate() != null ? new java.text.SimpleDateFormat("yyyy年MM月dd日").format(ledger.getLedgerDate()) : "";
            data.put("ledger_date", dateStr);
            data.put("date", dateStr);
            data.put("up_settle_status", ledger.getUpSettleStatus());
            data.put("down_settle_status", ledger.getDownSettleStatus());
            String bizNo = "LEDGER_" + ledger.getId() + "_POLICY";
            wxMsgRuleExecutor.executeByLedger("POLICY_CREATED", data, bizNo);
        }
        catch (Exception e)
        {
            org.slf4j.LoggerFactory.getLogger(getClass()).warn("[ledger] 出单推送失败(不影响业务): {}", e.getMessage());
        }
    }

    /**
     * 选产品联动: 带出 产品名/险别/公司/返利/是否扣税/上游渠道
     */
    private void fillFromProduct(JonlinkInsuranceLedger ledger)
    {
        if (ledger.getProductId() == null)
        {
            return;
        }
        JonlinkProduct p = wxBizMapper.selectProductJoin(ledger.getProductId());
        if (p == null)
        {
            return;
        }
        if (StringUtils.isEmpty(ledger.getProductName())) ledger.setProductName(p.getProductName());
        if (StringUtils.isEmpty(ledger.getInsuranceType())) ledger.setInsuranceType(p.getTypeName());
        if (StringUtils.isEmpty(ledger.getInsuranceCompany())) ledger.setInsuranceCompany(p.getCompanyName());
        if (StringUtils.isEmpty(ledger.getTaxFlag())) ledger.setTaxFlag(p.getDeductTax());
        // 上游渠道: 优先用 product.channelName (join jonlink_channel 带出的名称), 回落到 upChannel 文本
        if (StringUtils.isNotEmpty(p.getChannelName()))
        {
            ledger.setUpChannel(p.getChannelName());
        }
        else if (StringUtils.isEmpty(ledger.getUpChannel()))
        {
            ledger.setUpChannel(p.getUpChannel());
        }
        if (ledger.getUpRate() == null || ledger.getUpRate().compareTo(BigDecimal.ZERO) == 0)
            ledger.setUpRate(p.getUpRate());
        if (ledger.getDownRate() == null || ledger.getDownRate().compareTo(BigDecimal.ZERO) == 0)
            ledger.setDownRate(p.getDownRate());
    }

    /**
     * 自动算 4 列 (R14/R15)
     *
     * 计算规则(v4 定稿 2026-08-25):
     *   1 个税开关(taxFlag)同时作用于上游佣金和下游佣金
     *   - 含税(taxFlag=1): 上下游佣金基数 = 保费 / 1.06
     *   - 不含税(taxFlag=0): 上下游佣金基数 = 保费(原始)
     *   - 净费 = 保费(原始) - 上游佣金(始终用原始保费减)
     *   - 利润 = 上游佣金 - 下游佣金
     *   - 保费仅统计, 不参与利润公式
     */
    private void calcCommission(JonlinkInsuranceLedger ledger)
    {
        BigDecimal premium = ledger.getPremium() == null ? BigDecimal.ZERO : ledger.getPremium();
        BigDecimal upRate = ledger.getUpRate() == null ? BigDecimal.ZERO : ledger.getUpRate();
        BigDecimal downRate = ledger.getDownRate() == null ? BigDecimal.ZERO : ledger.getDownRate();

        // 佣金基数: 含税则 保费/1.06, 不含税则 保费原值
        BigDecimal commissionBase = premium;
        if ("1".equals(ledger.getTaxFlag()))
        {
            commissionBase = premium.divide(new BigDecimal("1.06"), 4, RoundingMode.HALF_UP);
        }

        // 上游佣金 = 基数 × 上游政策%
        BigDecimal upCommission = commissionBase.multiply(upRate)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        // 下游佣金 = 基数 × 下游政策%
        BigDecimal downCommission = commissionBase.multiply(downRate)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        // 净费 = 保费(原始) - 上游佣金
        BigDecimal netFee = premium.subtract(upCommission);
        // 利润 = 上游佣金 - 下游佣金
        BigDecimal profit = upCommission.subtract(downCommission);

        ledger.setUpCommission(upCommission);
        ledger.setDownCommission(downCommission);
        ledger.setNetFee(netFee);
        ledger.setProfit(profit);
    }

    /**
     * 自动记账: 台账新增后自动生成财务凭证
     */
    private void autoBook(JonlinkInsuranceLedger ledger, String username) {
        org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
        log.info("开始自动记账: ledgerId={}, policyNo={}", ledger.getId(), ledger.getPolicyNo());
        try {
            // 获取当前打开的期间
            com.jonlink.system.domain.FinPeriod query = new com.jonlink.system.domain.FinPeriod();
            query.setStatus("0"); // 0=打开
            java.util.List<com.jonlink.system.domain.FinPeriod> periods = periodService.selectFinPeriodList(query);
            if (periods == null || periods.isEmpty()) {
                log.warn("没有打开的期间,跳过自动记账");
                return;
            }
            String periodCode = periods.get(0).getPeriodCode();
            log.info("当前期间: {}", periodCode);
            // 构建台账上下文
            java.util.Map<String, Object> context = new java.util.HashMap<>();
            context.put("premium", ledger.getPremium());
            context.put("up_commission", ledger.getUpCommission());
            context.put("down_commission", ledger.getDownCommission());
            context.put("net_fee", ledger.getNetFee());
            context.put("profit", ledger.getProfit());
            log.info("台账上下文: {}", context);
            // 使用台账日期作为凭证日期，格式 yyyy-MM-dd
            String voucherDate = ledger.getLedgerDate() != null
                ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(ledger.getLedgerDate())
                : new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
            // 调用记账引擎
            ledgerBookEngine.book("ledger_book", ledger.getId(), ledger.getPolicyNo(), context, periodCode, voucherDate, username);
            log.info("自动记账成功");
        } catch (Exception e) {
            // 记账失败不影响台账保存，仅记录日志
            log.warn("台账自动记账失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 判定是否已结算 (P1-1):
     *  up_settle_status='1' (上游已结) 或 down_settle_status='1' (下游已结) 即为已结算。
     *  JonLink 风格的字典 0=未结 1=已结。
     */
    private boolean isSettled(JonlinkInsuranceLedger row)
    {
        return "1".equals(row.getUpSettleStatus()) || "1".equals(row.getDownSettleStatus());
    }

    /**
     * 取当前登录用户名(为空兜底 "system")。
     */
    private String currentUsername()
    {
        try
        {
            String u = SecurityUtils.getUsername();
            return StringUtils.isNotEmpty(u) ? u : "system";
        }
        catch (Exception e)
        {
            return "system";
        }
    }

    /**
     * 批量删除保险台账
     *
     * @param ids 需要删除的保险台账主键
     * @return 结果
     */
    @Override
    public int deleteJonlinkInsuranceLedgerByIds(Long[] ids)
    {
        return jonlinkInsuranceLedgerMapper.deleteJonlinkInsuranceLedgerByIds(ids);
    }

    /**
     * 删除保险台账信息
     *
     * @param id 保险台账主键
     * @return 结果
     */
    @Override
    public int deleteJonlinkInsuranceLedgerById(Long id)
    {
        return jonlinkInsuranceLedgerMapper.deleteJonlinkInsuranceLedgerById(id);
    }
}
