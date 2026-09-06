package com.jonlink.system.service.impl;

import com.jonlink.common.exception.ServiceException;
import com.jonlink.system.domain.FinAllocation;
import com.jonlink.system.domain.FinPayable;
import com.jonlink.system.domain.FinReceivable;
import com.jonlink.system.mapper.FinAllocationMapper;
import com.jonlink.system.mapper.FinPayableMapper;
import com.jonlink.system.mapper.FinReceivableMapper;
import com.jonlink.system.service.IFinSettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 核销引擎实现
 * @author jonlink
 */
@Service
public class FinSettlementServiceImpl implements IFinSettlementService
{
    @Autowired private FinReceivableMapper receivableMapper;
    @Autowired private FinPayableMapper payableMapper;
    @Autowired private FinAllocationMapper allocationMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FinReceivable cancelReceivable(Long receivableId, BigDecimal amount, String operator)
    {
        return cancelReceivable(receivableId, amount, operator, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FinReceivable cancelReceivable(Long receivableId, BigDecimal amount, String operator, Long receiptId)
    {
        if (receivableId == null) throw new ServiceException("receivableId 不能为空");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) throw new ServiceException("核销金额必须大于 0");
        FinReceivable r = receivableMapper.selectFinReceivableById(receivableId);
        if (r == null) throw new ServiceException("应收单不存在: " + receivableId);
        if ("3".equals(r.getStatus())) throw new ServiceException("应收单已作废");
        if (r.getRemainAmount() == null) r.setRemainAmount(r.getAmount());
        if (amount.compareTo(r.getRemainAmount()) > 0)
            throw new ServiceException("核销金额(" + amount + ")大于剩余应收(" + r.getRemainAmount() + ")");
        // 累加
        BigDecimal newPaid = (r.getPaidAmount() == null ? BigDecimal.ZERO : r.getPaidAmount()).add(amount);
        BigDecimal newRemain = r.getAmount().subtract(newPaid);
        r.setPaidAmount(newPaid);
        r.setRemainAmount(newRemain);
        r.setStatus(newRemain.compareTo(BigDecimal.ZERO) == 0 ? "2" : "1");
        r.setUpdateBy(operator);
        receivableMapper.updateFinReceivable(r);

        // 写核销记录
        FinAllocation alloc = new FinAllocation();
        alloc.setDocType("0"); // 0=应收
        alloc.setDocId(receivableId);
        alloc.setReceiptId(receiptId);
        alloc.setAmount(amount);
        alloc.setAllocateTime(new Date());
        alloc.setCreateBy(operator);
        alloc.setCreateTime(new Date());
        allocationMapper.insertFinAllocation(alloc);

        return r;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FinPayable cancelPayable(Long payableId, BigDecimal amount, String operator)
    {
        return cancelPayable(payableId, amount, operator, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FinPayable cancelPayable(Long payableId, BigDecimal amount, String operator, Long paymentId)
    {
        if (payableId == null) throw new ServiceException("payableId 不能为空");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) throw new ServiceException("核销金额必须大于 0");
        FinPayable p = payableMapper.selectFinPayableById(payableId);
        if (p == null) throw new ServiceException("应付单不存在: " + payableId);
        if ("3".equals(p.getStatus())) throw new ServiceException("应付单已作废");
        if (p.getRemainAmount() == null) p.setRemainAmount(p.getAmount());
        if (amount.compareTo(p.getRemainAmount()) > 0)
            throw new ServiceException("核销金额(" + amount + ")大于剩余应付(" + p.getRemainAmount() + ")");
        BigDecimal newPaid = (p.getPaidAmount() == null ? BigDecimal.ZERO : p.getPaidAmount()).add(amount);
        BigDecimal newRemain = p.getAmount().subtract(newPaid);
        p.setPaidAmount(newPaid);
        p.setRemainAmount(newRemain);
        p.setStatus(newRemain.compareTo(BigDecimal.ZERO) == 0 ? "2" : "1");
        p.setUpdateBy(operator);
        payableMapper.updateFinPayable(p);

        // 写核销记录
        FinAllocation alloc = new FinAllocation();
        alloc.setDocType("1"); // 1=应付
        alloc.setDocId(payableId);
        alloc.setPaymentId(paymentId);
        alloc.setAmount(amount);
        alloc.setAllocateTime(new Date());
        alloc.setCreateBy(operator);
        alloc.setCreateTime(new Date());
        allocationMapper.insertFinAllocation(alloc);

        return p;
    }
}