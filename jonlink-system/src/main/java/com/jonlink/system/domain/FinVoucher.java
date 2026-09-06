package com.jonlink.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 记账凭证对象 fin_voucher
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public class FinVoucher extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 凭证ID */
    private Long id;

    /** 凭证号（记-yyyyMM-NNNN） */
    @Excel(name = "凭证号", readConverterExp = "记=-yyyyMM-NNNN")
    private String voucherNo;

    /** 期间编码 */
    @Excel(name = "期间编码")
    private String periodCode;

    /** 凭证日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "凭证日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date voucherDate;

    /** 来源类型（manual/ledger/settle_up/settle_down/receipt/payment/expense/period_close） */
    @Excel(name = "来源类型", readConverterExp = "m=anual/ledger/settle_up/settle_down/receipt/payment/expense/period_close")
    private String sourceType;

    /** 来源业务ID（台账行/结算记录等） */
    @Excel(name = "来源ID", readConverterExp = "台=账行/结算记录等")
    private Long sourceId;

    /** 摘要 */
    @Excel(name = "摘要")
    private String summary;

    /** 借方合计 */
    @Excel(name = "借方合计")
    private BigDecimal totalDebit;

    /** 贷方合计 */
    @Excel(name = "贷方合计")
    private BigDecimal totalCredit;

    /** 0草稿 1已审核 2已过账 3已作废 */
    @Excel(name = "状态")
    private String status;

    /** 审核人 */
    @Excel(name = "审核人")
    private String auditor;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date auditTime;

    /** 过账人 */
    @Excel(name = "过账人")
    private String poster;

    /** 过账时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "过账时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date postTime;

    /** 制单人 */
    @Excel(name = "制单人")
    private String voucherMaker;

    /** 分录列表(冗余字段,不映射 DB;录入/展示/打印用) */
    private List<FinVoucherEntry> entries;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setVoucherNo(String voucherNo) 
    {
        this.voucherNo = voucherNo;
    }

    public String getVoucherNo() 
    {
        return voucherNo;
    }

    public void setPeriodCode(String periodCode) 
    {
        this.periodCode = periodCode;
    }

    public String getPeriodCode() 
    {
        return periodCode;
    }

    public void setVoucherDate(Date voucherDate) 
    {
        this.voucherDate = voucherDate;
    }

    public Date getVoucherDate() 
    {
        return voucherDate;
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

    public void setSummary(String summary) 
    {
        this.summary = summary;
    }

    public String getSummary() 
    {
        return summary;
    }

    public void setTotalDebit(BigDecimal totalDebit) 
    {
        this.totalDebit = totalDebit;
    }

    public BigDecimal getTotalDebit() 
    {
        return totalDebit;
    }

    public void setTotalCredit(BigDecimal totalCredit) 
    {
        this.totalCredit = totalCredit;
    }

    public BigDecimal getTotalCredit() 
    {
        return totalCredit;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setAuditor(String auditor) 
    {
        this.auditor = auditor;
    }

    public String getAuditor() 
    {
        return auditor;
    }

    public void setAuditTime(Date auditTime) 
    {
        this.auditTime = auditTime;
    }

    public Date getAuditTime() 
    {
        return auditTime;
    }

    public void setPoster(String poster) 
    {
        this.poster = poster;
    }

    public String getPoster() 
    {
        return poster;
    }

    public void setPostTime(Date postTime) 
    {
        this.postTime = postTime;
    }

    public Date getPostTime() 
    {
        return postTime;
    }

    public void setVoucherMaker(String voucherMaker)
    {
        this.voucherMaker = voucherMaker;
    }

    public String getVoucherMaker()
    {
        return voucherMaker;
    }

    public void setEntries(List<FinVoucherEntry> entries)
    {
        this.entries = entries;
    }

    public List<FinVoucherEntry> getEntries()
    {
        return entries;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("voucherNo", getVoucherNo())
            .append("periodCode", getPeriodCode())
            .append("voucherDate", getVoucherDate())
            .append("sourceType", getSourceType())
            .append("sourceId", getSourceId())
            .append("summary", getSummary())
            .append("totalDebit", getTotalDebit())
            .append("totalCredit", getTotalCredit())
            .append("status", getStatus())
            .append("auditor", getAuditor())
            .append("auditTime", getAuditTime())
            .append("poster", getPoster())
            .append("postTime", getPostTime())
            .append("voucherMaker", getVoucherMaker())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
