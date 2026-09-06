package com.jonlink.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 费用报销单对象 fin_expense
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public class FinExpense extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 报销单ID */
    private Long id;

    /** 单号（BX+日期+流水） */
    @Excel(name = "单号", readConverterExp = "B=X+日期+流水")
    private String expenseNo;

    /** 申请人 */
    @Excel(name = "申请人")
    private String applicant;

    /** 部门 */
    @Excel(name = "部门")
    private String deptName;

    /** 报销总额 */
    @Excel(name = "报销总额")
    private BigDecimal totalAmount;

    /** 0管理费用 1销售费用 */
    @Excel(name = "费用类型")
    private String expenseType;

    /** 0草稿 1待审 2已审 3已付款 4驳回 */
    @Excel(name = "状态")
    private String status;

    /** 审批人 */
    @Excel(name = "审批人")
    private String approver;

    /** 审批时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审批时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date approveTime;

    /** 驳回原因 */
    @Excel(name = "驳回原因")
    private String rejectReason;

    /** 付款单ID */
    @Excel(name = "付款单ID")
    private Long paymentId;

    /** 凭证ID */
    @Excel(name = "凭证ID")
    private Long voucherId;

    /** voucherNo（关联查询） */
    @Excel(name = "voucherNo")
    private String voucherNo;
    /**
     * 明细数 (子查询)
     */
    private Long itemCount;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setExpenseNo(String expenseNo) 
    {
        this.expenseNo = expenseNo;
    }

    public String getExpenseNo() 
    {
        return expenseNo;
    }

    public void setApplicant(String applicant) 
    {
        this.applicant = applicant;
    }

    public String getApplicant() 
    {
        return applicant;
    }

    public void setDeptName(String deptName) 
    {
        this.deptName = deptName;
    }

    public String getDeptName() 
    {
        return deptName;
    }

    public void setTotalAmount(BigDecimal totalAmount) 
    {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmount() 
    {
        return totalAmount;
    }

    public void setExpenseType(String expenseType) 
    {
        this.expenseType = expenseType;
    }

    public String getExpenseType() 
    {
        return expenseType;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setApprover(String approver) 
    {
        this.approver = approver;
    }

    public String getApprover() 
    {
        return approver;
    }

    public void setApproveTime(Date approveTime) 
    {
        this.approveTime = approveTime;
    }

    public Date getApproveTime() 
    {
        return approveTime;
    }

    public void setRejectReason(String rejectReason) 
    {
        this.rejectReason = rejectReason;
    }

    public String getRejectReason() 
    {
        return rejectReason;
    }

    public void setPaymentId(Long paymentId) 
    {
        this.paymentId = paymentId;
    }

    public Long getPaymentId() 
    {
        return paymentId;
    }

    public void setVoucherId(Long voucherId) 
    {
        this.voucherId = voucherId;
    }

    public Long getVoucherId() 
    {
        return voucherId;
    }

    public void setVoucherNo(String voucherNo)
    {
        this.voucherNo = voucherNo;
    }

    public String getVoucherNo()
    {
        return voucherNo;
    }

    public void setItemCount(Long itemCount) { this.itemCount = itemCount; }
    public Long getItemCount() { return this.itemCount; }

public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("expenseNo", getExpenseNo())
            .append("applicant", getApplicant())
            .append("deptName", getDeptName())
            .append("totalAmount", getTotalAmount())
            .append("expenseType", getExpenseType())
            .append("status", getStatus())
            .append("approver", getApprover())
            .append("approveTime", getApproveTime())
            .append("rejectReason", getRejectReason())
            .append("paymentId", getPaymentId())
            .append("voucherId", getVoucherId())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
