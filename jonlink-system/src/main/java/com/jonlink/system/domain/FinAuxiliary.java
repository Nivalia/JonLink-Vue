package com.jonlink.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 辅助核算对象 fin_auxiliary
 * 
 * @author jonlink
 * @date 2026-08-23
 */
public class FinAuxiliary extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 辅助核算ID */
    private Long id;

    /** 辅助核算类型(dept/project/customer) */
    @Excel(name = "类型", readConverterExp = "dept=部门,project=项目,project=客户")
    private String auxType;

    /** 辅助核算编码 */
    @Excel(name = "辅助编码")
    private String auxCode;

    /** 辅助核算名称 */
    @Excel(name = "辅助名称")
    private String auxName;

    /** 状态(0正常 1停用) */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setAuxType(String auxType) 
    {
        this.auxType = auxType;
    }

    public String getAuxType() 
    {
        return auxType;
    }

    public void setAuxCode(String auxCode) 
    {
        this.auxCode = auxCode;
    }

    public String getAuxCode() 
    {
        return auxCode;
    }

    public void setAuxName(String auxName) 
    {
        this.auxName = auxName;
    }

    public String getAuxName() 
    {
        return auxName;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("auxType", getAuxType())
            .append("auxCode", getAuxCode())
            .append("auxName", getAuxName())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
