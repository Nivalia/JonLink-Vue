package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.WxDistFanMapping;

/**
 * 业务员↔粉丝映射Mapper接口
 */
public interface WxDistFanMappingMapper
{
    public WxDistFanMapping selectWxDistFanMappingById(Long id);
    public List<WxDistFanMapping> selectWxDistFanMappingList(WxDistFanMapping wxDistFanMapping);
    public WxDistFanMapping selectByDistAndOpenid(Long distUserId, String fanOpenid);
    public List<WxDistFanMapping> selectByDistUserId(Long distUserId);
    public List<WxDistFanMapping> selectByFanOpenid(String fanOpenid);
    public int insertWxDistFanMapping(WxDistFanMapping wxDistFanMapping);
    public int updateWxDistFanMapping(WxDistFanMapping wxDistFanMapping);
    public int deleteWxDistFanMappingById(Long id);
    public int deleteByDistAndOpenid(Long distUserId, String fanOpenid);
}
