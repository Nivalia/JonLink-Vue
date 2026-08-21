package com.jonlink.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 政策历史版本对象 policy_article_version
 *
 * @author jonlink
 * @date 2026-08-22
 */
public class PolicyArticleVersion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 政策文章 ID */
    @Excel(name = "文章ID")
    private Long articleId;

    /** 版本号 */
    @Excel(name = "版本号")
    private Integer versionNo;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 图片 JSON 数组 */
    private String pics;

    public void setId(Long id) { this.id = id; }
    public Long getId() { return id; }

    public void setArticleId(Long articleId) { this.articleId = articleId; }
    public Long getArticleId() { return articleId; }

    public void setVersionNo(Integer versionNo) { this.versionNo = versionNo; }
    public Integer getVersionNo() { return versionNo; }

    public void setTitle(String title) { this.title = title; }
    public String getTitle() { return title; }

    public void setPics(String pics) { this.pics = pics; }
    public String getPics() { return pics; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", id)
            .append("articleId", articleId)
            .append("versionNo", versionNo)
            .append("title", title)
            .toString();
    }
}
