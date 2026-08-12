package com.jonlink.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 公众号回调日志对象 wx_mp_callback_log
 * 
 * @author jonlink
 * @date 2026-08-09
 */
public class WxMpCallbackLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 消息类型(text/event/image...) */
    @Excel(name = "消息类型")
    private String msgType;

    /** 事件类型(subscribe/unsubscribe/SCAN/TEMPLATESENDJOBFINISH) */
    @Excel(name = "事件类型")
    private String event;

    /** 发送方openid */
    @Excel(name = "发送方openid")
    private String fromUser;

    /** 接收方(公众号原始ID) */
    @Excel(name = "接收方")
    private String toUser;

    /** 微信消息ID */
    @Excel(name = "消息ID")
    private String msgId;

    /** 二维码场景值 */
    @Excel(name = "场景值")
    private String scene;

    /** 事件Key */
    @Excel(name = "事件Key")
    private String eventKey;

    /** 关键载荷摘要 */
    @Excel(name = "载荷摘要")
    private String payload;

    /** 原始XML报文 */
    private String rawXml;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setMsgType(String msgType) 
    {
        this.msgType = msgType;
    }

    public String getMsgType() 
    {
        return msgType;
    }

    public void setEvent(String event) 
    {
        this.event = event;
    }

    public String getEvent() 
    {
        return event;
    }

    public void setFromUser(String fromUser) 
    {
        this.fromUser = fromUser;
    }

    public String getFromUser() 
    {
        return fromUser;
    }

    public void setToUser(String toUser) 
    {
        this.toUser = toUser;
    }

    public String getToUser() 
    {
        return toUser;
    }

    public void setMsgId(String msgId) 
    {
        this.msgId = msgId;
    }

    public String getMsgId() 
    {
        return msgId;
    }

    public void setScene(String scene) 
    {
        this.scene = scene;
    }

    public String getScene() 
    {
        return scene;
    }

    public void setEventKey(String eventKey) 
    {
        this.eventKey = eventKey;
    }

    public String getEventKey() 
    {
        return eventKey;
    }

    public void setPayload(String payload) 
    {
        this.payload = payload;
    }

    public String getPayload() 
    {
        return payload;
    }

    public void setRawXml(String rawXml) 
    {
        this.rawXml = rawXml;
    }

    public String getRawXml() 
    {
        return rawXml;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("msgType", getMsgType())
            .append("event", getEvent())
            .append("fromUser", getFromUser())
            .append("toUser", getToUser())
            .append("msgId", getMsgId())
            .append("scene", getScene())
            .append("eventKey", getEventKey())
            .append("payload", getPayload())
            .append("createTime", getCreateTime())
            .toString();
    }
}
