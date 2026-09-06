package com.jonlink.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 凭证分录对象 fin_voucher_entry
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public class FinVoucherEntry extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 分录ID */
    private Long id;

    /** 凭证ID */
    @Excel(name = "凭证ID")
    private Long voucherId;

    /** 科目ID */
    @Excel(name = "科目ID")
    private Long subjectId;

    /** 摘要 */
    @Excel(name = "摘要")
    private String summary;

    /** 借方金额 */
    @Excel(name = "借方金额")
    private BigDecimal debitAmount;

    /** 贷方金额 */
    @Excel(name = "贷方金额")
    private BigDecimal creditAmount;

    /** 往来单位ID（辅助核算，可空） */
    @Excel(name = "往来ID", readConverterExp = "辅=助核算，可空")
    private Long partnerId;

    /** 银行账户ID（辅助核算，可空） */
    @Excel(name = "银行ID", readConverterExp = "辅=助核算，可空")
    private Long bankAccountId;

    /** 排序 */
    @Excel(name = "排序")
    private Long sortOrder;

    /** voucherNo（关联查询） */
    @Excel(name = "voucherNo")
    private String voucherNo;
    /** subjectCode（关联查询） */
    @Excel(name = "subjectCode")
    private String subjectCode;
    /** subjectName（关联查询） */
    @Excel(name = "subjectName")
    private String subjectName;
    /** partnerName（关联查询） */
    @Excel(name = "partnerName")
    private String partnerName;
    /** bankAccountName（关联查询） */
    @Excel(name = "银行账户名")
    private String bankAccountName;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setVoucherId(Long voucherId) 
    {
        this.voucherId = voucherId;
    }

    public Long getVoucherId() 
    {
        return voucherId;
    }

    public void setSubjectId(Long subjectId) 
    {
        this.subjectId = subjectId;
    }

    public Long getSubjectId() 
    {
        return subjectId;
    }

    public void setSummary(String summary) 
    {
        this.summary = summary;
    }

    public String getSummary() 
    {
        return summary;
    }

    public void setDebitAmount(BigDecimal debitAmount) 
    {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getDebitAmount() 
    {
        return debitAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) 
    {
        this.creditAmount = creditAmount;
    }

    public BigDecimal getCreditAmount() 
    {
        return creditAmount;
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

    public void setSortOrder(Long sortOrder) 
    {
        this.sortOrder = sortOrder;
    }

    public Long getSortOrder() 
    {
        return sortOrder;
    }

    public void setVoucherNo(String voucherNo)
    {
        this.voucherNo = voucherNo;
    }

    public String getVoucherNo()
    {
        return voucherNo;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("voucherId", getVoucherId())
            .append("subjectId", getSubjectId())
            .append("summary", getSummary())
            .append("debitAmount", getDebitAmount())
            .append("creditAmount", getCreditAmount())
            .append("partnerId", getPartnerId())
            .append("bankAccountId", getBankAccountId())
            .append("sortOrder", getSortOrder())
            .toString();
    }
}
