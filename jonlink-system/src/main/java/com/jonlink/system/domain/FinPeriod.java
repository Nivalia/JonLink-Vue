package com.jonlink.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 会计期间对象 fin_period
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public class FinPeriod extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 期间ID */
    private Long id;

    /** 期间编码（如 202608） */
    @Excel(name = "期间编码", readConverterExp = "如=,2=02608")
    private String periodCode;

    /** 期间名称（如 2026年8月） */
    @Excel(name = "期间名称", readConverterExp = "如=,2=026年8月")
    private String periodName;

    /** 开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /** 结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endDate;

    /** 0未结账 1已结账 */
    @Excel(name = "状态")
    private String status;

    /** 结账人 */
    @Excel(name = "结账人")
    private String closeUser;

    /** 结账时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结账时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date closeTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setPeriodCode(String periodCode) 
    {
        this.periodCode = periodCode;
    }

    public String getPeriodCode() 
    {
        return periodCode;
    }

    public void setPeriodName(String periodName) 
    {
        this.periodName = periodName;
    }

    public String getPeriodName() 
    {
        return periodName;
    }

    public void setStartDate(Date startDate) 
    {
        this.startDate = startDate;
    }

    public Date getStartDate() 
    {
        return startDate;
    }

    public void setEndDate(Date endDate) 
    {
        this.endDate = endDate;
    }

    public Date getEndDate() 
    {
        return endDate;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setCloseUser(String closeUser) 
    {
        this.closeUser = closeUser;
    }

    public String getCloseUser() 
    {
        return closeUser;
    }

    public void setCloseTime(Date closeTime) 
    {
        this.closeTime = closeTime;
    }

    public Date getCloseTime() 
    {
        return closeTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("periodCode", getPeriodCode())
            .append("periodName", getPeriodName())
            .append("startDate", getStartDate())
            .append("endDate", getEndDate())
            .append("status", getStatus())
            .append("closeUser", getCloseUser())
            .append("closeTime", getCloseTime())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
