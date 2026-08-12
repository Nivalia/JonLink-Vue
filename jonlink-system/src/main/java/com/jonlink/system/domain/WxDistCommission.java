package com.jonlink.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 佣金积分对象 wx_dist_commission
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public class WxDistCommission extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 订单号(核销关联,唯一幂等) */
    @Excel(name = "订单号(核销关联,唯一幂等)")
    private String orderNo;

    /** 下单客户手机号 */
    @Excel(name = "下单客户手机号")
    private String customerPhone;

    /** 下单客户openid */
    @Excel(name = "下单客户openid")
    private String customerOpenid;

    /** 受益分销员(直接上级)user_id */
    @Excel(name = "受益分销员(直接上级)user_id")
    private Long beneficiaryId;

    /** 受益分销员姓名 */
    @Excel(name = "受益分销员姓名")
    private String beneficiaryName;

    /** 该单佣金(核销金额) */
    @Excel(name = "该单佣金(核销金额)")
    private BigDecimal commissionAmt;

    /** 分润比例(默认100%=直接上级独享) */
    @Excel(name = "分润比例(默认100%=直接上级独享)")
    private BigDecimal ratio;

    /** 记入积分(=佣金*比例) */
    @Excel(name = "记入积分(=佣金*比例)")
    private BigDecimal points;

    /** 0待入账 1已入账 */
    @Excel(name = "0待入账 1已入账")
    private String status;

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

    public void setCustomerPhone(String customerPhone) 
    {
        this.customerPhone = customerPhone;
    }

    public String getCustomerPhone() 
    {
        return customerPhone;
    }

    public void setCustomerOpenid(String customerOpenid) 
    {
        this.customerOpenid = customerOpenid;
    }

    public String getCustomerOpenid() 
    {
        return customerOpenid;
    }

    public void setBeneficiaryId(Long beneficiaryId) 
    {
        this.beneficiaryId = beneficiaryId;
    }

    public Long getBeneficiaryId() 
    {
        return beneficiaryId;
    }

    public void setBeneficiaryName(String beneficiaryName) 
    {
        this.beneficiaryName = beneficiaryName;
    }

    public String getBeneficiaryName() 
    {
        return beneficiaryName;
    }

    public void setCommissionAmt(BigDecimal commissionAmt) 
    {
        this.commissionAmt = commissionAmt;
    }

    public BigDecimal getCommissionAmt() 
    {
        return commissionAmt;
    }

    public void setRatio(BigDecimal ratio) 
    {
        this.ratio = ratio;
    }

    public BigDecimal getRatio() 
    {
        return ratio;
    }

    public void setPoints(BigDecimal points) 
    {
        this.points = points;
    }

    public BigDecimal getPoints() 
    {
        return points;
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
            .append("orderNo", getOrderNo())
            .append("customerPhone", getCustomerPhone())
            .append("customerOpenid", getCustomerOpenid())
            .append("beneficiaryId", getBeneficiaryId())
            .append("beneficiaryName", getBeneficiaryName())
            .append("commissionAmt", getCommissionAmt())
            .append("ratio", getRatio())
            .append("points", getPoints())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
