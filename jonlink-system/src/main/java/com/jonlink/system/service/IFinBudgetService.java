package com.jonlink.system.service;

import java.util.List;
import java.util.Map;
import com.jonlink.system.domain.FinBudget;

/**
 * 预算管理Service接口
 * 
 * @author jonlink
 * @date 2026-08-23
 */
public interface IFinBudgetService 
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
     * 批量删除预算管理
     * 
     * @param ids 需要删除的预算管理主键集合
     * @return 结果
     */
    public int deleteFinBudgetByIds(Long[] ids);

    /**
     * 删除预算管理信息
     * 
     * @param id 预算管理主键
     * @return 结果
     */
    public int deleteFinBudgetById(Long id);

    /**
     * 检查是否超预算
     * 
     * @param subjectCode 科目编码
     * @param periodCode 期间编码
     * @param amount 金额
     * @return 检查结果(true=超预算, false=未超预算)
     */
    public boolean checkBudget(String subjectCode, String periodCode, java.math.BigDecimal amount);

    /**
     * 获取预算执行报告
     * 
     * @param periodCode 期间编码
     * @return 预算执行报告
     */
    public Map<String, Object> getBudgetReport(String periodCode);
}
