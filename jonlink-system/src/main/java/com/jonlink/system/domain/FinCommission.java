package com.jonlink.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 佣金结算对象 fin_commission
 *
 * @author jonlink
 */
public class FinCommission extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 保单号 */
    @Excel(name = "保单号")
    private String policyNo;

    /** 保险公司ID */
    @Excel(name = "保险公司ID")
    private Long companyId;

    /** 保险公司名称 */
    @Excel(name = "保险公司")
    private String companyName;

    /** 保单保费 */
    @Excel(name = "保单保费")
    private BigDecimal premium;

    /** 是否含税 0否 1是(保费/1.06计算, v4规则) */
    @Excel(name = "是否含税")
    private String taxFlag;

    /** 佣金比例(%) */
    @Excel(name = "佣金比例")
    private BigDecimal commissionRate;

    /** 佣金金额 = 保费/1.06×佣金比例%(含税) | 保费×佣金比例%(不含税) */
    @Excel(name = "佣金金额")
    private BigDecimal commissionAmount;

    /** 方向:0=上游(应收) 1=下游(应付) */
    @Excel(name = "方向")
    private String direction;

    /** 结算状态:0=待结算 1=已确认 2=已支付 3=已对账 */
    @Excel(name = "结算状态")
    private String status;

    /** 保单日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "保单日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date policyDate;

    /** 结算日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结算日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date settleDate;

    /** 关联收款单ID */
    private Long receiptId;

    /** 关联付款单ID */
    private Long paymentId;

    /** 保单录入人员 */
    @Excel(name = "保单录入人员")
    private String policyUser;

    public void setId(Long id) { this.id = id; }
    public Long getId() { return id; }

    public void setPolicyNo(String policyNo) { this.policyNo = policyNo; }
    public String getPolicyNo() { return policyNo; }

    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getCompanyId() { return companyId; }

    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getCompanyName() { return companyName; }

    public void setPremium(BigDecimal premium) { this.premium = premium; }
    public BigDecimal getPremium() { return premium; }

    public void setTaxFlag(String taxFlag) { this.taxFlag = taxFlag; }
    public String getTaxFlag() { return taxFlag; }

    public void setCommissionRate(BigDecimal commissionRate) { this.commissionRate = commissionRate; }
    public BigDecimal getCommissionRate() { return commissionRate; }

    public void setCommissionAmount(BigDecimal commissionAmount) { this.commissionAmount = commissionAmount; }
    public BigDecimal getCommissionAmount() { return commissionAmount; }

    public void setDirection(String direction) { this.direction = direction; }
    public String getDirection() { return direction; }

    public void setStatus(String status) { this.status = status; }
    public String getStatus() { return status; }

    public void setPolicyDate(Date policyDate) { this.policyDate = policyDate; }
    public Date getPolicyDate() { return policyDate; }

    public void setSettleDate(Date settleDate) { this.settleDate = settleDate; }
    public Date getSettleDate() { return settleDate; }

    public void setReceiptId(Long receiptId) { this.receiptId = receiptId; }
    public Long getReceiptId() { return receiptId; }

    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }
    public Long getPaymentId() { return paymentId; }

    public void setPolicyUser(String policyUser) { this.policyUser = policyUser; }
    public String getPolicyUser() { return policyUser; }
}
