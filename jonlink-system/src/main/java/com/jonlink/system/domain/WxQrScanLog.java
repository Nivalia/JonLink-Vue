package com.jonlink.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 扫码日志对象 wx_qr_scan_log
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public class WxQrScanLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** scene值 */
    @Excel(name = "scene")
    private Long sceneId;

    /** 0分销 1活动 2通知 3公告 */
    @Excel(name = "类型")
    private String bizType;

    /** 场景串(临时码) */
    @Excel(name = "场景串")
    private String sceneStr;

    /** 扫码粉丝openid */
    @Excel(name = "openid")
    private String openid;

    /** 是否本次扫码新关注 0否 1是 */
    @Excel(name = "是否新关注")
    private String isNewFollow;

    /** 扫码时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "扫码时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date scanTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setSceneId(Long sceneId) 
    {
        this.sceneId = sceneId;
    }

    public Long getSceneId() 
    {
        return sceneId;
    }

    public void setBizType(String bizType) 
    {
        this.bizType = bizType;
    }

    public String getBizType() 
    {
        return bizType;
    }

    public void setSceneStr(String sceneStr) 
    {
        this.sceneStr = sceneStr;
    }

    public String getSceneStr() 
    {
        return sceneStr;
    }

    public void setOpenid(String openid) 
    {
        this.openid = openid;
    }

    public String getOpenid() 
    {
        return openid;
    }

    public void setIsNewFollow(String isNewFollow) 
    {
        this.isNewFollow = isNewFollow;
    }

    public String getIsNewFollow() 
    {
        return isNewFollow;
    }

    public void setScanTime(Date scanTime) 
    {
        this.scanTime = scanTime;
    }

    public Date getScanTime() 
    {
        return scanTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("sceneId", getSceneId())
            .append("bizType", getBizType())
            .append("sceneStr", getSceneStr())
            .append("openid", getOpenid())
            .append("isNewFollow", getIsNewFollow())
            .append("scanTime", getScanTime())
            .append("remark", getRemark())
            .toString();
    }
}
