package com.jonlink.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 银行账户对象 fin_bank_account
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public class FinBankAccount extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 账户ID */
    private Long id;

    /** 账户名称 */
    @Excel(name = "账户名称")
    private String accountName;

    /** 账号（唯一） */
    @Excel(name = "账号", readConverterExp = "唯=一")
    private String accountNo;

    /** 开户行 */
    @Excel(name = "开户行")
    private String bankName;

    /** 期初余额 */
    @Excel(name = "期初余额")
    private BigDecimal initBalance;

    /** 当前余额（实时累计） */
    @Excel(name = "当前余额", readConverterExp = "实=时累计")
    private BigDecimal currentBalance;

    /** 0停用 1启用 */
    @Excel(name = "状态")
    private String status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setAccountName(String accountName) 
    {
        this.accountName = accountName;
    }

    public String getAccountName() 
    {
        return accountName;
    }

    public void setAccountNo(String accountNo) 
    {
        this.accountNo = accountNo;
    }

    public String getAccountNo() 
    {
        return accountNo;
    }

    public void setBankName(String bankName) 
    {
        this.bankName = bankName;
    }

    public String getBankName() 
    {
        return bankName;
    }

    public void setInitBalance(BigDecimal initBalance) 
    {
        this.initBalance = initBalance;
    }

    public BigDecimal getInitBalance() 
    {
        return initBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) 
    {
        this.currentBalance = currentBalance;
    }

    public BigDecimal getCurrentBalance() 
    {
        return currentBalance;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("accountName", getAccountName())
            .append("accountNo", getAccountNo())
            .append("bankName", getBankName())
            .append("initBalance", getInitBalance())
            .append("currentBalance", getCurrentBalance())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
