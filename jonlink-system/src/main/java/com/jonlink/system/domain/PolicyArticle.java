package com.jonlink.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 政策文章对象 policy_article
 *
 * @author jonlink
 */
public class PolicyArticle extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 2级分类ID */
    @Excel(name = "分类ID")
    private Long categoryId;

    /** 分类名称（关联查询） */
    @Excel(name = "分类名称")
    private String categoryName;

    /** 政策标题 */
    @Excel(name = "政策标题")
    private String title;

    /** 图片JSON数组 */
    private String pics;

    /** 状态 0草稿 1已发布 */
    @Excel(name = "状态")
    private String status;

    /** 当前版本号 */
    @Excel(name = "版本号")
    private Integer versionNo;

    /** 浏览次数 */
    @Excel(name = "浏览次数")
    private Integer viewCount;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    public Long getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }

    public void setPics(String pics)
    {
        this.pics = pics;
    }

    public String getPics()
    {
        return pics;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public void setVersionNo(Integer versionNo)
    {
        this.versionNo = versionNo;
    }

    public Integer getVersionNo()
    {
        return versionNo;
    }

    public void setViewCount(Integer viewCount)
    {
        this.viewCount = viewCount;
    }

    public Integer getViewCount()
    {
        return viewCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("categoryId", getCategoryId())
            .append("categoryName", getCategoryName())
            .append("title", getTitle())
            .append("pics", getPics())
            .append("status", getStatus())
            .append("versionNo", getVersionNo())
            .append("viewCount", getViewCount())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}