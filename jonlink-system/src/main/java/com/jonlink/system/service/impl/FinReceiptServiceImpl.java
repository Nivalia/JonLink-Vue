package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.exception.ServiceException;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.FinReceiptMapper;
import com.jonlink.system.domain.FinReceipt;
import com.jonlink.system.service.IFinReceiptService;

/**
 * 收款单Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@Service
public class FinReceiptServiceImpl implements IFinReceiptService 
{
    @Autowired
    private FinReceiptMapper finReceiptMapper;

    /**
     * 查询收款单
     * 
     * @param id 收款单主键
     * @return 收款单
     */
    @Override
    public FinReceipt selectFinReceiptById(Long id)
    {
        return finReceiptMapper.selectFinReceiptById(id);
    }

    /**
     * 查询收款单列表
     * 
     * @param finReceipt 收款单
     * @return 收款单
     */
    @Override
    public List<FinReceipt> selectFinReceiptList(FinReceipt finReceipt)
    {
        return finReceiptMapper.selectFinReceiptList(finReceipt);
    }

    /**
     * 新增收款单
     * 
     * @param finReceipt 收款单
     * @return 结果
     */
    @Override
    public int insertFinReceipt(FinReceipt finReceipt)
    {
        finReceipt.setCreateTime(DateUtils.getNowDate());
        return finReceiptMapper.insertFinReceipt(finReceipt);
    }

    /**
     * 修改收款单
     * 
     * @param finReceipt 收款单
     * @return 结果
     */
    @Override
    public int updateFinReceipt(FinReceipt finReceipt)
    {
        finReceipt.setUpdateTime(DateUtils.getNowDate());
        return finReceiptMapper.updateFinReceipt(finReceipt);
    }

    /**
     * 批量删除收款单
     * 
     * @param ids 需要删除的收款单主键
     * @return 结果
     */
    @Override
    public int deleteFinReceiptByIds(Long[] ids)
    {
        return finReceiptMapper.deleteFinReceiptByIds(ids);
    }

    /**
     * 删除收款单信息
     * 
     * @param id 收款单主键
     * @return 结果
     */
    @Override
    public int deleteFinReceiptById(Long id)
    {
        return finReceiptMapper.deleteFinReceiptById(id);
    }

    @Override
    public Long create(Long partnerId, Long bankAccountId, java.math.BigDecimal amount,
                       String bizType, String sourceType, Long sourceId, String operator)
    {
        if (amount == null || amount.compareTo(java.math.BigDecimal.ZERO) <= 0)
            throw new ServiceException("金额必须大于 0");
        FinReceipt r = new FinReceipt();
        r.setBillNo("RC-" + new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date())
            + "-" + String.format("%06d", (long)(System.nanoTime() % 1000000)));
        r.setPartnerId(partnerId);
        r.setBankAccountId(bankAccountId);
        r.setAmount(amount);
        r.setBizType(bizType == null ? "DEFAULT" : bizType);
        r.setSourceType(sourceType);
        r.setSourceId(sourceId);
        r.setBillDate(new java.util.Date());
        r.setStatus("0");
        r.setCreateBy(operator);
        finReceiptMapper.insertFinReceipt(r);
        return r.getId();
    }
}
