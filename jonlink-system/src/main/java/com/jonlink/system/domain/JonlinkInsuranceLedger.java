package com.jonlink.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 保险台账对象 jonlink_insurance_ledger
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public class JonlinkInsuranceLedger extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 日期(自动=当日) */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date ledgerDate;

    /** 渠道来源 0自定义 1公众号粉丝 2系统业务员 3企业微信(预留) */
    @Excel(name = "渠道来源")
    private String channelType;

    /** 渠道关联ID(按type指向wx_mp_user.id/sys_user.user_id) */
    @Excel(name = "渠道ID")
    private Long channelRef;

    /** 渠道/业务员名称(带出) */
    @Excel(name = "渠道")
    private String channelName;

    /** 保单号(手动,可重复) */
    @Excel(name = "保单号")
    private String policyNo;

    /** 产品ID(选产品联动) */
    @Excel(name = "产品ID")
    private Long productId;

    /** 产品名称(带出) */
    @Excel(name = "产品名称")
    private String productName;

    /** 险别(产品带出) */
    @Excel(name = "险别")
    private String insuranceType;

    /** 保险公司(产品带出) */
    @Excel(name = "保险公司")
    private String insuranceCompany;

    /** 投保人(手动) */
    @Excel(name = "投保人")
    private String applicant;

    /** 被保人(手动) */
    @Excel(name = "被保人")
    private String insured;

    /** 保费(手动,¥) */
    @Excel(name = "保费")
    private BigDecimal premium;

    /** 是否含税 0否 1是(产品带出) */
    @Excel(name = "是否含税")
    private String taxFlag;

    /** 上游专属返利%(产品带出) */
    @Excel(name = "上游返利率")
    private BigDecimal upRate;

    /** 下游专属返利%(产品带出) */
    @Excel(name = "下游返利率")
    private BigDecimal downRate;

    /** 上游渠道(产品带出) */
    @Excel(name = "上游渠道")
    private String upChannel;

    /** 下游佣金 = 保费/1.06 × 下游政策%(含税) | 保费 × 下游政策%(不含税)(自动算) */
    @Excel(name = "下游佣金")
    private BigDecimal downCommission;

    /** 上游佣金 = 保费/1.06 × 上游政策%(含税) | 保费 × 上游政策%(不含税)(自动算) */
    @Excel(name = "上游佣金")
    private BigDecimal upCommission;

    /** 净费 = 保费 - 上游佣金(自动算, 始终用原始保费减) */
    @Excel(name = "净费")
    private BigDecimal netFee;

    /** 利润 = 上游佣金 - 下游佣金(自动算, 保费仅统计不入利润公式) */
    @Excel(name = "利润")
    private BigDecimal profit;

    /** 上游结费 0未结算 1已结算 */
    @Excel(name = "上游结费")
    private String upSettleStatus;

    /** 下游结费 0未结算 1已结算 */
    @Excel(name = "下游结费")
    private String downSettleStatus;

    /** 上游结算单号 */
    @Excel(name = "上游结算单")
    private String upSettleNo;

    /** 下游结算单号 */
    @Excel(name = "下游结算单")
    private String downSettleNo;

    /** 理赔备注 */
    @Excel(name = "理赔备注")
    private String claimRemark;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setLedgerDate(Date ledgerDate) 
    {
        this.ledgerDate = ledgerDate;
    }

    public Date getLedgerDate() 
    {
        return ledgerDate;
    }

    public void setChannelType(String channelType) 
    {
        this.channelType = channelType;
    }

    public String getChannelType() 
    {
        return channelType;
    }

    public void setChannelRef(Long channelRef) 
    {
        this.channelRef = channelRef;
    }

    public Long getChannelRef() 
    {
        return channelRef;
    }

    public void setChannelName(String channelName) 
    {
        this.channelName = channelName;
    }

    public String getChannelName() 
    {
        return channelName;
    }

    public void setPolicyNo(String policyNo) 
    {
        this.policyNo = policyNo;
    }

    public String getPolicyNo() 
    {
        return policyNo;
    }

    public void setProductId(Long productId) 
    {
        this.productId = productId;
    }

    public Long getProductId() 
    {
        return productId;
    }

    public void setProductName(String productName) 
    {
        this.productName = productName;
    }

    public String getProductName() 
    {
        return productName;
    }

    public void setInsuranceType(String insuranceType) 
    {
        this.insuranceType = insuranceType;
    }

    public String getInsuranceType() 
    {
        return insuranceType;
    }

    public void setInsuranceCompany(String insuranceCompany) 
    {
        this.insuranceCompany = insuranceCompany;
    }

    public String getInsuranceCompany() 
    {
        return insuranceCompany;
    }

    public void setApplicant(String applicant) 
    {
        this.applicant = applicant;
    }

    public String getApplicant() 
    {
        return applicant;
    }

    public void setInsured(String insured) 
    {
        this.insured = insured;
    }

    public String getInsured() 
    {
        return insured;
    }

    public void setPremium(BigDecimal premium) 
    {
        this.premium = premium;
    }

    public BigDecimal getPremium() 
    {
        return premium;
    }

    public void setTaxFlag(String taxFlag) 
    {
        this.taxFlag = taxFlag;
    }

    public String getTaxFlag() 
    {
        return taxFlag;
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

    public void setUpChannel(String upChannel) 
    {
        this.upChannel = upChannel;
    }

    public String getUpChannel() 
    {
        return upChannel;
    }

    public void setDownCommission(BigDecimal downCommission) 
    {
        this.downCommission = downCommission;
    }

    public BigDecimal getDownCommission() 
    {
        return downCommission;
    }

    public void setUpCommission(BigDecimal upCommission) 
    {
        this.upCommission = upCommission;
    }

    public BigDecimal getUpCommission() 
    {
        return upCommission;
    }

    public void setNetFee(BigDecimal netFee) 
    {
        this.netFee = netFee;
    }

    public BigDecimal getNetFee() 
    {
        return netFee;
    }

    public void setProfit(BigDecimal profit) 
    {
        this.profit = profit;
    }

    public BigDecimal getProfit() 
    {
        return profit;
    }

    public void setUpSettleStatus(String upSettleStatus) 
    {
        this.upSettleStatus = upSettleStatus;
    }

    public String getUpSettleStatus() 
    {
        return upSettleStatus;
    }

    public void setDownSettleStatus(String downSettleStatus) 
    {
        this.downSettleStatus = downSettleStatus;
    }

    public String getDownSettleStatus() 
    {
        return downSettleStatus;
    }

    public void setUpSettleNo(String upSettleNo) 
    {
        this.upSettleNo = upSettleNo;
    }

    public String getUpSettleNo() 
    {
        return upSettleNo;
    }

    public void setDownSettleNo(String downSettleNo) 
    {
        this.downSettleNo = downSettleNo;
    }

    public String getDownSettleNo() 
    {
        return downSettleNo;
    }

    public void setClaimRemark(String claimRemark) 
    {
        this.claimRemark = claimRemark;
    }

    public String getClaimRemark() 
    {
        return claimRemark;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("ledgerDate", getLedgerDate())
            .append("channelType", getChannelType())
            .append("channelRef", getChannelRef())
            .append("channelName", getChannelName())
            .append("policyNo", getPolicyNo())
            .append("productId", getProductId())
            .append("productName", getProductName())
            .append("insuranceType", getInsuranceType())
            .append("insuranceCompany", getInsuranceCompany())
            .append("applicant", getApplicant())
            .append("insured", getInsured())
            .append("premium", getPremium())
            .append("taxFlag", getTaxFlag())
            .append("upRate", getUpRate())
            .append("downRate", getDownRate())
            .append("upChannel", getUpChannel())
            .append("downCommission", getDownCommission())
            .append("upCommission", getUpCommission())
            .append("netFee", getNetFee())
            .append("profit", getProfit())
            .append("upSettleStatus", getUpSettleStatus())
            .append("downSettleStatus", getDownSettleStatus())
            .append("upSettleNo", getUpSettleNo())
            .append("downSettleNo", getDownSettleNo())
            .append("claimRemark", getClaimRemark())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
