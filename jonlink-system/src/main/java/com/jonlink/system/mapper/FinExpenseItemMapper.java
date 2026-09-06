package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.FinExpenseItem;

/**
 * 报销明细Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public interface FinExpenseItemMapper 
{
    /**
     * 查询报销明细
     * 
     * @param id 报销明细主键
     * @return 报销明细
     */
    public FinExpenseItem selectFinExpenseItemById(Long id);

    /**
     * 查询报销明细列表
     * 
     * @param finExpenseItem 报销明细
     * @return 报销明细集合
     */
    public List<FinExpenseItem> selectFinExpenseItemList(FinExpenseItem finExpenseItem);

    /**
     * 新增报销明细
     * 
     * @param finExpenseItem 报销明细
     * @return 结果
     */
    public int insertFinExpenseItem(FinExpenseItem finExpenseItem);

    /**
     * 修改报销明细
     * 
     * @param finExpenseItem 报销明细
     * @return 结果
     */
    public int updateFinExpenseItem(FinExpenseItem finExpenseItem);

    /**
     * 删除报销明细
     * 
     * @param id 报销明细主键
     * @return 结果
     */
    public int deleteFinExpenseItemById(Long id);

    /**
     * 批量删除报销明细
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinExpenseItemByIds(Long[] ids);

    public List<FinExpenseItem> selectFinExpenseItemListByExpenseId(Long expenseId);

    /** 按报销单ID删除所有明细 */
    public int deleteByExpenseId(Long expenseId);
}
