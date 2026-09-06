package com.jonlink.system.service.impl;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jonlink.common.utils.DateUtils;
import com.jonlink.system.domain.JonlinkInsuranceLedger;
import com.jonlink.system.domain.JonlinkLedgerAdjust;
import com.jonlink.system.mapper.JonlinkInsuranceLedgerMapper;
import com.jonlink.system.mapper.JonlinkLedgerAdjustMapper;
import com.jonlink.system.service.IJonlinkLedgerAdjustService;

/**
 * 台账批增退记录Service业务层处理
 * 
 * @author jonlink
 * @date 2026-09-05
 */
@Service
public class JonlinkLedgerAdjustServiceImpl implements IJonlinkLedgerAdjustService 
{
    @Autowired
    private JonlinkLedgerAdjustMapper jonlinkLedgerAdjustMapper;

    @Autowired
    private JonlinkInsuranceLedgerMapper jonlinkInsuranceLedgerMapper;

    /**
     * 查询台账批增退记录
     */
    @Override
    public JonlinkLedgerAdjust selectJonlinkLedgerAdjustById(Long id)
    {
        return jonlinkLedgerAdjustMapper.selectJonlinkLedgerAdjustById(id);
    }

    /**
     * 查询台账批增退记录列表
     */
    @Override
    public List<JonlinkLedgerAdjust> selectJonlinkLedgerAdjustList(JonlinkLedgerAdjust jonlinkLedgerAdjust)
    {
        return jonlinkLedgerAdjustMapper.selectJonlinkLedgerAdjustList(jonlinkLedgerAdjust);
    }

    /**
     * 新增台账批增退记录
     */
    @Override
    public int insertJonlinkLedgerAdjust(JonlinkLedgerAdjust jonlinkLedgerAdjust)
    {
        return jonlinkLedgerAdjustMapper.insertJonlinkLedgerAdjust(jonlinkLedgerAdjust);
    }

    /**
     * 修改台账批增退记录
     */
    @Override
    public int updateJonlinkLedgerAdjust(JonlinkLedgerAdjust jonlinkLedgerAdjust)
    {
        return jonlinkLedgerAdjustMapper.updateJonlinkLedgerAdjust(jonlinkLedgerAdjust);
    }

    /**
     * 批量删除台账批增退记录
     */
    @Override
    public int deleteJonlinkLedgerAdjustByIds(Long[] ids)
    {
        return jonlinkLedgerAdjustMapper.deleteJonlinkLedgerAdjustByIds(ids);
    }

    /**
     * 执行批增/批退操作
     * 1. 记录批增退流水
     * 2. 更新原台账保费
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int executeAdjust(JonlinkLedgerAdjust adjust)
    {
        // 1. 获取原台账信息
        JonlinkInsuranceLedger ledger = jonlinkInsuranceLedgerMapper.selectJonlinkInsuranceLedgerById(adjust.getLedgerId());
        if (ledger == null) {
            throw new RuntimeException("台账记录不存在");
        }

        // 2. 计算新的保费
        BigDecimal originalPremium = ledger.getPremium();
        BigDecimal adjustAmount = adjust.getAdjustPremium();
        BigDecimal newPremium;

        if ("1".equals(adjust.getAdjustType())) {
            // 批增：原保费 + 调整金额
            newPremium = originalPremium.add(adjustAmount);
        } else {
            // 批退：原保费 - 调整金额
            newPremium = originalPremium.subtract(adjustAmount);
            if (newPremium.compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("批退金额不能超过原保费");
            }
        }

        // 3. 更新原台账保费
        ledger.setPremium(newPremium);
        int result = jonlinkInsuranceLedgerMapper.updateJonlinkInsuranceLedger(ledger);

        // 4. 记录批增退流水
        jonlinkLedgerAdjustMapper.insertJonlinkLedgerAdjust(adjust);

        return result;
    }
}
