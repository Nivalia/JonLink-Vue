package com.jonlink.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 付款单对象 fin_payment
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public class FinPayment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 付款单ID */
    private Long id;

    /** 单号（FK+日期+流水） */
    @Excel(name = "单号", readConverterExp = "F=K+日期+流水")
    private String billNo;

    /** 往来单位ID */
    @Excel(name = "往来ID")
    private Long partnerId;

    /** 银行账户ID */
    @Excel(name = "银行ID")
    private Long bankAccountId;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal amount;

    /** 业务类型（settle_down/expense/manual 等） */
    @Excel(name = "业务类型", readConverterExp = "s=ettle_down/expense/manual,等=")
    private String bizType;

    /** 来源类型 */
    @Excel(name = "来源类型")
    private String sourceType;

    /** 来源业务ID */
    @Excel(name = "来源ID")
    private Long sourceId;

    /** 单据日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "单据日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date billDate;

    /** 0草稿 1已确认 */
    @Excel(name = "状态")
    private String status;

    /** 关联凭证 */
    @Excel(name = "关联凭证")
    private Long voucherId;

    /** 确认时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "确认时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date confirmTime;

    /** partnerName（关联查询） */
    @Excel(name = "partnerName")
    private String partnerName;
    /** bankAccountName（关联查询） */
    @Excel(name = "银行账户名")
    private String bankAccountName;
    /** voucherNo（关联查询） */
    @Excel(name = "voucherNo")
    private String voucherNo;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setBillNo(String billNo) 
    {
        this.billNo = billNo;
    }

    public String getBillNo() 
    {
        return billNo;
    }

    public void setPartnerId(Long partnerId) 
    {
        this.partnerId = partnerId;
    }

    public Long getPartnerId() 
    {
        return partnerId;
    }

    public void setBankAccountId(Long bankAccountId) 
    {
        this.bankAccountId = bankAccountId;
    }

    public Long getBankAccountId() 
    {
        return bankAccountId;
    }

    public void setAmount(BigDecimal amount) 
    {
        this.amount = amount;
    }

    public BigDecimal getAmount() 
    {
        return amount;
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

    public void setBillDate(Date billDate) 
    {
        this.billDate = billDate;
    }

    public Date getBillDate() 
    {
        return billDate;
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

    public void setConfirmTime(Date confirmTime) 
    {
        this.confirmTime = confirmTime;
    }

    public Date getConfirmTime() 
    {
        return confirmTime;
    }

    public void setPartnerName(String partnerName)
    {
        this.partnerName = partnerName;
    }

    public String getPartnerName()
    {
        return partnerName;
    }
    public void setBankAccountName(String bankAccountName)
    {
        this.bankAccountName = bankAccountName;
    }

    public String getBankAccountName()
    {
        return bankAccountName;
    }
    public void setVoucherNo(String voucherNo)
    {
        this.voucherNo = voucherNo;
    }

    public String getVoucherNo()
    {
        return voucherNo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("billNo", getBillNo())
            .append("partnerId", getPartnerId())
            .append("bankAccountId", getBankAccountId())
            .append("amount", getAmount())
            .append("bizType", getBizType())
            .append("sourceType", getSourceType())
            .append("sourceId", getSourceId())
            .append("billDate", getBillDate())
            .append("status", getStatus())
            .append("voucherId", getVoucherId())
            .append("confirmTime", getConfirmTime())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
