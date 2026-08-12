package com.jonlink.system.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import com.jonlink.common.utils.DateUtils;
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
 *
 * @author jonlink
 * @date 2026-08-08
 */
@Service
public class JonlinkInsuranceLedgerServiceImpl implements IJonlinkInsuranceLedgerService 
{
    @Autowired
    private JonlinkInsuranceLedgerMapper jonlinkInsuranceLedgerMapper;
    @Autowired
    private WxBizMapper wxBizMapper;

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
        if (jonlinkInsuranceLedger.getLedgerDate() == null)
        {
            jonlinkInsuranceLedger.setLedgerDate(DateUtils.getNowDate());
        }
        jonlinkInsuranceLedger.setCreateTime(DateUtils.getNowDate());
        return jonlinkInsuranceLedgerMapper.insertJonlinkInsuranceLedger(jonlinkInsuranceLedger);
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
        fillFromProduct(jonlinkInsuranceLedger);
        calcCommission(jonlinkInsuranceLedger);
        jonlinkInsuranceLedger.setUpdateTime(DateUtils.getNowDate());
        return jonlinkInsuranceLedgerMapper.updateJonlinkInsuranceLedger(jonlinkInsuranceLedger);
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
        if (StringUtils.isEmpty(ledger.getUpChannel())) ledger.setUpChannel(p.getUpChannel());
        if (ledger.getUpRate() == null || ledger.getUpRate().compareTo(BigDecimal.ZERO) == 0)
            ledger.setUpRate(p.getUpRate());
        if (ledger.getDownRate() == null || ledger.getDownRate().compareTo(BigDecimal.ZERO) == 0)
            ledger.setDownRate(p.getDownRate());
    }

    /**
     * 自动算 4 列 (R14/R15)
     */
    private void calcCommission(JonlinkInsuranceLedger ledger)
    {
        BigDecimal premium = ledger.getPremium() == null ? BigDecimal.ZERO : ledger.getPremium();
        BigDecimal upRate = ledger.getUpRate() == null ? BigDecimal.ZERO : ledger.getUpRate();
        BigDecimal downRate = ledger.getDownRate() == null ? BigDecimal.ZERO : ledger.getDownRate();
        // 上游税后佣金: 扣税则 保费/1.06 × 费率%
        BigDecimal upCommission;
        if ("1".equals(ledger.getTaxFlag()))
        {
            upCommission = premium.divide(new BigDecimal("1.06"), 4, RoundingMode.HALF_UP)
                    .multiply(upRate).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        }
        else
        {
            upCommission = premium.multiply(upRate).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        }
        // 下游佣金 = 保费 × 费率%
        BigDecimal downCommission = premium.multiply(downRate).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        // 净费 = 保费 - 下游佣金
        BigDecimal netFee = premium.subtract(downCommission);
        // 利润 = 上游税后佣金 - 下游佣金
        BigDecimal profit = upCommission.subtract(downCommission);

        ledger.setUpCommission(upCommission);
        ledger.setDownCommission(downCommission);
        ledger.setNetFee(netFee);
        ledger.setProfit(profit);
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
