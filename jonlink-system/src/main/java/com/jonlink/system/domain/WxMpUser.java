package com.jonlink.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.jonlink.common.annotation.Excel;
import com.jonlink.common.core.domain.BaseEntity;

/**
 * 粉丝管理对象 wx_mp_user
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public class WxMpUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 微信openid(无感获取) */
    @Excel(name = "openid")
    private String openid;

    /** 手机号(授权回填/手动更换) */
    @Excel(name = "手机号")
    private String phone;

    /** 昵称 */
    @Excel(name = "昵称")
    private String nickname;

    /** 头像地址 */
    @Excel(name = "头像地址")
    private String avatar;

    /** 性别 0未知 1男 2女 */
    @Excel(name = "性别")
    private String sex;

    /** 国家 */
    @Excel(name = "国家")
    private String country;

    /** 省 */
    @Excel(name = "省")
    private String province;

    /** 市 */
    @Excel(name = "市")
    private String city;

    /** 关注状态 0否 1是 */
    @Excel(name = "订阅")
    private String subscribe;

    /** 关注时间(重复关注刷新) */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "关注时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date subscribeTime;

    /** 取关时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "取关时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date unsubscribeTime;

    /** 最近活跃时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最近活跃", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastActivityTime;

    /** 互动次数 */
    @Excel(name = "互动次数")
    private Long activityCount;

    /** 活跃度 1高 2中 3低(定时任务计算) */
    @Excel(name = "活跃度")
    private String activityLevel;

    /** 绑定系统用户ID(可空) */
    @Excel(name = "用户ID")
    private Long userId;

    /** 上级分销员user_id(扫码绑定) */
    @Excel(name = "上级分销员")
    private Long distributorId;

    /** 上级分销员名称(JOIN wx_dist_member 带出, 非表字段) */
    private String distributorName;

    /** 归属绑定时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "绑定时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date bindTime;

    /** 0扫码 1手动 2其它 */
    @Excel(name = "绑定来源")
    private String bindSource;

    /** 关注起始日(查询用,非表字段, 字符串 YYYY-MM-DD) */
    private String subscribeTimeBegin;

    /** 关注结束日(查询用,非表字段, 字符串 YYYY-MM-DD) */
    private String subscribeTimeEnd;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
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

    public void setNickname(String nickname) 
    {
        this.nickname = nickname;
    }

    public String getNickname() 
    {
        return nickname;
    }

    public void setAvatar(String avatar) 
    {
        this.avatar = avatar;
    }

    public String getAvatar() 
    {
        return avatar;
    }

    public void setSex(String sex) 
    {
        this.sex = sex;
    }

    public String getSex() 
    {
        return sex;
    }

    public void setCountry(String country) 
    {
        this.country = country;
    }

    public String getCountry() 
    {
        return country;
    }

    public void setProvince(String province) 
    {
        this.province = province;
    }

    public String getProvince() 
    {
        return province;
    }

    public void setCity(String city) 
    {
        this.city = city;
    }

    public String getCity() 
    {
        return city;
    }

    public void setSubscribe(String subscribe) 
    {
        this.subscribe = subscribe;
    }

    public String getSubscribe() 
    {
        return subscribe;
    }

    public void setSubscribeTime(Date subscribeTime) 
    {
        this.subscribeTime = subscribeTime;
    }

    public Date getSubscribeTime() 
    {
        return subscribeTime;
    }

    public void setUnsubscribeTime(Date unsubscribeTime) 
    {
        this.unsubscribeTime = unsubscribeTime;
    }

    public Date getUnsubscribeTime() 
    {
        return unsubscribeTime;
    }

    public void setLastActivityTime(Date lastActivityTime) 
    {
        this.lastActivityTime = lastActivityTime;
    }

    public Date getLastActivityTime() 
    {
        return lastActivityTime;
    }

    public void setActivityCount(Long activityCount) 
    {
        this.activityCount = activityCount;
    }

    public Long getActivityCount() 
    {
        return activityCount;
    }

    public void setActivityLevel(String activityLevel) 
    {
        this.activityLevel = activityLevel;
    }

    public String getActivityLevel() 
    {
        return activityLevel;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setDistributorId(Long distributorId) 
    {
        this.distributorId = distributorId;
    }

    public Long getDistributorId() 
    {
        return distributorId;
    }

    public void setDistributorName(String distributorName) 
    {
        this.distributorName = distributorName;
    }

    public String getDistributorName() 
    {
        return distributorName;
    }

    public void setBindTime(Date bindTime) 
    {
        this.bindTime = bindTime;
    }

    public Date getBindTime() 
    {
        return bindTime;
    }

    public void setBindSource(String bindSource)
    {
        this.bindSource = bindSource;
    }

    public String getBindSource()
    {
        return bindSource;
    }

    public void setSubscribeTimeBegin(String subscribeTimeBegin)
    {
        this.subscribeTimeBegin = subscribeTimeBegin;
    }

    public String getSubscribeTimeBegin()
    {
        return subscribeTimeBegin;
    }

    public void setSubscribeTimeEnd(String subscribeTimeEnd)
    {
        this.subscribeTimeEnd = subscribeTimeEnd;
    }

    public String getSubscribeTimeEnd()
    {
        return subscribeTimeEnd;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("openid", getOpenid())
            .append("phone", getPhone())
            .append("nickname", getNickname())
            .append("avatar", getAvatar())
            .append("sex", getSex())
            .append("country", getCountry())
            .append("province", getProvince())
            .append("city", getCity())
            .append("subscribe", getSubscribe())
            .append("subscribeTime", getSubscribeTime())
            .append("unsubscribeTime", getUnsubscribeTime())
            .append("lastActivityTime", getLastActivityTime())
            .append("activityCount", getActivityCount())
            .append("activityLevel", getActivityLevel())
            .append("userId", getUserId())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("distributorId", getDistributorId())
            .append("bindTime", getBindTime())
            .append("bindSource", getBindSource())
            .toString();
    }
}
