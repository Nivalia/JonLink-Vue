package com.jonlink.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 核销记录对象 fin_allocation
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public class FinAllocation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 核销ID */
    private Long id;

    /** 0应收 1应付 */
    @Excel(name = "类型")
    private String docType;

    /** 应收/应付单ID */
    @Excel(name = "应收单ID")
    private Long docId;

    /** 收款单ID（doc_type=0 时） */
    @Excel(name = "收款单ID", readConverterExp = "d=oc_type=0,时=")
    private Long receiptId;

    /** 付款单ID（doc_type=1 时） */
    @Excel(name = "付款单ID", readConverterExp = "d=oc_type=1,时=")
    private Long paymentId;

    /** 核销金额 */
    @Excel(name = "核销金额")
    private BigDecimal amount;

    /** 核销时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "核销时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date allocateTime;

    /** docNo（关联查询） */
    @Excel(name = "docNo")
    private String docNo;
    /** receiptNo（关联查询） */
    @Excel(name = "receiptNo")
    private String receiptNo;
    /** paymentNo（关联查询） */
    @Excel(name = "paymentNo")
    private String paymentNo;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setDocType(String docType) 
    {
        this.docType = docType;
    }

    public String getDocType() 
    {
        return docType;
    }

    public void setDocId(Long docId) 
    {
        this.docId = docId;
    }

    public Long getDocId() 
    {
        return docId;
    }

    public void setReceiptId(Long receiptId) 
    {
        this.receiptId = receiptId;
    }

    public Long getReceiptId() 
    {
        return receiptId;
    }

    public void setPaymentId(Long paymentId) 
    {
        this.paymentId = paymentId;
    }

    public Long getPaymentId() 
    {
        return paymentId;
    }

    public void setAmount(BigDecimal amount) 
    {
        this.amount = amount;
    }

    public BigDecimal getAmount() 
    {
        return amount;
    }

    public void setAllocateTime(Date allocateTime) 
    {
        this.allocateTime = allocateTime;
    }

    public Date getAllocateTime() 
    {
        return allocateTime;
    }

    public void setDocNo(String docNo)
    {
        this.docNo = docNo;
    }

    public String getDocNo()
    {
        return docNo;
    }
    public void setReceiptNo(String receiptNo)
    {
        this.receiptNo = receiptNo;
    }

    public String getReceiptNo()
    {
        return receiptNo;
    }
    public void setPaymentNo(String paymentNo)
    {
        this.paymentNo = paymentNo;
    }

    public String getPaymentNo()
    {
        return paymentNo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("docType", getDocType())
            .append("docId", getDocId())
            .append("receiptId", getReceiptId())
            .append("paymentId", getPaymentId())
            .append("amount", getAmount())
            .append("allocateTime", getAllocateTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}
