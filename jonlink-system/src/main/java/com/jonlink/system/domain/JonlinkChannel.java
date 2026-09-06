package com.jonlink.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 上游渠道商对象 jonlink_channel
 *
 * @author jonlink
 * @date 2026-08-13
 */
public class JonlinkChannel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 渠道商名称 */
    @Excel(name = "渠道商名称")
    private String channelName;

    /** 联系人ID(jonlink_contact_person.id) */
    @Excel(name = "联系人ID")
    private Long contactId;

    /** 联系人 */
    @Excel(name = "联系人")
    private String contactPerson;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 微信号 */
    @Excel(name = "微信号")
    private String wechat;

    /** 结算周期 */
    @Excel(name = "结算周期")
    private String settleCycle;

    /** 状态 0停用 1启用 */
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

    public void setChannelName(String channelName)
    {
        this.channelName = channelName;
    }

    public String getChannelName()
    {
        return channelName;
    }

    public void setContactId(Long contactId)
    {
        this.contactId = contactId;
    }

    public Long getContactId()
    {
        return contactId;
    }

    public void setContactPerson(String contactPerson)
    {
        this.contactPerson = contactPerson;
    }

    public String getContactPerson()
    {
        return contactPerson;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setWechat(String wechat)
    {
        this.wechat = wechat;
    }

    public String getWechat()
    {
        return wechat;
    }

    public void setSettleCycle(String settleCycle)
    {
        this.settleCycle = settleCycle;
    }

    public String getSettleCycle()
    {
        return settleCycle;
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
            .append("channelName", getChannelName())
            .append("contactId", getContactId())
            .append("contactPerson", getContactPerson())
            .append("phone", getPhone())
            .append("wechat", getWechat())
            .append("settleCycle", getSettleCycle())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
