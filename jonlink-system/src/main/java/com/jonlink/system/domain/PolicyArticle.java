package com.jonlink.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 政策文章对象 policy_article
 * 每个 2 级分类一条当前记录,修改时旧版本归档到 policy_article_version
 *
 * @author jonlink
 * @date 2026-08-22
 */
public class PolicyArticle extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 文章ID */
    private Long id;

    /** 2 级分类ID */
    @Excel(name = "分类ID")
    private Long categoryId;

    /** 政策标题 */
    @Excel(name = "政策标题")
    private String title;

    /** 图片 JSON 数组(URL 列表) */
    private String pics;

    /** 0草稿 1已发布 */
    @Excel(name = "状态", readConverterExp = "0=草稿,1=已发布")
    private String status;

    /** 当前版本号 */
    @Excel(name = "版本号")
    private Integer versionNo;

    /** 浏览次数 */
    @Excel(name = "浏览次数")
    private Integer viewCount;

    /** 关联分类名(展示用) */
    private String categoryName;

    public void setId(Long id) { this.id = id; }
    public Long getId() { return id; }

    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public Long getCategoryId() { return categoryId; }

    public void setTitle(String title) { this.title = title; }
    public String getTitle() { return title; }

    public void setPics(String pics) { this.pics = pics; }
    public String getPics() { return pics; }

    public void setStatus(String status) { this.status = status; }
    public String getStatus() { return status; }

    public void setVersionNo(Integer versionNo) { this.versionNo = versionNo; }
    public Integer getVersionNo() { return versionNo; }

    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }
    public Integer getViewCount() { return viewCount; }

    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getCategoryName() { return categoryName; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", id)
            .append("categoryId", categoryId)
            .append("title", title)
            .append("status", status)
            .append("versionNo", versionNo)
            .append("viewCount", viewCount)
            .toString();
    }
}
