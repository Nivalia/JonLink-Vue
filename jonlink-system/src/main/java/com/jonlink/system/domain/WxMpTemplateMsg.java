package com.jonlink.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 发送记录对象 wx_mp_template_msg
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public class WxMpTemplateMsg extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 批次号(手工/导入) */
    @Excel(name = "批次号")
    private String batchNo;

    /** 业务流水号(幂等,去重) */
    @Excel(name = "业务流水号")
    private String transposeNo;

    /** 接收openid */
    @Excel(name = "openid")
    private String openid;

    /** 手机号(冗余冗余,便于查询) */
    @Excel(name = "手机号")
    private String phone;

    /** 模板ID */
    @Excel(name = "模板ID")
    private String templateId;

    /** 点击跳转链接 */
    @Excel(name = "跳转链接")
    private String url;

    /** 关键词值 JSON(按模板keyword_order顺序) */
    @Excel(name = "关键词值")
    private String keywords;

    /** 微信返回的msgid */
    @Excel(name = "msgid")
    private String msgId;

    /** 0待发 1成功 2失败 3超限待发(次日补) 4逾期放弃 5未找到粉丝 */
    @Excel(name = "状态")
    private String status;

    private String checkStatus;

    private String checkDiff;

    private Date checkTime;

    /** 错误信息(失败原因) */
    @Excel(name = "错误信息")
    private String errMsg;

    /** 0业务触发 1手工/导入 2合并汇总 */
    @Excel(name = "来源")
    private String source;

    /** 业务类型(ORDER_NOTIFY等) */
    @Excel(name = "业务类型")
    private String bizType;

    /** 业务单号 */
    @Excel(name = "业务单号")
    private String bizId;

    /** 批次内行号(定位失败行) */
    @Excel(name = "行号")
    private Long rowNo;

    /** 合并组ID(多条合并为1条时的关联) */
    @Excel(name = "合并组ID")
    private Long groupId;

    /** 实际发送时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发送时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sendTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setBatchNo(String batchNo) 
    {
        this.batchNo = batchNo;
    }

    public String getBatchNo() 
    {
        return batchNo;
    }

    public void setTransposeNo(String transposeNo) 
    {
        this.transposeNo = transposeNo;
    }

    public String getTransposeNo() 
    {
        return transposeNo;
    }

    public void setOpenid(String openid) 
    {
        this.openid = openid;
    }

    public String getOpenid() 
    {
        return openid;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public void setTemplateId(String templateId) 
    {
        this.templateId = templateId;
    }

    public String getTemplateId() 
    {
        return templateId;
    }

    public void setUrl(String url) 
    {
        this.url = url;
    }

    public String getUrl() 
    {
        return url;
    }

    public void setKeywords(String keywords) 
    {
        this.keywords = keywords;
    }

    public String getKeywords() 
    {
        return keywords;
    }

    public void setMsgId(String msgId) 
    {
        this.msgId = msgId;
    }

    public String getMsgId() 
    {
        return msgId;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public void setCheckStatus(String checkStatus)
    {
        this.checkStatus = checkStatus;
    }

    public String getCheckStatus()
    {
        return checkStatus;
    }

    public void setCheckDiff(String checkDiff)
    {
        this.checkDiff = checkDiff;
    }

    public String getCheckDiff()
    {
        return checkDiff;
    }

    public void setCheckTime(Date checkTime)
    {
        this.checkTime = checkTime;
    }

    public Date getCheckTime()
    {
        return checkTime;
    }

    public void setErrMsg(String errMsg) 
    {
        this.errMsg = errMsg;
    }

    public String getErrMsg() 
    {
        return errMsg;
    }

    public void setSource(String source) 
    {
        this.source = source;
    }

    public String getSource() 
    {
        return source;
    }

    public void setBizType(String bizType) 
    {
        this.bizType = bizType;
    }

    public String getBizType() 
    {
        return bizType;
    }

    public void setBizId(String bizId) 
    {
        this.bizId = bizId;
    }

    public String getBizId() 
    {
        return bizId;
    }

    public void setRowNo(Long rowNo) 
    {
        this.rowNo = rowNo;
    }

    public Long getRowNo() 
    {
        return rowNo;
    }

    public void setGroupId(Long groupId) 
    {
        this.groupId = groupId;
    }

    public Long getGroupId() 
    {
        return groupId;
    }

    public void setSendTime(Date sendTime) 
    {
        this.sendTime = sendTime;
    }

    public Date getSendTime() 
    {
        return sendTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("batchNo", getBatchNo())
            .append("transposeNo", getTransposeNo())
            .append("openid", getOpenid())
            .append("phone", getPhone())
            .append("templateId", getTemplateId())
            .append("url", getUrl())
            .append("keywords", getKeywords())
            .append("msgId", getMsgId())
            .append("status", getStatus())
            .append("errMsg", getErrMsg())
            .append("source", getSource())
            .append("bizType", getBizType())
            .append("bizId", getBizId())
            .append("rowNo", getRowNo())
            .append("groupId", getGroupId())
            .append("sendTime", getSendTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
