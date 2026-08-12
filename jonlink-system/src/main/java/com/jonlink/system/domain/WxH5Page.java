package com.jonlink.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * H5页面对象 wx_h5_page
 * 
 * @author jonlink
 * @date 2026-08-09
 */
public class WxH5Page extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 页面编码 */
    @Excel(name = "页面编码")
    private String pageCode;

    /** 页面名称 */
    @Excel(name = "页面名称")
    private String pageName;

    /** 访问路径(相对/根) */
    @Excel(name = "访问路径")
    private String pagePath;

    /** 页面说明 */
    @Excel(name = "页面说明")
    private String pageDesc;

    /** 状态 0停用 1启用 */
    @Excel(name = "状态")
    private String status;

    /** 排序 */
    @Excel(name = "排序")
    private Long sortOrder;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setPageCode(String pageCode) 
    {
        this.pageCode = pageCode;
    }

    public String getPageCode() 
    {
        return pageCode;
    }

    public void setPageName(String pageName) 
    {
        this.pageName = pageName;
    }

    public String getPageName() 
    {
        return pageName;
    }

    public void setPagePath(String pagePath) 
    {
        this.pagePath = pagePath;
    }

    public String getPagePath() 
    {
        return pagePath;
    }

    public void setPageDesc(String pageDesc) 
    {
        this.pageDesc = pageDesc;
    }

    public String getPageDesc() 
    {
        return pageDesc;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setSortOrder(Long sortOrder) 
    {
        this.sortOrder = sortOrder;
    }

    public Long getSortOrder() 
    {
        return sortOrder;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("pageCode", getPageCode())
            .append("pageName", getPageName())
            .append("pagePath", getPagePath())
            .append("pageDesc", getPageDesc())
            .append("status", getStatus())
            .append("sortOrder", getSortOrder())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
