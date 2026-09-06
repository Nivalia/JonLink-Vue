package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.FinPayment;

/**
 * 付款单Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public interface FinPaymentMapper 
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
     * 删除付款单
     * 
     * @param id 付款单主键
     * @return 结果
     */
    public int deleteFinPaymentById(Long id);

    /**
     * 批量删除付款单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinPaymentByIds(Long[] ids);
}
