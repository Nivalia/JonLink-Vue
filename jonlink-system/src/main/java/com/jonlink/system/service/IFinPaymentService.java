package com.jonlink.system.service;

import java.util.List;
import com.jonlink.system.domain.FinPayment;

/**
 * 付款单Service接口
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public interface IFinPaymentService 
{
    /**
     * 查询付款单
     * 
     * @param id 付款单主键
     * @return 付款单
     */
    public FinPayment selectFinPaymentById(Long id);

    /**
     * 查询付款单列表
     * 
     * @param finPayment 付款单
     * @return 付款单集合
     */
    public List<FinPayment> selectFinPaymentList(FinPayment finPayment);

    /**
     * 新增付款单
     * 
     * @param finPayment 付款单
     * @return 结果
     */
    public int insertFinPayment(FinPayment finPayment);

    /**
     * 修改付款单
     * 
     * @param finPayment 付款单
     * @return 结果
     */
    public int updateFinPayment(FinPayment finPayment);

    /**
     * 批量删除付款单
     * 
     * @param ids 需要删除的付款单主键集合
     * @return 结果
     */
    public int deleteFinPaymentByIds(Long[] ids);

    /**
     * 删除付款单信息
     * 
     * @param id 付款单主键
     * @return 结果
     */
    public int deleteFinPaymentById(Long id);

    /** 创建付款单 */
    public Long create(Long partnerId, Long bankAccountId, java.math.BigDecimal amount,
                       String bizType, String sourceType, Long sourceId, String operator);
}
