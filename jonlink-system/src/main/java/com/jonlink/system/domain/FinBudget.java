package com.jonlink.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 预算管理对象 fin_budget
 * 
 * @author jonlink
 * @date 2026-08-23
 */
public class FinBudget extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 预算ID */
    private Long id;

    /** 科目编码 */
    @Excel(name = "科目编码")
    private String subjectCode;

    /** 科目名称 */
    @Excel(name = "科目名称")
    private String subjectName;

    /** 期间编码 */
    @Excel(name = "期间编码")
    private String periodCode;

    /** 预算额 */
    @Excel(name = "预算额")
    private BigDecimal budgetAmount;

    /** 实际额 */
    @Excel(name = "实际额")
    private BigDecimal actualAmount;

    /** 预警阈值(%) */
    @Excel(name = "预警阈值")
    private BigDecimal alertThreshold;

    /** 状态(0正常 1停用) */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setSubjectCode(String subjectCode) 
    {
        this.subjectCode = subjectCode;
    }

    public String getSubjectCode() 
    {
        return subjectCode;
    }

    public void setSubjectName(String subjectName) 
    {
        this.subjectName = subjectName;
    }

    public String getSubjectName() 
    {
        return subjectName;
    }

    public void setPeriodCode(String periodCode) 
    {
        this.periodCode = periodCode;
    }

    public String getPeriodCode() 
    {
        return periodCode;
    }

    public void setBudgetAmount(BigDecimal budgetAmount) 
    {
        this.budgetAmount = budgetAmount;
    }

    public BigDecimal getBudgetAmount() 
    {
        return budgetAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) 
    {
        this.actualAmount = actualAmount;
    }

    public BigDecimal getActualAmount() 
    {
        return actualAmount;
    }

    public void setAlertThreshold(BigDecimal alertThreshold) 
    {
        this.alertThreshold = alertThreshold;
    }

    public BigDecimal getAlertThreshold() 
    {
        return alertThreshold;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("subjectCode", getSubjectCode())
            .append("subjectName", getSubjectName())
            .append("periodCode", getPeriodCode())
            .append("budgetAmount", getBudgetAmount())
            .append("actualAmount", getActualAmount())
            .append("alertThreshold", getAlertThreshold())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
