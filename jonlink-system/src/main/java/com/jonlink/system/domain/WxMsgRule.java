package com.jonlink.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 推送规则对象 wx_msg_rule
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public class WxMsgRule extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 规则ID */
    private Long id;

    /** 规则编码（唯一，如 SETTLE_UP / SETTLE_DOWN） */
    @Excel(name = "规则编码", readConverterExp = "唯=一，如,S=ETTLE_UP,/=,S=ETTLE_DOWN")
    private String ruleCode;

    /** 规则名称（如：上游结算通知） */
    @Excel(name = "规则名称", readConverterExp = "如=：上游结算通知")
    private String ruleName;

    /** 业务事件类型（settle_up上游结算/settle_down下游结算/dist_commission分销佣金/order_verify订单核销） */
    @Excel(name = "业务事件类型", readConverterExp = "s=ettle_up上游结算/settle_down下游结算/dist_commission分销佣金/order_verify订单核销")
    private String bizType;

    /** 关联 wx_mp_template.id（微信后台模板） */
    @Excel(name = "关联 wx_mp_template.id", readConverterExp = "微=信后台模板")
    private Long templateId;

    /** 内容组装规则(JSON)：keyword占位符映射，如 {"policyNo":"{保单号}","amount":"{金额}"} */
    @Excel(name = "内容组装规则(JSON)")
    private String contentRule;

    /** 跳转地址模板(含{ticket}防伪占位)，可为空 */
    @Excel(name = "跳转地址模板(含{ticket}防伪占位)，可为空")
    private String urlRule;

    /** 受众规则(JSON)：如 {"source":"channel_phone"} 取台账渠道手机号；支持 fans_openid 直接推 */
    @Excel(name = "受众规则(JSON)")
    private String audienceRule;

    /** 是否启用（0停用 1启用） */
    @Excel(name = "是否启用", readConverterExp = "0=停用,1=启用")
    private String enabled;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sortOrder;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setRuleCode(String ruleCode) 
    {
        this.ruleCode = ruleCode;
    }

    public String getRuleCode() 
    {
        return ruleCode;
    }

    public void setRuleName(String ruleName) 
    {
        this.ruleName = ruleName;
    }

    public String getRuleName() 
    {
        return ruleName;
    }

    public void setBizType(String bizType) 
    {
        this.bizType = bizType;
    }

    public String getBizType() 
    {
        return bizType;
    }

    public void setTemplateId(Long templateId) 
    {
        this.templateId = templateId;
    }

    public Long getTemplateId() 
    {
        return templateId;
    }

    public void setContentRule(String contentRule) 
    {
        this.contentRule = contentRule;
    }

    public String getContentRule() 
    {
        return contentRule;
    }

    public void setUrlRule(String urlRule) 
    {
        this.urlRule = urlRule;
    }

    public String getUrlRule() 
    {
        return urlRule;
    }

    public void setAudienceRule(String audienceRule) 
    {
        this.audienceRule = audienceRule;
    }

    public String getAudienceRule() 
    {
        return audienceRule;
    }

    public void setEnabled(String enabled) 
    {
        this.enabled = enabled;
    }

    public String getEnabled() 
    {
        return enabled;
    }

    public void setSortOrder(Integer sortOrder) 
    {
        this.sortOrder = sortOrder;
    }

    public Integer getSortOrder() 
    {
        return sortOrder;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("ruleCode", getRuleCode())
            .append("ruleName", getRuleName())
            .append("bizType", getBizType())
            .append("templateId", getTemplateId())
            .append("contentRule", getContentRule())
            .append("urlRule", getUrlRule())
            .append("audienceRule", getAudienceRule())
            .append("enabled", getEnabled())
            .append("sortOrder", getSortOrder())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
