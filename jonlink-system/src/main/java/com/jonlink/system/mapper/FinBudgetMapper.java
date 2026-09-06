package com.jonlink.system.mapper;

import java.math.BigDecimal;
import java.util.List;
import com.jonlink.system.domain.FinBudget;

/**
 * 预算管理Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-23
 */
public interface FinBudgetMapper 
{
    /**
     * 查询预算管理
     * 
     * @param id 预算管理主键
     * @return 预算管理
     */
    public FinBudget selectFinBudgetById(Long id);

    /**
     * 查询预算管理列表
     * 
     * @param finBudget 预算管理
     * @return 预算管理集合
     */
    public List<FinBudget> selectFinBudgetList(FinBudget finBudget);

    /**
     * 新增预算管理
     * 
     * @param finBudget 预算管理
     * @return 结果
     */
    public int insertFinBudget(FinBudget finBudget);

    /**
     * 修改预算管理
     * 
     * @param finBudget 预算管理
     * @return 结果
     */
    public int updateFinBudget(FinBudget finBudget);

    /**
     * 删除预算管理
     * 
     * @param id 预算管理主键
     * @return 结果
     */
    public int deleteFinBudgetById(Long id);

    /**
     * 批量删除预算管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinBudgetByIds(Long[] ids);

    /**
     * 查询预算使用情况
     * 
     * @param subjectCode 科目编码
     * @param periodCode 期间编码
     * @return 预算使用情况
     */
    public FinBudget getBudgetUsage(String subjectCode, String periodCode);
}
