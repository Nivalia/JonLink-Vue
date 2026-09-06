package com.jonlink.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.TreeEntity;

/**
 * 会计科目对象 fin_subject
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public class FinSubject extends TreeEntity
{
    private static final long serialVersionUID = 1L;

    /** 科目ID */
    private Long id;

    /** 科目编码（如 1002） */
    @Excel(name = "科目编码", readConverterExp = "如=,1=002")
    private String subjectCode;

    /** 科目名称 */
    @Excel(name = "科目名称")
    private String subjectName;

    /** 1资产 2负债 3权益 4成本 5损益 */
    @Excel(name = "类型")
    private String subjectType;

    /** 余额方向 0借 1贷 */
    @Excel(name = "余额方向")
    private String balanceDirection;

    /** 0非末级 1末级 */
    @Excel(name = "末级")
    private String isLeaf;

    /** 0停用 1启用 */
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

    public void setSubjectCode(String subjectCode) 
    {
        this.subjectCode = subjectCode;
    }

    public String getSubjectCode() 
    {
        return subjectCode;
    }

    public void setSubjectName(String subjectName) 
    {
        this.subjectName = subjectName;
    }

    public String getSubjectName() 
    {
        return subjectName;
    }

    public void setSubjectType(String subjectType) 
    {
        this.subjectType = subjectType;
    }

    public String getSubjectType() 
    {
        return subjectType;
    }

    public void setBalanceDirection(String balanceDirection) 
    {
        this.balanceDirection = balanceDirection;
    }

    public String getBalanceDirection() 
    {
        return balanceDirection;
    }

    public void setIsLeaf(String isLeaf) 
    {
        this.isLeaf = isLeaf;
    }

    public String getIsLeaf() 
    {
        return isLeaf;
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
            .append("parentId", getParentId())
            .append("subjectCode", getSubjectCode())
            .append("subjectName", getSubjectName())
            .append("subjectType", getSubjectType())
            .append("balanceDirection", getBalanceDirection())
            .append("isLeaf", getIsLeaf())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
