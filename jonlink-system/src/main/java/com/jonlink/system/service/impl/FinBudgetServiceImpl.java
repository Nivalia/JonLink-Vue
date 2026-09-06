package com.jonlink.system.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.jonlink.common.exception.ServiceException;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.FinBudgetMapper;
import com.jonlink.system.domain.FinBudget;
import com.jonlink.system.service.IFinBudgetService;

/**
 * 预算管理Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-23
 */
@Service
public class FinBudgetServiceImpl implements IFinBudgetService
{
    @Autowired
    private FinBudgetMapper finBudgetMapper;

    /**
     * 查询预算管理
     * 
     * @param id 预算管理主键
     * @return 预算管理
     */
    @Override
    public FinBudget selectFinBudgetById(Long id)
    {
        return finBudgetMapper.selectFinBudgetById(id);
    }

    /**
     * 查询预算管理列表
     * 
     * @param finBudget 预算管理
     * @return 预算管理
     */
    @Override
    public List<FinBudget> selectFinBudgetList(FinBudget finBudget)
    {
        return finBudgetMapper.selectFinBudgetList(finBudget);
    }

    /**
     * 新增预算管理
     * 
     * @param finBudget 预算管理
     * @return 结果
     */
    @Override
    public int insertFinBudget(FinBudget finBudget)
    {
        finBudget.setCreateTime(DateUtils.getNowDate());
        return finBudgetMapper.insertFinBudget(finBudget);
    }

    /**
     * 修改预算管理
     * 
     * @param finBudget 预算管理
     * @return 结果
     */
    @Override
    public int updateFinBudget(FinBudget finBudget)
    {
        finBudget.setUpdateTime(DateUtils.getNowDate());
        return finBudgetMapper.updateFinBudget(finBudget);
    }

    /**
     * 批量删除预算管理
     * 
     * @param ids 需要删除的预算管理主键
     * @return 结果
     */
    @Override
    public int deleteFinBudgetByIds(Long[] ids)
    {
        return finBudgetMapper.deleteFinBudgetByIds(ids);
    }

    /**
     * 删除预算管理信息
     * 
     * @param id 预算管理主键
     * @return 结果
     */
    @Override
    public int deleteFinBudgetById(Long id)
    {
        return finBudgetMapper.deleteFinBudgetById(id);
    }

    /**
     * 检查是否超预算
     * 
     * @param subjectCode 科目编码
     * @param periodCode 期间编码
     * @param amount 金额
     * @return 检查结果(true=超预算, false=未超预算)
     */
    @Override
    public boolean checkBudget(String subjectCode, String periodCode, BigDecimal amount)
    {
        FinBudget budget = finBudgetMapper.getBudgetUsage(subjectCode, periodCode);
        if (budget == null || budget.getBudgetAmount() == null) {
            return false;
        }
        BigDecimal actualAmount = budget.getActualAmount() == null ? BigDecimal.ZERO : budget.getActualAmount();
        BigDecimal totalAmount = actualAmount.add(amount);
        if (totalAmount.compareTo(budget.getBudgetAmount()) > 0) {
            return true;
        }
        if (budget.getAlertThreshold() != null && budget.getAlertThreshold().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal usageRate = totalAmount.multiply(new BigDecimal("100")).divide(budget.getBudgetAmount(), 2, BigDecimal.ROUND_HALF_UP);
            if (usageRate.compareTo(budget.getAlertThreshold()) >= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取预算执行报告
     * 
     * @param periodCode 期间编码
     * @return 预算执行报告
     */
    @Override
    public Map<String, Object> getBudgetReport(String periodCode)
    {
        FinBudget query = new FinBudget();
        query.setPeriodCode(periodCode);
        List<FinBudget> list = finBudgetMapper.selectFinBudgetList(query);
        
        Map<String, Object> report = new HashMap<>();
        report.put("periodCode", periodCode);
        report.put("details", list);
        
        BigDecimal totalBudget = BigDecimal.ZERO;
        BigDecimal totalActual = BigDecimal.ZERO;
        List<Map<String, Object>> alerts = new ArrayList<>();
        
        for (FinBudget item : list) {
            totalBudget = totalBudget.add(item.getBudgetAmount() != null ? item.getBudgetAmount() : BigDecimal.ZERO);
            totalActual = totalActual.add(item.getActualAmount() != null ? item.getActualAmount() : BigDecimal.ZERO);
            
            if (item.getAlertThreshold() != null && item.getBudgetAmount() != null && item.getBudgetAmount().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal usageRate = (item.getActualAmount() != null ? item.getActualAmount() : BigDecimal.ZERO)
                    .multiply(new BigDecimal("100"))
                    .divide(item.getBudgetAmount(), 2, BigDecimal.ROUND_HALF_UP);
                if (usageRate.compareTo(item.getAlertThreshold()) >= 0) {
                    Map<String, Object> alert = new HashMap<>();
                    alert.put("subjectCode", item.getSubjectCode());
                    alert.put("subjectName", item.getSubjectName());
                    alert.put("usageRate", usageRate);
                    alert.put("alertThreshold", item.getAlertThreshold());
                    alerts.add(alert);
                }
            }
        }
        
        report.put("totalBudget", totalBudget);
        report.put("totalActual", totalActual);
        report.put("alerts", alerts);
        
        return report;
    }
}
