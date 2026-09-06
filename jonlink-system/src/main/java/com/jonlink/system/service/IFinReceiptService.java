package com.jonlink.system.service;

import java.util.List;
import com.jonlink.system.domain.FinReceipt;

/**
 * 收款单Service接口
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public interface IFinReceiptService 
{
    /**
     * 查询收款单
     * 
     * @param id 收款单主键
     * @return 收款单
     */
    public FinReceipt selectFinReceiptById(Long id);

    /**
     * 查询收款单列表
     * 
     * @param finReceipt 收款单
     * @return 收款单集合
     */
    public List<FinReceipt> selectFinReceiptList(FinReceipt finReceipt);

    /**
     * 新增收款单
     * 
     * @param finReceipt 收款单
     * @return 结果
     */
    public int insertFinReceipt(FinReceipt finReceipt);

    /**
     * 修改收款单
     * 
     * @param finReceipt 收款单
     * @return 结果
     */
    public int updateFinReceipt(FinReceipt finReceipt);

    /**
     * 批量删除收款单
     * 
     * @param ids 需要删除的收款单主键集合
     * @return 结果
     */
    public int deleteFinReceiptByIds(Long[] ids);

    /**
     * 删除收款单信息
     * 
     * @param id 收款单主键
     * @return 结果
     */
    public int deleteFinReceiptById(Long id);

    /** 创建收款单 */
    public Long create(Long partnerId, Long bankAccountId, java.math.BigDecimal amount,
                       String bizType, String sourceType, Long sourceId, String operator);
}
