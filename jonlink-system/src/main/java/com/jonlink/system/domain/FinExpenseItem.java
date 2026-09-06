package com.jonlink.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 报销明细对象 fin_expense_item
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public class FinExpenseItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 明细ID */
    private Long id;

    /** 报销单ID */
    @Excel(name = "报销单ID")
    private Long expenseId;

    /** 费用项目 */
    @Excel(name = "费用项目")
    private String itemName;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal amount;

    /** 关联发票ID */
    @Excel(name = "发票ID")
    private Long invoiceId;

    /** 排序 */
    @Excel(name = "排序")
    private Long sortOrder;

    /** expenseNo（关联查询） */
    @Excel(name = "expenseNo")
    private String expenseNo;
    /** invoiceNo（关联查询） */
    @Excel(name = "invoiceNo")
    private String invoiceNo;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setExpenseId(Long expenseId) 
    {
        this.expenseId = expenseId;
    }

    public Long getExpenseId() 
    {
        return expenseId;
    }

    public void setItemName(String itemName) 
    {
        this.itemName = itemName;
    }

    public String getItemName() 
    {
        return itemName;
    }

    public void setAmount(BigDecimal amount) 
    {
        this.amount = amount;
    }

    public BigDecimal getAmount() 
    {
        return amount;
    }

    public void setInvoiceId(Long invoiceId) 
    {
        this.invoiceId = invoiceId;
    }

    public Long getInvoiceId() 
    {
        return invoiceId;
    }

    public void setSortOrder(Long sortOrder) 
    {
        this.sortOrder = sortOrder;
    }

    public Long getSortOrder() 
    {
        return sortOrder;
    }

    public void setExpenseNo(String expenseNo)
    {
        this.expenseNo = expenseNo;
    }

    public String getExpenseNo()
    {
        return expenseNo;
    }
    public void setInvoiceNo(String invoiceNo)
    {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceNo()
    {
        return invoiceNo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("expenseId", getExpenseId())
            .append("itemName", getItemName())
            .append("amount", getAmount())
            .append("invoiceId", getInvoiceId())
            .append("remark", getRemark())
            .append("sortOrder", getSortOrder())
            .toString();
    }
}
