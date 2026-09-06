package com.jonlink.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 应收单对象 fin_receivable
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public class FinReceivable extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 应收单ID */
    private Long id;

    /** 单据号（RE+日期+流水） */
    @Excel(name = "单据号", readConverterExp = "R=E+日期+流水")
    private String docNo;

    /** 往来单位ID */
    @Excel(name = "往来ID")
    private Long partnerId;

    /** 业务类型（ledger_premium/ledger_up_commission/manual 等） */
    @Excel(name = "业务类型", readConverterExp = "l=edger_premium/ledger_up_commission/manual,等=")
    private String bizType;

    /** 来源类型 */
    @Excel(name = "来源类型")
    private String sourceType;

    /** 来源业务ID */
    @Excel(name = "来源ID")
    private Long sourceId;

    /** 单据金额 */
    @Excel(name = "单据金额")
    private BigDecimal amount;

    /** 已核销金额 */
    @Excel(name = "已核销金额")
    private BigDecimal paidAmount;

    /** 未核销余额（=amount-paid_amount） */
    @Excel(name = "未核销余额", readConverterExp = "==amount-paid_amount")
    private BigDecimal remainAmount;

    /** 到期日 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "到期日", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dueDate;

    /** 0未结清 1已结清 */
    @Excel(name = "状态")
    private String status;

    /** 记账凭证ID（来源凭证） */
    @Excel(name = "记账凭证", readConverterExp = "来=源凭证")
    private Long voucherId;

    /** partnerName（关联查询） */
    @Excel(name = "partnerName")
    private String partnerName;
    /** voucherNo（关联查询） */
    @Excel(name = "voucherNo")
    private String voucherNo;
    /** sourceNo（关联查询） */
    @Excel(name = "sourceNo")
    private String sourceNo;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setDocNo(String docNo) 
    {
        this.docNo = docNo;
    }

    public String getDocNo() 
    {
        return docNo;
    }

    public void setPartnerId(Long partnerId) 
    {
        this.partnerId = partnerId;
    }

    public Long getPartnerId() 
    {
        return partnerId;
    }

    public void setBizType(String bizType) 
    {
        this.bizType = bizType;
    }

    public String getBizType() 
    {
        return bizType;
    }

    public void setSourceType(String sourceType) 
    {
        this.sourceType = sourceType;
    }

    public String getSourceType() 
    {
        return sourceType;
    }

    public void setSourceId(Long sourceId) 
    {
        this.sourceId = sourceId;
    }

    public Long getSourceId() 
    {
        return sourceId;
    }

    public void setAmount(BigDecimal amount) 
    {
        this.amount = amount;
    }

    public BigDecimal getAmount() 
    {
        return amount;
    }

    public void setPaidAmount(BigDecimal paidAmount) 
    {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getPaidAmount() 
    {
        return paidAmount;
    }

    public void setRemainAmount(BigDecimal remainAmount) 
    {
        this.remainAmount = remainAmount;
    }

    public BigDecimal getRemainAmount() 
    {
        return remainAmount;
    }

    public void setDueDate(Date dueDate) 
    {
        this.dueDate = dueDate;
    }

    public Date getDueDate() 
    {
        return dueDate;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setVoucherId(Long voucherId) 
    {
        this.voucherId = voucherId;
    }

    public Long getVoucherId() 
    {
        return voucherId;
    }

    public void setPartnerName(String partnerName)
    {
        this.partnerName = partnerName;
    }

    public String getPartnerName()
    {
        return partnerName;
    }
    public void setVoucherNo(String voucherNo)
    {
        this.voucherNo = voucherNo;
    }

    public String getVoucherNo()
    {
        return voucherNo;
    }
    public void setSourceNo(String sourceNo)
    {
        this.sourceNo = sourceNo;
    }

    public String getSourceNo()
    {
        return sourceNo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("docNo", getDocNo())
            .append("partnerId", getPartnerId())
            .append("bizType", getBizType())
            .append("sourceType", getSourceType())
            .append("sourceId", getSourceId())
            .append("amount", getAmount())
            .append("paidAmount", getPaidAmount())
            .append("remainAmount", getRemainAmount())
            .append("dueDate", getDueDate())
            .append("status", getStatus())
            .append("voucherId", getVoucherId())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
