package com.jonlink.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 核销表单字段配置 wx_fc_config
 *
 * @author jonlink
 */
public class WxFcConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 字段名(英文/驼峰,程序内识别) */
    @Excel(name = "字段名")
    private String fieldKey;

    /** 显示标签(中文,渲染用) */
    @Excel(name = "显示标签")
    private String fieldLabel;

    /** 字段类型 0文本 1数字 2日期 3下拉 */
    @Excel(name = "字段类型")
    private String fieldType;

    /** 业务分类 0核销 1下单 */
    @Excel(name = "业务分类")
    private String sourceType;

    /** 数据源(core=内置字段/custom=自定义JSON) */
    @Excel(name = "数据源")
    private String dataSource;

    /** 是否可见 0否 1是 */
    @Excel(name = "是否可见")
    private String visible;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sortNo;

    /** 是否必填 0否 1是 */
    @Excel(name = "是否必填")
    private String required;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFieldKey() { return fieldKey; }
    public void setFieldKey(String fieldKey) { this.fieldKey = fieldKey; }

    public String getFieldLabel() { return fieldLabel; }
    public void setFieldLabel(String fieldLabel) { this.fieldLabel = fieldLabel; }

    public String getFieldType() { return fieldType; }
    public void setFieldType(String fieldType) { this.fieldType = fieldType; }

    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }

    public String getDataSource() { return dataSource; }
    public void setDataSource(String dataSource) { this.dataSource = dataSource; }

    public String getVisible() { return visible; }
    public void setVisible(String visible) { this.visible = visible; }

    public Integer getSortNo() { return sortNo; }
    public void setSortNo(Integer sortNo) { this.sortNo = sortNo; }

    public String getRequired() { return required; }
    public void setRequired(String required) { this.required = required; }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", id)
                .append("fieldKey", fieldKey)
                .append("fieldLabel", fieldLabel)
                .append("fieldType", fieldType)
                .append("sourceType", sourceType)
                .append("dataSource", dataSource)
                .append("visible", visible)
                .append("sortNo", sortNo)
                .append("required", required)
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
