package com.jonlink.system.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 发送批次对象 wx_mp_send_batch
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public class WxMpSendBatch extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 批次号(唯一) */
    @Excel(name = "批次号")
    private String batchNo;

    /** 使用的模板ID */
    @Excel(name = "模板ID")
    private String templateId;

    /** 总条数 */
    @Excel(name = "总条数")
    private Long total;

    /** 待发/超限待发 */
    @Excel(name = "待发")
    private Long pending;

    /** 成功(送达) */
    @Excel(name = "成功")
    private Long success;

    /** 失败 */
    @Excel(name = "失败")
    private Long fail;

    /** 逾期放弃 */
    @Excel(name = "逾期放弃")
    private Long overdue;

    /** 0处理中 1已完成 */
    @Excel(name = "状态")
    private String status;

    private Long checkPassed;
    private Long checkFailed;
    private Date checkTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setBatchNo(String batchNo) 
    {
        this.batchNo = batchNo;
    }

    public String getBatchNo() 
    {
        return batchNo;
    }

    public void setTemplateId(String templateId) 
    {
        this.templateId = templateId;
    }

    public String getTemplateId() 
    {
        return templateId;
    }

    public void setTotal(Long total) 
    {
        this.total = total;
    }

    public Long getTotal() 
    {
        return total;
    }

    public void setPending(Long pending) 
    {
        this.pending = pending;
    }

    public Long getPending() 
    {
        return pending;
    }

    public void setSuccess(Long success) 
    {
        this.success = success;
    }

    public Long getSuccess() 
    {
        return success;
    }

    public void setFail(Long fail) 
    {
        this.fail = fail;
    }

    public Long getFail() 
    {
        return fail;
    }

    public void setOverdue(Long overdue) 
    {
        this.overdue = overdue;
    }

    public Long getOverdue() 
    {
        return overdue;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public void setCheckPassed(Long checkPassed)
    {
        this.checkPassed = checkPassed;
    }

    public Long getCheckPassed()
    {
        return checkPassed;
    }

    public void setCheckFailed(Long checkFailed)
    {
        this.checkFailed = checkFailed;
    }

    public Long getCheckFailed()
    {
        return checkFailed;
    }

    public void setCheckTime(Date checkTime)
    {
        this.checkTime = checkTime;
    }

    public Date getCheckTime()
    {
        return checkTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("batchNo", getBatchNo())
            .append("templateId", getTemplateId())
            .append("total", getTotal())
            .append("pending", getPending())
            .append("success", getSuccess())
            .append("fail", getFail())
            .append("overdue", getOverdue())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
