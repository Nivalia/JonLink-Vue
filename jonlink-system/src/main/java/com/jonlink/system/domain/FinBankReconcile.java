package com.jonlink.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 银行对账单对象 fin_bank_reconcile
 *
 * @author jonlink
 */
public class FinBankReconcile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 银行账户ID */
    @Excel(name = "银行账户ID")
    private Long bankAccountId;

    /** 银行流水号 */
    @Excel(name = "银行流水号")
    private String bankTransNo;

    /** 交易日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "交易日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date transDate;

    /** 交易金额(正=收入 负=支出) */
    @Excel(name = "交易金额")
    private BigDecimal transAmount;

    /** 摘要/备注 */
    @Excel(name = "摘要")
    private String summary;

    /** 对方户名 */
    @Excel(name = "对方户名")
    private String counterParty;

    /** 对账状态:0=未对 1=已对 2=调整 */
    @Excel(name = "对账状态")
    private String status;

    /** 关联系统流水ID(对账后填写) */
    private Long cashFlowId;

    /** 银行名称(查询用) */
    private String bankName;

    /** 账户名称(查询用) */
    private String accountName;

    public void setId(Long id) { this.id = id; }
    public Long getId() { return id; }

    public void setBankAccountId(Long bankAccountId) { this.bankAccountId = bankAccountId; }
    public Long getBankAccountId() { return bankAccountId; }

    public void setBankTransNo(String bankTransNo) { this.bankTransNo = bankTransNo; }
    public String getBankTransNo() { return bankTransNo; }

    public void setTransDate(Date transDate) { this.transDate = transDate; }
    public Date getTransDate() { return transDate; }

    public void setTransAmount(BigDecimal transAmount) { this.transAmount = transAmount; }
    public BigDecimal getTransAmount() { return transAmount; }

    public void setSummary(String summary) { this.summary = summary; }
    public String getSummary() { return summary; }

    public void setCounterParty(String counterParty) { this.counterParty = counterParty; }
    public String getCounterParty() { return counterParty; }

    public void setStatus(String status) { this.status = status; }
    public String getStatus() { return status; }

    public void setCashFlowId(Long cashFlowId) { this.cashFlowId = cashFlowId; }
    public Long getCashFlowId() { return cashFlowId; }

    public void setBankName(String bankName) { this.bankName = bankName; }
    public String getBankName() { return bankName; }

    public void setAccountName(String accountName) { this.accountName = accountName; }
    public String getAccountName() { return accountName; }
}
