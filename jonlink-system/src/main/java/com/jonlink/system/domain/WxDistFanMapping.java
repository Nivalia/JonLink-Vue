package com.jonlink.system.domain;

import com.jonlink.common.core.domain.BaseEntity;

/**
 * 业务员↔粉丝映射对象 wx_dist_fan_mapping
 *
 * @author jonlink
 */
public class WxDistFanMapping extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long distUserId;
    private String fanOpenid;
    private String fanPhone;
    private String syncSource;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDistUserId() { return distUserId; }
    public void setDistUserId(Long distUserId) { this.distUserId = distUserId; }

    public String getFanOpenid() { return fanOpenid; }
    public void setFanOpenid(String fanOpenid) { this.fanOpenid = fanOpenid; }

    public String getFanPhone() { return fanPhone; }
    public void setFanPhone(String fanPhone) { this.fanPhone = fanPhone; }

    public String getSyncSource() { return syncSource; }
    public void setSyncSource(String syncSource) { this.syncSource = syncSource; }
}
