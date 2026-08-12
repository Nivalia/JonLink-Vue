package com.jonlink.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 结算记录对象 jonlink_settle_record
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public class JonlinkSettleRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 结算单号(唯一) */
    @Excel(name = "结算单号(唯一)")
    private String settleNo;

    /** 方向 0上游结费(收入) 1下游结费(支出) */
    @Excel(name = "方向 0上游结费(收入) 1下游结费(支出)")
    private String direction;

    /** 关联台账行(单笔结算) */
    @Excel(name = "关联台账行(单笔结算)")
    private Long ledgerId;

    /** 保单号(冗余) */
    @Excel(name = "保单号(冗余)")
    private String policyNo;

    /** 结算金额(如上游=上游税后佣金, 下游=下游佣金) */
    @Excel(name = "结算金额(如上游=上游税后佣金, 下游=下游佣金)")
    private BigDecimal amount;

    /** 状态 0作废 1已结算 */
    @Excel(name = "状态 0作废 1已结算")
    private String settleStatus;

    /** 经手人 */
    @Excel(name = "经手人")
    private String opUser;

    /** 结算时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结算时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date settleDate;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setSettleNo(String settleNo) 
    {
        this.settleNo = settleNo;
    }

    public String getSettleNo() 
    {
        return settleNo;
    }

    public void setDirection(String direction) 
    {
        this.direction = direction;
    }

    public String getDirection() 
    {
        return direction;
    }

    public void setLedgerId(Long ledgerId) 
    {
        this.ledgerId = ledgerId;
    }

    public Long getLedgerId() 
    {
        return ledgerId;
    }

    public void setPolicyNo(String policyNo) 
    {
        this.policyNo = policyNo;
    }

    public String getPolicyNo() 
    {
        return policyNo;
    }

    public void setAmount(BigDecimal amount) 
    {
        this.amount = amount;
    }

    public BigDecimal getAmount() 
    {
        return amount;
    }

    public void setSettleStatus(String settleStatus) 
    {
        this.settleStatus = settleStatus;
    }

    public String getSettleStatus() 
    {
        return settleStatus;
    }

    public void setOpUser(String opUser) 
    {
        this.opUser = opUser;
    }

    public String getOpUser() 
    {
        return opUser;
    }

    public void setSettleDate(Date settleDate) 
    {
        this.settleDate = settleDate;
    }

    public Date getSettleDate() 
    {
        return settleDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("settleNo", getSettleNo())
            .append("direction", getDirection())
            .append("ledgerId", getLedgerId())
            .append("policyNo", getPolicyNo())
            .append("amount", getAmount())
            .append("settleStatus", getSettleStatus())
            .append("opUser", getOpUser())
            .append("settleDate", getSettleDate())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
