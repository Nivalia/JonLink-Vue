package com.jonlink.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 分销员档案对象 wx_dist_member
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public class WxDistMember extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 系统用户ID(唯一) */
    @Excel(name = "系统用户ID(唯一)")
    private Long userId;

    /** 姓名(冗余) */
    @Excel(name = "姓名(冗余)")
    private String userName;

    /** 上级分销员user_id(空=顶级) */
    @Excel(name = "上级分销员user_id(空=顶级)")
    private Long parentId;

    /** 链深度(顶级=1,展示用) */
    @Excel(name = "链深度(顶级=1,展示用)")
    private Long depth;

    /** 积分累计(佣金1:1) */
    @Excel(name = "积分累计(佣金1:1)")
    private BigDecimal accumPoints;

    /** 0停用 1启用 */
    @Excel(name = "0停用 1启用")
    private String status;

    /** 成为分销员时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "成为分销员时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date joinTime;

    /** 下级分销员(树形展示用, 非表字段) */
    private java.util.List<WxDistMember> children;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }

    public void setParentId(Long parentId) 
    {
        this.parentId = parentId;
    }

    public Long getParentId() 
    {
        return parentId;
    }

    public void setDepth(Long depth) 
    {
        this.depth = depth;
    }

    public Long getDepth() 
    {
        return depth;
    }

    public void setAccumPoints(BigDecimal accumPoints) 
    {
        this.accumPoints = accumPoints;
    }

    public BigDecimal getAccumPoints() 
    {
        return accumPoints;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setJoinTime(Date joinTime) 
    {
        this.joinTime = joinTime;
    }

    public Date getJoinTime() 
    {
        return joinTime;
    }

    public void setChildren(java.util.List<WxDistMember> children) 
    {
        this.children = children;
    }

    public java.util.List<WxDistMember> getChildren() 
    {
        return children;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("parentId", getParentId())
            .append("depth", getDepth())
            .append("accumPoints", getAccumPoints())
            .append("status", getStatus())
            .append("joinTime", getJoinTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
