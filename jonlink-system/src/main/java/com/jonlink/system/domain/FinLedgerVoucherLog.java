package com.jonlink.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 台账记账日志对象 fin_ledger_voucher_log
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public class FinLedgerVoucherLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 日志ID */
    private Long id;

    /** 台账行ID */
    @Excel(name = "台账行ID")
    private Long ledgerId;

    /** 保单号（冗余） */
    @Excel(name = "保单号", readConverterExp = "冗=余")
    private String ledgerPolicyNo;

    /** 台账保费（冗余快照） */
    @Excel(name = "台账保费", readConverterExp = "冗=余快照")
    private BigDecimal ledgerAmount;

    /** 生成凭证ID */
    @Excel(name = "凭证ID")
    private Long voucherId;

    /** 凭证号 */
    @Excel(name = "凭证号")
    private String voucherNo;

    /** 0台账记账 1上游结算记账 2下游结算记账 */
    @Excel(name = "记账类型")
    private String bookType;

    /** 结算记录ID（book_type=1/2 时） */
    @Excel(name = "结算ID", readConverterExp = "b=ook_type=1/2,时=")
    private Long settleRecordId;

    /** 记账人 */
    @Excel(name = "记账人")
    private String bookUser;

    /** 记账时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "记账时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date bookTime;

    /** 1有效 0已反记账 */
    @Excel(name = "状态")
    private String status;

    /** settleRecordNo（关联查询） */
    @Excel(name = "settleRecordNo")
    private String settleRecordNo;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setLedgerId(Long ledgerId) 
    {
        this.ledgerId = ledgerId;
    }

    public Long getLedgerId() 
    {
        return ledgerId;
    }

    public void setLedgerPolicyNo(String ledgerPolicyNo) 
    {
        this.ledgerPolicyNo = ledgerPolicyNo;
    }

    public String getLedgerPolicyNo() 
    {
        return ledgerPolicyNo;
    }

    public void setLedgerAmount(BigDecimal ledgerAmount) 
    {
        this.ledgerAmount = ledgerAmount;
    }

    public BigDecimal getLedgerAmount() 
    {
        return ledgerAmount;
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

    public void setBookType(String bookType) 
    {
        this.bookType = bookType;
    }

    public String getBookType() 
    {
        return bookType;
    }

    public void setSettleRecordId(Long settleRecordId) 
    {
        this.settleRecordId = settleRecordId;
    }

    public Long getSettleRecordId() 
    {
        return settleRecordId;
    }

    public void setBookUser(String bookUser) 
    {
        this.bookUser = bookUser;
    }

    public String getBookUser() 
    {
        return bookUser;
    }

    public void setBookTime(Date bookTime) 
    {
        this.bookTime = bookTime;
    }

    public Date getBookTime() 
    {
        return bookTime;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setSettleRecordNo(String settleRecordNo)
    {
        this.settleRecordNo = settleRecordNo;
    }

    public String getSettleRecordNo()
    {
        return settleRecordNo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("ledgerId", getLedgerId())
            .append("ledgerPolicyNo", getLedgerPolicyNo())
            .append("ledgerAmount", getLedgerAmount())
            .append("voucherId", getVoucherId())
            .append("voucherNo", getVoucherNo())
            .append("bookType", getBookType())
            .append("settleRecordId", getSettleRecordId())
            .append("bookUser", getBookUser())
            .append("bookTime", getBookTime())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
