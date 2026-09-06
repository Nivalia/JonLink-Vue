package com.jonlink.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 凭证模板对象 fin_voucher_template
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public class FinVoucherTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 模板ID */
    private Long id;

    /** 模板编码（ledger_book / settle_up / settle_down / receipt / payment / expense / period_close_income / period_close_cost 等） */
    @Excel(name = "模板编码", readConverterExp = "l=edger_book,/=,s=ettle_up,/=,s=ettle_down,/=,r=eceipt,/=,p=ayment,/=,e=xpense,/=,p=eriod_close_income,/=,p=eriod_close_cost,等=")
    private String templateCode;

    /** 模板名称 */
    @Excel(name = "模板名称")
    private String templateName;

    /** 凭证摘要模板 */
    @Excel(name = "摘要模板")
    private String voucherSummary;

    /** 分录结构JSON（[{subject_code, summary, debit/credit, calc_formula}]） */
    @Excel(name = "分录", readConverterExp = "[={subject_code,,s=ummary,,d=ebit/credit,,c=alc_formula}]")
    private String entriesJson;

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

    public void setTemplateCode(String templateCode) 
    {
        this.templateCode = templateCode;
    }

    public String getTemplateCode() 
    {
        return templateCode;
    }

    public void setTemplateName(String templateName) 
    {
        this.templateName = templateName;
    }

    public String getTemplateName() 
    {
        return templateName;
    }

    public void setVoucherSummary(String voucherSummary) 
    {
        this.voucherSummary = voucherSummary;
    }

    public String getVoucherSummary() 
    {
        return voucherSummary;
    }

    public void setEntriesJson(String entriesJson) 
    {
        this.entriesJson = entriesJson;
    }

    public String getEntriesJson() 
    {
        return entriesJson;
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
            .append("templateCode", getTemplateCode())
            .append("templateName", getTemplateName())
            .append("voucherSummary", getVoucherSummary())
            .append("entriesJson", getEntriesJson())
            .append("remark", getRemark())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
