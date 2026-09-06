package com.jonlink.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;

import java.util.Date;

/**
 * 政策历史版本对象 policy_article_version
 *
 * @author jonlink
 */
public class PolicyArticleVersion
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 政策文章ID */
    @Excel(name = "文章ID")
    private Long articleId;

    /** 版本号 */
    @Excel(name = "版本号")
    private Integer versionNo;

    /** 政策标题 */
    @Excel(name = "政策标题")
    private String title;

    /** 图片JSON数组 */
    private String pics;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setArticleId(Long articleId)
    {
        this.articleId = articleId;
    }

    public Long getArticleId()
    {
        return articleId;
    }

    public void setVersionNo(Integer versionNo)
    {
        this.versionNo = versionNo;
    }

    public Integer getVersionNo()
    {
        return versionNo;
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

    public void setCreateBy(String createBy)
    {
        this.createBy = createBy;
    }

    public String getCreateBy()
    {
        return createBy;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("articleId", getArticleId())
            .append("versionNo", getVersionNo())
            .append("title", getTitle())
            .append("pics", getPics())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}