package com.jonlink.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 账号配置对象 wx_mp_account
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public class WxMpAccount extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 公众号 AppID */
    @Excel(name = "公众号 AppID")
    private String appId;

    /** AppSecret(加密存储) */
    @Excel(name = "AppSecret(加密存储)")
    private String appSecret;

    /** 公众号名称 */
    @Excel(name = "公众号名称")
    private String name;

    /** 服务器配置Token(验签) */
    @Excel(name = "服务器配置Token(验签)")
    private String token;

    /** 消息加密密钥(可空) */
    @Excel(name = "消息加密密钥(可空)")
    private String encodingAesKey;

    /** 加密模式 0明文 1加密 */
    @Excel(name = "加密模式 0明文 1加密")
    private String encryptMode;

    /** 缓存的access_token */
    @Excel(name = "缓存的access_token")
    private String accessToken;

    /** access_token过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "access_token过期时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date tokenExpireTime;

    /** 状态 0停用 1启用 */
    @Excel(name = "状态 0停用 1启用")
    private String status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setAppId(String appId) 
    {
        this.appId = appId;
    }

    public String getAppId() 
    {
        return appId;
    }

    public void setAppSecret(String appSecret) 
    {
        this.appSecret = appSecret;
    }

    public String getAppSecret() 
    {
        return appSecret;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setToken(String token) 
    {
        this.token = token;
    }

    public String getToken() 
    {
        return token;
    }

    public void setEncodingAesKey(String encodingAesKey) 
    {
        this.encodingAesKey = encodingAesKey;
    }

    public String getEncodingAesKey() 
    {
        return encodingAesKey;
    }

    public void setEncryptMode(String encryptMode) 
    {
        this.encryptMode = encryptMode;
    }

    public String getEncryptMode() 
    {
        return encryptMode;
    }

    public void setAccessToken(String accessToken) 
    {
        this.accessToken = accessToken;
    }

    public String getAccessToken() 
    {
        return accessToken;
    }

    public void setTokenExpireTime(Date tokenExpireTime) 
    {
        this.tokenExpireTime = tokenExpireTime;
    }

    public Date getTokenExpireTime() 
    {
        return tokenExpireTime;
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
            .append("appId", getAppId())
            .append("appSecret", getAppSecret())
            .append("name", getName())
            .append("token", getToken())
            .append("encodingAesKey", getEncodingAesKey())
            .append("encryptMode", getEncryptMode())
            .append("accessToken", getAccessToken())
            .append("tokenExpireTime", getTokenExpireTime())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
