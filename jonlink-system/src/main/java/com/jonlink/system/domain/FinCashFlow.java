package com.jonlink.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 资金流水对象 fin_cash_flow
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public class FinCashFlow extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 流水ID */
    private Long id;

    /** 银行账户ID */
    @Excel(name = "银行ID")
    private Long bankAccountId;

    /** 0流入 1流出 */
    @Excel(name = "方向")
    private String direction;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal amount;

    /** 账户余额（实时累计） */
    @Excel(name = "账户余额", readConverterExp = "实=时累计")
    private BigDecimal balanceAfter;

    /** 来源业务类型 */
    @Excel(name = "来源类型")
    private String bizType;

    /** 来源业务ID */
    @Excel(name = "来源ID")
    private Long bizId;

    /** 流水时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "流水时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date flowTime;

    /** bankAccountName（关联查询） */
    @Excel(name = "银行账户名")
    private String bankAccountName;
    /** bizNo（关联查询） */
    @Excel(name = "bizNo")
    private String bizNo;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setBankAccountId(Long bankAccountId) 
    {
        this.bankAccountId = bankAccountId;
    }

    public Long getBankAccountId() 
    {
        return bankAccountId;
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

    public void setBalanceAfter(BigDecimal balanceAfter) 
    {
        this.balanceAfter = balanceAfter;
    }

    public BigDecimal getBalanceAfter() 
    {
        return balanceAfter;
    }

    public void setBizType(String bizType) 
    {
        this.bizType = bizType;
    }

    public String getBizType() 
    {
        return bizType;
    }

    public void setBizId(Long bizId) 
    {
        this.bizId = bizId;
    }

    public Long getBizId() 
    {
        return bizId;
    }

    public void setFlowTime(Date flowTime) 
    {
        this.flowTime = flowTime;
    }

    public Date getFlowTime() 
    {
        return flowTime;
    }

    public void setBankAccountName(String bankAccountName)
    {
        this.bankAccountName = bankAccountName;
    }

    public String getBankAccountName()
    {
        return bankAccountName;
    }
    public void setBizNo(String bizNo)
    {
        this.bizNo = bizNo;
    }

    public String getBizNo()
    {
        return bizNo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("bankAccountId", getBankAccountId())
            .append("direction", getDirection())
            .append("amount", getAmount())
            .append("balanceAfter", getBalanceAfter())
            .append("bizType", getBizType())
            .append("bizId", getBizId())
            .append("flowTime", getFlowTime())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}
