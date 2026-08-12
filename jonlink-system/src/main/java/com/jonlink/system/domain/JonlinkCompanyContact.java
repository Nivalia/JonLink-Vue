package com.jonlink.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 公司联系人对象 jonlink_company_contact
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public class JonlinkCompanyContact extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 公司ID */
    @Excel(name = "公司ID")
    private Long companyId;

    /** 联系人ID */
    @Excel(name = "联系人ID")
    private Long contactId;

    /** 是否主联系人 0否 1是 */
    @Excel(name = "是否主联系人 0否 1是")
    private String isPrimary;

    /** 关系状态 0停用 1启用 */
    @Excel(name = "关系状态 0停用 1启用")
    private String status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setCompanyId(Long companyId) 
    {
        this.companyId = companyId;
    }

    public Long getCompanyId() 
    {
        return companyId;
    }

    public void setContactId(Long contactId) 
    {
        this.contactId = contactId;
    }

    public Long getContactId() 
    {
        return contactId;
    }

    public void setIsPrimary(String isPrimary) 
    {
        this.isPrimary = isPrimary;
    }

    public String getIsPrimary() 
    {
        return isPrimary;
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
            .append("companyId", getCompanyId())
            .append("contactId", getContactId())
            .append("isPrimary", getIsPrimary())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .toString();
    }
}
