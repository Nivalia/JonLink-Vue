package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.FinExpense;

/**
 * 费用报销单Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public interface FinExpenseMapper 
{
    /**
     * 查询费用报销单
     * 
     * @param id 费用报销单主键
     * @return 费用报销单
     */
    public FinExpense selectFinExpenseById(Long id);

    /**
     * 查询费用报销单列表
     * 
     * @param finExpense 费用报销单
     * @return 费用报销单集合
     */
    public List<FinExpense> selectFinExpenseList(FinExpense finExpense);

    /**
     * 新增费用报销单
     * 
     * @param finExpense 费用报销单
     * @return 结果
     */
    public int insertFinExpense(FinExpense finExpense);

    /**
     * 修改费用报销单
     * 
     * @param finExpense 费用报销单
     * @return 结果
     */
    public int updateFinExpense(FinExpense finExpense);

    /**
     * 删除费用报销单
     * 
     * @param id 费用报销单主键
     * @return 结果
     */
    public int deleteFinExpenseById(Long id);

    /**
     * 批量删除费用报销单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinExpenseByIds(Long[] ids);
}
