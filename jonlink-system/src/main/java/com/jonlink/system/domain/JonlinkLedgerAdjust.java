package com.jonlink.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 台账批增退记录对象 jonlink_ledger_adjust
 * 
 * @author jonlink
 * @date 2026-09-05
 */
public class JonlinkLedgerAdjust extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 台账ID */
    @Excel(name = "台账ID")
    private Long ledgerId;

    /** 调整类型 1批增 2批退 */
    @Excel(name = "调整类型")
    private String adjustType;

    /** 调整保费金额 */
    @Excel(name = "调整保费")
    private BigDecimal adjustPremium;

    /** 调整原因 */
    @Excel(name = "调整原因")
    private String adjustReason;

    /** 调整日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "调整日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date adjustDate;

    /** 调整人 */
    @Excel(name = "调整人")
    private String adjustBy;

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

    public void setAdjustType(String adjustType) 
    {
        this.adjustType = adjustType;
    }

    public String getAdjustType() 
    {
        return adjustType;
    }

    public void setAdjustPremium(BigDecimal adjustPremium) 
    {
        this.adjustPremium = adjustPremium;
    }

    public BigDecimal getAdjustPremium() 
    {
        return adjustPremium;
    }

    public void setAdjustReason(String adjustReason) 
    {
        this.adjustReason = adjustReason;
    }

    public String getAdjustReason() 
    {
        return adjustReason;
    }

    public void setAdjustDate(Date adjustDate) 
    {
        this.adjustDate = adjustDate;
    }

    public Date getAdjustDate() 
    {
        return adjustDate;
    }

    public void setAdjustBy(String adjustBy) 
    {
        this.adjustBy = adjustBy;
    }

    public String getAdjustBy() 
    {
        return adjustBy;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("ledgerId", getLedgerId())
            .append("adjustType", getAdjustType())
            .append("adjustPremium", getAdjustPremium())
            .append("adjustReason", getAdjustReason())
            .append("adjustDate", getAdjustDate())
            .append("adjustBy", getAdjustBy())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
