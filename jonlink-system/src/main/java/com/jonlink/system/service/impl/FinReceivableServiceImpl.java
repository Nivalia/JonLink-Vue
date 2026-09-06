package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.exception.ServiceException;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.FinReceivableMapper;
import com.jonlink.system.domain.FinReceivable;
import com.jonlink.system.service.IFinReceivableService;

/**
 * 应收单Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@Service
public class FinReceivableServiceImpl implements IFinReceivableService 
{
    @Autowired
    private FinReceivableMapper finReceivableMapper;

    /**
     * 查询应收单
     * 
     * @param id 应收单主键
     * @return 应收单
     */
    @Override
    public FinReceivable selectFinReceivableById(Long id)
    {
        return finReceivableMapper.selectFinReceivableById(id);
    }

    /**
     * 查询应收单列表
     * 
     * @param finReceivable 应收单
     * @return 应收单
     */
    @Override
    public List<FinReceivable> selectFinReceivableList(FinReceivable finReceivable)
    {
        return finReceivableMapper.selectFinReceivableList(finReceivable);
    }

    /**
     * 新增应收单
     * 
     * @param finReceivable 应收单
     * @return 结果
     */
    @Override
    public int insertFinReceivable(FinReceivable finReceivable)
    {
        finReceivable.setCreateTime(DateUtils.getNowDate());
        return finReceivableMapper.insertFinReceivable(finReceivable);
    }

    /**
     * 修改应收单
     * 
     * @param finReceivable 应收单
     * @return 结果
     */
    @Override
    public int updateFinReceivable(FinReceivable finReceivable)
    {
        finReceivable.setUpdateTime(DateUtils.getNowDate());
        return finReceivableMapper.updateFinReceivable(finReceivable);
    }

    /**
     * 批量删除应收单
     * 
     * @param ids 需要删除的应收单主键
     * @return 结果
     */
    @Override
    public int deleteFinReceivableByIds(Long[] ids)
    {
        return finReceivableMapper.deleteFinReceivableByIds(ids);
    }

    /**
     * 删除应收单信息
     * 
     * @param id 应收单主键
     * @return 结果
     */
    @Override
    public int deleteFinReceivableById(Long id)
    {
        return finReceivableMapper.deleteFinReceivableById(id);
    }

    /**
     * 创建应收单(独立调用,不一定从 ledger)
     * doc_no 自动生成: AR-yyyyMM-NNNN
     */
    @Override
    public Long createForPartner(Long partnerId, String bizType, String sourceType, Long sourceId,
                                 java.math.BigDecimal amount, java.util.Date dueDate, String operator)
    {
        if (partnerId == null) throw new ServiceException("partnerId 不能为空");
        if (amount == null || amount.compareTo(java.math.BigDecimal.ZERO) <= 0)
            throw new ServiceException("金额必须大于 0");
        FinReceivable r = new FinReceivable();
        r.setDocNo(generateDocNo("AR"));
        r.setPartnerId(partnerId);
        r.setBizType(bizType == null ? "DEFAULT" : bizType);
        r.setSourceType(sourceType);
        r.setSourceId(sourceId);
        r.setAmount(amount);
        r.setPaidAmount(java.math.BigDecimal.ZERO);
        r.setRemainAmount(amount);
        r.setDueDate(dueDate);
        r.setStatus("0");
        r.setCreateBy(operator);
        finReceivableMapper.insertFinReceivable(r);
        return r.getId();
    }

    private String generateDocNo(String prefix)
    {
        // 简单实现:prefix + yyyyMMdd + 6 位 nanoseconds
        return prefix + "-" + new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date())
            + "-" + String.format("%06d", (long)(System.nanoTime() % 1000000));
    }
}
