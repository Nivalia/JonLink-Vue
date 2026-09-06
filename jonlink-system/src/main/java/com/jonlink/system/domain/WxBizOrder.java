package com.jonlink.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 核销管理对象 wx_biz_order
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public class WxBizOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 订单号(唯一) */
    @Excel(name = "订单号")
    private String orderNo;

    /** 手机号(与粉丝表关联,索引) */
    @Excel(name = "手机号")
    private String phone;

    /** 客户名称 */
    @Excel(name = "客户名称")
    private String customerName;

    /** 服务/项目类型 */
    @Excel(name = "服务类型")
    private String serviceType;

    /** 车牌号(核销要素) */
    @Excel(name = "车牌号")
    private String carNo;

    /** 佣金/金额(核销要素) */
    @Excel(name = "佣金")
    private BigDecimal amount;

    /** 类型(字典) */
    @Excel(name = "类型")
    private String orderType;

    /** 数据来源 0手工 1Excel导入 2台账同步 */
    @Excel(name = "数据来源")
    private String sourceType;

    /** 结算状态 0未结算 1已结算 */
    @Excel(name = "结算状态")
    private String status;

    /** 核销状态(模板推送校验) 0未核销 1已核销 */
    @Excel(name = "核销状态")
    private String verifyStatus;

    /** 核销时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "核销时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date verifyTime;

    /** 核销结果(成功/失败原因) */
    @Excel(name = "核销结果")
    private String verifyMsg;

    /** 自定义扩展字段(JSON键值) */
    @Excel(name = "扩展字段")
    private String extJson;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setOrderNo(String orderNo) 
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo() 
    {
        return orderNo;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public void setCustomerName(String customerName) 
    {
        this.customerName = customerName;
    }

    public String getCustomerName() 
    {
        return customerName;
    }

    public void setServiceType(String serviceType) 
    {
        this.serviceType = serviceType;
    }

    public String getServiceType() 
    {
        return serviceType;
    }

    public void setCarNo(String carNo) 
    {
        this.carNo = carNo;
    }

    public String getCarNo() 
    {
        return carNo;
    }

    public void setAmount(BigDecimal amount) 
    {
        this.amount = amount;
    }

    public BigDecimal getAmount() 
    {
        return amount;
    }

    public void setOrderType(String orderType) 
    {
        this.orderType = orderType;
    }

    public String getOrderType()
    {
        return orderType;
    }

    public void setSourceType(String sourceType)
    {
        this.sourceType = sourceType;
    }

    public String getSourceType()
    {
        return sourceType;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setVerifyStatus(String verifyStatus) 
    {
        this.verifyStatus = verifyStatus;
    }

    public String getVerifyStatus() 
    {
        return verifyStatus;
    }

    public void setVerifyTime(Date verifyTime) 
    {
        this.verifyTime = verifyTime;
    }

    public Date getVerifyTime() 
    {
        return verifyTime;
    }

    public void setVerifyMsg(String verifyMsg) 
    {
        this.verifyMsg = verifyMsg;
    }

    public String getVerifyMsg() 
    {
        return verifyMsg;
    }

    public void setExtJson(String extJson) 
    {
        this.extJson = extJson;
    }

    public String getExtJson() 
    {
        return extJson;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orderNo", getOrderNo())
            .append("phone", getPhone())
            .append("customerName", getCustomerName())
            .append("serviceType", getServiceType())
            .append("carNo", getCarNo())
            .append("amount", getAmount())
            .append("orderType", getOrderType())
            .append("status", getStatus())
            .append("verifyStatus", getVerifyStatus())
            .append("verifyTime", getVerifyTime())
            .append("verifyMsg", getVerifyMsg())
            .append("extJson", getExtJson())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
