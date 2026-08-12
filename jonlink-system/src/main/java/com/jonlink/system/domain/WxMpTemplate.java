package com.jonlink.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 模板管理对象 wx_mp_template
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public class WxMpTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 微信模板ID(唯一) */
    @Excel(name = "微信模板ID(唯一)")
    private String templateId;

    /** 模板库短ID(创建时用) */
    @Excel(name = "模板库短ID(创建时用)")
    private String templateIdShort;

    /** 模板标题 */
    @Excel(name = "模板标题")
    private String title;

    /** 模板内容(含{{keywordN.DATA}}占位) */
    @Excel(name = "模板内容(含{{keywordN.DATA}}占位)")
    private String content;

    /** 关键词顺序(如 keyword1,keyword2..) */
    @Excel(name = "关键词顺序(如 keyword1,keyword2..)")
    private String keywordOrder;

    /** 主营行业 */
    @Excel(name = "主营行业")
    private String primaryIndustry;

    /** 副营行业 */
    @Excel(name = "副营行业")
    private String deputyIndustry;

    /** 模板示例 */
    @Excel(name = "模板示例")
    private String example;

    /** 状态 0停用 1启用(本地启停) */
    @Excel(name = "状态 0停用 1启用(本地启停)")
    private String status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setTemplateId(String templateId) 
    {
        this.templateId = templateId;
    }

    public String getTemplateId() 
    {
        return templateId;
    }

    public void setTemplateIdShort(String templateIdShort) 
    {
        this.templateIdShort = templateIdShort;
    }

    public String getTemplateIdShort() 
    {
        return templateIdShort;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setKeywordOrder(String keywordOrder) 
    {
        this.keywordOrder = keywordOrder;
    }

    public String getKeywordOrder() 
    {
        return keywordOrder;
    }

    public void setPrimaryIndustry(String primaryIndustry) 
    {
        this.primaryIndustry = primaryIndustry;
    }

    public String getPrimaryIndustry() 
    {
        return primaryIndustry;
    }

    public void setDeputyIndustry(String deputyIndustry) 
    {
        this.deputyIndustry = deputyIndustry;
    }

    public String getDeputyIndustry() 
    {
        return deputyIndustry;
    }

    public void setExample(String example) 
    {
        this.example = example;
    }

    public String getExample() 
    {
        return example;
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
            .append("templateId", getTemplateId())
            .append("templateIdShort", getTemplateIdShort())
            .append("title", getTitle())
            .append("content", getContent())
            .append("keywordOrder", getKeywordOrder())
            .append("primaryIndustry", getPrimaryIndustry())
            .append("deputyIndustry", getDeputyIndustry())
            .append("example", getExample())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
