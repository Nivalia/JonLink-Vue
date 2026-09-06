package com.jonlink.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 二维码管理对象 wx_qr_scene
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public class WxQrScene extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 微信永久二维码scene值(唯一) */
    @Excel(name = "scene")
    private Long sceneId;

    /** 分销员(系统用户ID) */
    @Excel(name = "分销员")
    private Long userId;

    /** 分销员姓名(冗余) */
    @Excel(name = "分销员姓名")
    private String userName;

    /** 0永久 1临时 */
    @Excel(name = "类型")
    private String qrType;

    /** 微信API返回ticket */
    @Excel(name = "ticket")
    private String ticket;

    /** 二维码图片地址(本地保存) */
    @Excel(name = "二维码地址")
    private String qrUrl;

    /** 0停用 1启用 */
    @Excel(name = "状态")
    private String status;

    /** 0分销(永久) 1活动 2通知 3公告 */
    @Excel(name = "类型")
    private String bizType;

    /** 临时码场景字符串(act_1001等) */
    @Excel(name = "临时场景")
    private String sceneStr;

    /** 过期时间(临时码30天内) */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "过期时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expireTime;

    /** 落地页地址(活动/公告详情) */
    @Excel(name = "落地页")
    private String landingUrl;

    /** 扫码次数(列表统计, 非表字段) */
    private Long scanCount;

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

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }

    public void setQrType(String qrType) 
    {
        this.qrType = qrType;
    }

    public String getQrType() 
    {
        return qrType;
    }

    public void setTicket(String ticket) 
    {
        this.ticket = ticket;
    }

    public String getTicket() 
    {
        return ticket;
    }

    public void setQrUrl(String qrUrl) 
    {
        this.qrUrl = qrUrl;
    }

    public String getQrUrl() 
    {
        return qrUrl;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
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

    public void setExpireTime(Date expireTime) 
    {
        this.expireTime = expireTime;
    }

    public Date getExpireTime() 
    {
        return expireTime;
    }

    public void setLandingUrl(String landingUrl) 
    {
        this.landingUrl = landingUrl;
    }

    public String getLandingUrl() 
    {
        return landingUrl;
    }
    public Long getScanCount()
    {
        return scanCount;
    }

    public void setScanCount(Long scanCount)
    {
        this.scanCount = scanCount;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("sceneId", getSceneId())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("qrType", getQrType())
            .append("ticket", getTicket())
            .append("qrUrl", getQrUrl())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("bizType", getBizType())
            .append("sceneStr", getSceneStr())
            .append("expireTime", getExpireTime())
            .append("landingUrl", getLandingUrl())
            .toString();
    }
}
