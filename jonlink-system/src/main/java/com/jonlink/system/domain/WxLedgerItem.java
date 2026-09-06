package com.jonlink.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 电子台账流水对象 wx_ledger_item
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public class WxLedgerItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 台账流水号(唯一) */
    @Excel(name = "流水号")
    private String ledgerNo;

    /** 类型 1核销 2分销积分 3模板推送 4扫码 5粉丝绑定 6手动调整 */
    @Excel(name = "类型")
    private String ledgerType;

    /** 关联业务单号(订单号/佣金单/批次号/scene) */
    @Excel(name = "关联单号")
    private String bizNo;

    /** 客户手机号 */
    @Excel(name = "手机号")
    private String phone;

    /** 客户openid */
    @Excel(name = "openid")
    private String openid;

    /** 流水金额(佣金等) */
    @Excel(name = "流水金额")
    private BigDecimal amount;

    /** 流水分值(积分入账=amount) */
    @Excel(name = "流水分值")
    private BigDecimal points;

    /** 方向 0流出 1流入 2中性(核销/推送) */
    @Excel(name = "方向")
    private String direction;

    /** 经办人(手动调整时; 自动=SYSTEM) */
    @Excel(name = "经办人")
    private String bizUser;

    /** 0作废 1有效 */
    @Excel(name = "状态")
    private String status;

    /** 业务发生时间(核销/发送/扫码时刻) */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "业务时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date occurredTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setLedgerNo(String ledgerNo) 
    {
        this.ledgerNo = ledgerNo;
    }

    public String getLedgerNo() 
    {
        return ledgerNo;
    }

    public void setLedgerType(String ledgerType) 
    {
        this.ledgerType = ledgerType;
    }

    public String getLedgerType() 
    {
        return ledgerType;
    }

    public void setBizNo(String bizNo) 
    {
        this.bizNo = bizNo;
    }

    public String getBizNo() 
    {
        return bizNo;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public void setOpenid(String openid) 
    {
        this.openid = openid;
    }

    public String getOpenid() 
    {
        return openid;
    }

    public void setAmount(BigDecimal amount) 
    {
        this.amount = amount;
    }

    public BigDecimal getAmount() 
    {
        return amount;
    }

    public void setPoints(BigDecimal points) 
    {
        this.points = points;
    }

    public BigDecimal getPoints() 
    {
        return points;
    }

    public void setDirection(String direction) 
    {
        this.direction = direction;
    }

    public String getDirection() 
    {
        return direction;
    }

    public void setBizUser(String bizUser) 
    {
        this.bizUser = bizUser;
    }

    public String getBizUser() 
    {
        return bizUser;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setOccurredTime(Date occurredTime) 
    {
        this.occurredTime = occurredTime;
    }

    public Date getOccurredTime() 
    {
        return occurredTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("ledgerNo", getLedgerNo())
            .append("ledgerType", getLedgerType())
            .append("bizNo", getBizNo())
            .append("phone", getPhone())
            .append("openid", getOpenid())
            .append("amount", getAmount())
            .append("points", getPoints())
            .append("direction", getDirection())
            .append("bizUser", getBizUser())
            .append("status", getStatus())
            .append("occurredTime", getOccurredTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
