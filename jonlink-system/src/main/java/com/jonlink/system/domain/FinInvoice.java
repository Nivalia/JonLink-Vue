package com.jonlink.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 发票对象 fin_invoice
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public class FinInvoice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 发票ID */
    private Long id;

    /** 发票号码（唯一） */
    @Excel(name = "发票号码", readConverterExp = "唯=一")
    private String invoiceNo;

    /** 0增值税专票 1普票 */
    @Excel(name = "发票类型")
    private String invoiceType;

    /** 开票/收票单位 */
    @Excel(name = "往来单位")
    private Long partnerId;

    /** 0进项 1销项 */
    @Excel(name = "方向")
    private String direction;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal amount;

    /** 税额 */
    @Excel(name = "税额")
    private BigDecimal taxAmount;

    /** 开票日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开票日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date invoiceDate;

    /** 关联业务类型 */
    @Excel(name = "关联类型")
    private String relateType;

    /** 关联业务ID */
    @Excel(name = "关联ID")
    private Long relateId;

    /** 0未核销 1已核销 */
    @Excel(name = "核销状态")
    private String status;

    /** partnerName（关联查询） */
    @Excel(name = "partnerName")
    private String partnerName;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setInvoiceNo(String invoiceNo) 
    {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceNo() 
    {
        return invoiceNo;
    }

    public void setInvoiceType(String invoiceType) 
    {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceType() 
    {
        return invoiceType;
    }

    public void setPartnerId(Long partnerId) 
    {
        this.partnerId = partnerId;
    }

    public Long getPartnerId() 
    {
        return partnerId;
    }

    public void setDirection(String direction) 
    {
        this.direction = direction;
    }

    public String getDirection() 
    {
        return direction;
    }

    public void setAmount(BigDecimal amount) 
    {
        this.amount = amount;
    }

    public BigDecimal getAmount() 
    {
        return amount;
    }

    public void setTaxAmount(BigDecimal taxAmount) 
    {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTaxAmount() 
    {
        return taxAmount;
    }

    public void setInvoiceDate(Date invoiceDate) 
    {
        this.invoiceDate = invoiceDate;
    }

    public Date getInvoiceDate() 
    {
        return invoiceDate;
    }

    public void setRelateType(String relateType) 
    {
        this.relateType = relateType;
    }

    public String getRelateType() 
    {
        return relateType;
    }

    public void setRelateId(Long relateId) 
    {
        this.relateId = relateId;
    }

    public Long getRelateId() 
    {
        return relateId;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setPartnerName(String partnerName)
    {
        this.partnerName = partnerName;
    }

    public String getPartnerName()
    {
        return partnerName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("invoiceNo", getInvoiceNo())
            .append("invoiceType", getInvoiceType())
            .append("partnerId", getPartnerId())
            .append("direction", getDirection())
            .append("amount", getAmount())
            .append("taxAmount", getTaxAmount())
            .append("invoiceDate", getInvoiceDate())
            .append("relateType", getRelateType())
            .append("relateId", getRelateId())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
