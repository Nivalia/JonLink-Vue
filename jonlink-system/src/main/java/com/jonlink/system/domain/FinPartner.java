package com.jonlink.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 往来单位对象 fin_partner
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public class FinPartner extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 往来单位ID */
    private Long id;

    /** 1上游渠道商 2下游业务员 3客户 4其他 */
    @Excel(name = "类型")
    private String partnerType;

    /** 往来单位名称（唯一） */
    @Excel(name = "往来单位名称", readConverterExp = "唯=一")
    private String partnerName;

    /** 关联表（jonlink_channel / jonlink_channel_user） */
    @Excel(name = "关联表", readConverterExp = "j=onlink_channel,/=,j=onlink_channel_user")
    private String refTable;

    /** 关联表记录ID */
    @Excel(name = "关联ID")
    private Long refId;

    /** 电话 */
    @Excel(name = "电话")
    private String phone;

    /** 联系人 */
    @Excel(name = "联系人")
    private String contact;

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

    public void setPartnerType(String partnerType) 
    {
        this.partnerType = partnerType;
    }

    public String getPartnerType() 
    {
        return partnerType;
    }

    public void setPartnerName(String partnerName) 
    {
        this.partnerName = partnerName;
    }

    public String getPartnerName() 
    {
        return partnerName;
    }

    public void setRefTable(String refTable) 
    {
        this.refTable = refTable;
    }

    public String getRefTable() 
    {
        return refTable;
    }

    public void setRefId(Long refId) 
    {
        this.refId = refId;
    }

    public Long getRefId() 
    {
        return refId;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public void setContact(String contact) 
    {
        this.contact = contact;
    }

    public String getContact() 
    {
        return contact;
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
            .append("partnerType", getPartnerType())
            .append("partnerName", getPartnerName())
            .append("refTable", getRefTable())
            .append("refId", getRefId())
            .append("phone", getPhone())
            .append("contact", getContact())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
