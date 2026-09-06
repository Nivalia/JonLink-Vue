package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.exception.ServiceException;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.FinPaymentMapper;
import com.jonlink.system.domain.FinPayment;
import com.jonlink.system.service.IFinPaymentService;

/**
 * 付款单Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@Service
public class FinPaymentServiceImpl implements IFinPaymentService 
{
    @Autowired
    private FinPaymentMapper finPaymentMapper;

    /**
     * 查询付款单
     * 
     * @param id 付款单主键
     * @return 付款单
     */
    @Override
    public FinPayment selectFinPaymentById(Long id)
    {
        return finPaymentMapper.selectFinPaymentById(id);
    }

    /**
     * 查询付款单列表
     * 
     * @param finPayment 付款单
     * @return 付款单
     */
    @Override
    public List<FinPayment> selectFinPaymentList(FinPayment finPayment)
    {
        return finPaymentMapper.selectFinPaymentList(finPayment);
    }

    /**
     * 新增付款单
     * 
     * @param finPayment 付款单
     * @return 结果
     */
    @Override
    public int insertFinPayment(FinPayment finPayment)
    {
        finPayment.setCreateTime(DateUtils.getNowDate());
        return finPaymentMapper.insertFinPayment(finPayment);
    }

    /**
     * 修改付款单
     * 
     * @param finPayment 付款单
     * @return 结果
     */
    @Override
    public int updateFinPayment(FinPayment finPayment)
    {
        finPayment.setUpdateTime(DateUtils.getNowDate());
        return finPaymentMapper.updateFinPayment(finPayment);
    }

    /**
     * 批量删除付款单
     * 
     * @param ids 需要删除的付款单主键
     * @return 结果
     */
    @Override
    public int deleteFinPaymentByIds(Long[] ids)
    {
        return finPaymentMapper.deleteFinPaymentByIds(ids);
    }

    /**
     * 删除付款单信息
     * 
     * @param id 付款单主键
     * @return 结果
     */
    @Override
    public int deleteFinPaymentById(Long id)
    {
        return finPaymentMapper.deleteFinPaymentById(id);
    }

    @Override
    public Long create(Long partnerId, Long bankAccountId, java.math.BigDecimal amount,
                       String bizType, String sourceType, Long sourceId, String operator)
    {
        if (amount == null || amount.compareTo(java.math.BigDecimal.ZERO) <= 0)
            throw new ServiceException("金额必须大于 0");
        FinPayment p = new FinPayment();
        p.setBillNo("PY-" + new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date())
            + "-" + String.format("%06d", (long)(System.nanoTime() % 1000000)));
        p.setPartnerId(partnerId);
        p.setBankAccountId(bankAccountId);
        p.setAmount(amount);
        p.setBizType(bizType == null ? "DEFAULT" : bizType);
        p.setSourceType(sourceType);
        p.setSourceId(sourceId);
        p.setBillDate(new java.util.Date());
        p.setStatus("0");
        p.setCreateBy(operator);
        finPaymentMapper.insertFinPayment(p);
        return p.getId();
    }
}
