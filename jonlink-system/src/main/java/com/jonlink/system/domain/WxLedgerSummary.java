package com.jonlink.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 台账日汇总对象 wx_ledger_summary
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public class WxLedgerSummary extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 统计日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "统计日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date statDate;

    /** 台账类型 */
    @Excel(name = "台账类型")
    private String ledgerType;

    /** 当日笔数 */
    @Excel(name = "当日笔数")
    private Long totalCount;

    /** 当日金额合计 */
    @Excel(name = "当日金额合计")
    private BigDecimal totalAmount;

    /** 当日积分合计 */
    @Excel(name = "当日积分合计")
    private BigDecimal totalPoints;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setStatDate(Date statDate) 
    {
        this.statDate = statDate;
    }

    public Date getStatDate() 
    {
        return statDate;
    }

    public void setLedgerType(String ledgerType) 
    {
        this.ledgerType = ledgerType;
    }

    public String getLedgerType() 
    {
        return ledgerType;
    }

    public void setTotalCount(Long totalCount) 
    {
        this.totalCount = totalCount;
    }

    public Long getTotalCount() 
    {
        return totalCount;
    }

    public void setTotalAmount(BigDecimal totalAmount) 
    {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmount() 
    {
        return totalAmount;
    }

    public void setTotalPoints(BigDecimal totalPoints) 
    {
        this.totalPoints = totalPoints;
    }

    public BigDecimal getTotalPoints() 
    {
        return totalPoints;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("statDate", getStatDate())
            .append("ledgerType", getLedgerType())
            .append("totalCount", getTotalCount())
            .append("totalAmount", getTotalAmount())
            .append("totalPoints", getTotalPoints())
            .toString();
    }
}
