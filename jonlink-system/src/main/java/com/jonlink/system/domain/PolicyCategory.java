package com.jonlink.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 政策分类对象 policy_category
 * 2 级树:1 级=保险公司, 2 级=分支机构
 *
 * @author jonlink
 * @date 2026-08-22
 */
public class PolicyCategory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 分类ID */
    private Long id;

    /** 父分类ID,0=1 级 */
    @Excel(name = "父分类ID")
    private Long parentId;

    /** 分类名称 */
    @Excel(name = "分类名称")
    private String categoryName;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sort;

    /** 0停用 1启用 */
    @Excel(name = "状态", readConverterExp = "0=停用,1=启用")
    private String status;

    /** 子分类(树查询时携带,非数据库字段) */
    private java.util.List<PolicyCategory> children;

    public void setId(Long id) { this.id = id; }
    public Long getId() { return id; }

    public void setParentId(Long parentId) { this.parentId = parentId; }
    public Long getParentId() { return parentId; }

    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getCategoryName() { return categoryName; }

    public void setSort(Integer sort) { this.sort = sort; }
    public Integer getSort() { return sort; }

    public void setStatus(String status) { this.status = status; }
    public String getStatus() { return status; }

    public void setChildren(java.util.List<PolicyCategory> children) { this.children = children; }
    public java.util.List<PolicyCategory> getChildren() { return children; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", id)
            .append("parentId", parentId)
            .append("categoryName", categoryName)
            .append("sort", sort)
            .append("status", status)
            .toString();
    }
}
