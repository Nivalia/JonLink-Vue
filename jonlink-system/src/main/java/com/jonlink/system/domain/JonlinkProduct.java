package com.jonlink.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 产品管理对象 jonlink_product
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public class JonlinkProduct extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String productName;

    /** 保险公司ID(引用) */
    @Excel(name = "保险公司ID(引用)")
    private Long companyId;

    /** 保险公司名称(带出冗余) */
    @Excel(name = "保险公司名称(带出冗余)")
    private String companyName;

    /** 负责人ID(引用联系人) */
    @Excel(name = "负责人ID(引用联系人)")
    private Long contactId;

    /** 负责人(冗余) */
    @Excel(name = "负责人(冗余)")
    private String contactName;

    /** 联系电话(带出) */
    @Excel(name = "联系电话(带出)")
    private String contactPhone;

    /** 险别ID(引用) */
    @Excel(name = "险别ID(引用)")
    private Long typeId;

    /** 险别名称(冗余) */
    @Excel(name = "险别名称(冗余)")
    private String typeName;

    /** 上游渠道(带出台账) */
    @Excel(name = "上游渠道(带出台账)")
    private String upChannel;

    /** 上游专属返利% */
    @Excel(name = "上游专属返利%")
    private BigDecimal upRate;

    /** 下游专属返利% */
    @Excel(name = "下游专属返利%")
    private BigDecimal downRate;

    /** 是否扣税 0否 1是(保费/1.06计算) */
    @Excel(name = "是否扣税 0否 1是(保费/1.06计算)")
    private String deductTax;

    /** 上下架 0下架 1上架 */
    @Excel(name = "上下架 0下架 1上架")
    private String shelfStatus;

    /** 排序 */
    @Excel(name = "排序")
    private Long sort;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setProductName(String productName) 
    {
        this.productName = productName;
    }

    public String getProductName() 
    {
        return productName;
    }

    public void setCompanyId(Long companyId) 
    {
        this.companyId = companyId;
    }

    public Long getCompanyId() 
    {
        return companyId;
    }

    public void setCompanyName(String companyName) 
    {
        this.companyName = companyName;
    }

    public String getCompanyName() 
    {
        return companyName;
    }

    public void setContactId(Long contactId) 
    {
        this.contactId = contactId;
    }

    public Long getContactId() 
    {
        return contactId;
    }

    public void setContactName(String contactName) 
    {
        this.contactName = contactName;
    }

    public String getContactName() 
    {
        return contactName;
    }

    public void setContactPhone(String contactPhone) 
    {
        this.contactPhone = contactPhone;
    }

    public String getContactPhone() 
    {
        return contactPhone;
    }

    public void setTypeId(Long typeId) 
    {
        this.typeId = typeId;
    }

    public Long getTypeId() 
    {
        return typeId;
    }

    public void setTypeName(String typeName) 
    {
        this.typeName = typeName;
    }

    public String getTypeName() 
    {
        return typeName;
    }

    public void setUpChannel(String upChannel) 
    {
        this.upChannel = upChannel;
    }

    public String getUpChannel() 
    {
        return upChannel;
    }

    public void setUpRate(BigDecimal upRate) 
    {
        this.upRate = upRate;
    }

    public BigDecimal getUpRate() 
    {
        return upRate;
    }

    public void setDownRate(BigDecimal downRate) 
    {
        this.downRate = downRate;
    }

    public BigDecimal getDownRate() 
    {
        return downRate;
    }

    public void setDeductTax(String deductTax) 
    {
        this.deductTax = deductTax;
    }

    public String getDeductTax() 
    {
        return deductTax;
    }

    public void setShelfStatus(String shelfStatus) 
    {
        this.shelfStatus = shelfStatus;
    }

    public String getShelfStatus() 
    {
        return shelfStatus;
    }

    public void setSort(Long sort) 
    {
        this.sort = sort;
    }

    public Long getSort() 
    {
        return sort;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("productName", getProductName())
            .append("companyId", getCompanyId())
            .append("companyName", getCompanyName())
            .append("contactId", getContactId())
            .append("contactName", getContactName())
            .append("contactPhone", getContactPhone())
            .append("typeId", getTypeId())
            .append("typeName", getTypeName())
            .append("upChannel", getUpChannel())
            .append("upRate", getUpRate())
            .append("downRate", getDownRate())
            .append("deductTax", getDeductTax())
            .append("shelfStatus", getShelfStatus())
            .append("sort", getSort())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
