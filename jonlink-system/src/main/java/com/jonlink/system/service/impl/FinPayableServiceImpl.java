package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.exception.ServiceException;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.FinPayableMapper;
import com.jonlink.system.domain.FinPayable;
import com.jonlink.system.service.IFinPayableService;

/**
 * 应付单Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@Service
public class FinPayableServiceImpl implements IFinPayableService 
{
    @Autowired
    private FinPayableMapper finPayableMapper;

    /**
     * 查询应付单
     * 
     * @param id 应付单主键
     * @return 应付单
     */
    @Override
    public FinPayable selectFinPayableById(Long id)
    {
        return finPayableMapper.selectFinPayableById(id);
    }

    /**
     * 查询应付单列表
     * 
     * @param finPayable 应付单
     * @return 应付单
     */
    @Override
    public List<FinPayable> selectFinPayableList(FinPayable finPayable)
    {
        return finPayableMapper.selectFinPayableList(finPayable);
    }

    /**
     * 新增应付单
     * 
     * @param finPayable 应付单
     * @return 结果
     */
    @Override
    public int insertFinPayable(FinPayable finPayable)
    {
        finPayable.setCreateTime(DateUtils.getNowDate());
        return finPayableMapper.insertFinPayable(finPayable);
    }

    /**
     * 修改应付单
     * 
     * @param finPayable 应付单
     * @return 结果
     */
    @Override
    public int updateFinPayable(FinPayable finPayable)
    {
        finPayable.setUpdateTime(DateUtils.getNowDate());
        return finPayableMapper.updateFinPayable(finPayable);
    }

    /**
     * 批量删除应付单
     * 
     * @param ids 需要删除的应付单主键
     * @return 结果
     */
    @Override
    public int deleteFinPayableByIds(Long[] ids)
    {
        return finPayableMapper.deleteFinPayableByIds(ids);
    }

    /**
     * 删除应付单信息
     * 
     * @param id 应付单主键
     * @return 结果
     */
    @Override
    public int deleteFinPayableById(Long id)
    {
        return finPayableMapper.deleteFinPayableById(id);
    }

    @Override
    public Long createForPartner(Long partnerId, String bizType, String sourceType, Long sourceId,
                                 java.math.BigDecimal amount, java.util.Date dueDate, String operator)
    {
        if (partnerId == null) throw new ServiceException("partnerId 不能为空");
        if (amount == null || amount.compareTo(java.math.BigDecimal.ZERO) <= 0)
            throw new ServiceException("金额必须大于 0");
        FinPayable p = new FinPayable();
        p.setDocNo("AP-" + new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date())
            + "-" + String.format("%06d", (long)(System.nanoTime() % 1000000)));
        p.setPartnerId(partnerId);
        p.setBizType(bizType == null ? "DEFAULT" : bizType);
        p.setSourceType(sourceType);
        p.setSourceId(sourceId);
        p.setAmount(amount);
        p.setPaidAmount(java.math.BigDecimal.ZERO);
        p.setRemainAmount(amount);
        p.setDueDate(dueDate);
        p.setStatus("0");
        p.setCreateBy(operator);
        finPayableMapper.insertFinPayable(p);
        return p.getId();
    }
}
