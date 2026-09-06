package com.jonlink.system.wx.service;

import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.common.utils.StringUtils;
import com.jonlink.system.domain.WxDistMember;
import com.jonlink.system.domain.WxDistFanMapping;
import com.jonlink.system.domain.WxMpUser;
import com.jonlink.system.mapper.WxDistFanMappingMapper;
import com.jonlink.system.mapper.WxDistMemberMapper;
import com.jonlink.system.mapper.WxBizMapper;
import com.jonlink.system.mapper.WxMpUserMapper;

/**
 * 业务员 ↔ 公众号粉丝数据同步服务（基于映射表）。
 *
 * 映射规则：
 *   1. 映射表 wx_dist_fan_mapping 持久化 dist_user_id ↔ fan_openid 关系
 *   2. 业务员有手机号 → 自动匹配粉丝 openid → 写入映射表
 *   3. 粉丝授权有手机号 → 反查业务员 → 写入映射表
 *   4. 映射表独立于 wx_mp_user，不受粉丝同步影响
 *
 * @author jonlink
 */
@Service
public class DistFanSyncService
{
    private static final Logger log = LoggerFactory.getLogger(DistFanSyncService.class);

    @Autowired
    private WxDistFanMappingMapper mappingMapper;
    @Autowired
    private WxDistMemberMapper distMemberMapper;
    @Autowired
    private WxBizMapper wxBizMapper;
    @Autowired
    private WxMpUserMapper wxMpUserMapper;

    /**
     * 从业务员侧同步：根据 user_id 找到手机号，匹配粉丝 openid，写入映射表。
     */
    public void syncFromDistMember(Long userId)
    {
        if (userId == null) return;
        WxDistMember dist = wxBizMapper.selectDistByUserId(userId);
        if (dist == null) return;

        String phone = getPhoneByUserId(userId);
        if (StringUtils.isEmpty(phone))
        {
            log.debug("[dist-fan-sync] 业务员 {} 无手机号, 跳过", userId);
            return;
        }

        // 通过手机号查找粉丝
        WxMpUser fan = findFanByPhone(phone);
        if (fan == null || StringUtils.isEmpty(fan.getOpenid()))
        {
            log.debug("[dist-fan-sync] 手机号 {} 无匹配粉丝", phone);
            return;
        }

        // 写入/更新映射表
        upsertMapping(userId, fan.getOpenid(), phone, "dist_sync");
        log.info("[dist-fan-sync] 业务员→粉丝: userId={}, phone={}, openid={}", userId, phone, fan.getOpenid());
    }

    /**
     * 从粉丝侧同步：根据 openid 找到手机号，反查业务员，写入映射表。
     */
    public void syncFromFan(String openid)
    {
        if (StringUtils.isEmpty(openid)) return;
        WxMpUser fan = findFanByOpenid(openid);
        if (fan == null) return;

        String phone = fan.getPhone();
        if (StringUtils.isEmpty(phone))
        {
            log.debug("[dist-fan-sync] 粉丝 {} 无手机号, 跳过", openid);
            return;
        }

        Long distUserId = getUserIdByPhone(phone);
        if (distUserId == null)
        {
            log.debug("[dist-fan-sync] 手机号 {} 无匹配业务员", phone);
            return;
        }

        // 写入/更新映射表
        upsertMapping(distUserId, openid, phone, "auto_oauth");
        log.info("[dist-fan-sync] 粉丝→业务员: openid={}, phone={}, userId={}", openid, phone, distUserId);
    }

    /**
     * 全量同步：遍历所有业务员，匹配粉丝并写入映射表。
     */
    public void syncAll()
    {
        log.info("[dist-fan-sync] 开始全量同步...");
        WxDistMember query = new WxDistMember();
        List<WxDistMember> allDist = distMemberMapper.selectWxDistMemberList(query);
        int count = 0;
        for (WxDistMember d : allDist)
        {
            syncFromDistMember(d.getUserId());
            count++;
        }
        log.info("[dist-fan-sync] 全量同步完成, 处理 {} 条", count);
    }

    /**
     * 查询业务员绑定的所有粉丝 openid。
     */
    public List<String> findOpenidsByDistUserId(Long distUserId)
    {
        List<WxDistFanMapping> mappings = mappingMapper.selectByDistUserId(distUserId);
        return mappings.stream().map(WxDistFanMapping::getFanOpenid).toList();
    }

    /**
     * 查询粉丝关联的所有业务员 user_id。
     */
    public List<Long> findDistUserIdsByOpenid(String openid)
    {
        List<WxDistFanMapping> mappings = mappingMapper.selectByFanOpenid(openid);
        return mappings.stream().map(WxDistFanMapping::getDistUserId).toList();
    }

    /**
     * 查找业务员的上级分销员 user_id。
     */
    public Long getParentUserId(Long userId)
    {
        if (userId == null) return null;
        WxDistMember dist = wxBizMapper.selectDistByUserId(userId);
        return dist != null ? dist.getParentId() : null;
    }

    /**
     * 通过手机号查找 user_id。
     */
    public Long getUserIdByPhone(String phone)
    {
        if (StringUtils.isEmpty(phone)) return null;
        try
        {
            com.jonlink.system.mapper.SysUserMapper sysUserMapper =
                com.jonlink.common.utils.spring.SpringUtils.getBean(com.jonlink.system.mapper.SysUserMapper.class);
            com.jonlink.common.core.domain.entity.SysUser user = new com.jonlink.common.core.domain.entity.SysUser();
            user.setPhonenumber(phone);
            List<com.jonlink.common.core.domain.entity.SysUser> users = sysUserMapper.selectUserList(user);
            return users.isEmpty() ? null : users.get(0).getUserId();
        }
        catch (Exception e)
        {
            log.warn("[dist-fan-sync] 查找手机号对应用户失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 通过 user_id 查找手机号。
     */
    public String getPhoneByUserId(Long userId)
    {
        if (userId == null) return null;
        try
        {
            com.jonlink.system.mapper.SysUserMapper sysUserMapper =
                com.jonlink.common.utils.spring.SpringUtils.getBean(com.jonlink.system.mapper.SysUserMapper.class);
            com.jonlink.common.core.domain.entity.SysUser user = sysUserMapper.selectUserById(userId);
            return user != null ? user.getPhonenumber() : null;
        }
        catch (Exception e)
        {
            log.warn("[dist-fan-sync] 查找用户手机号失败: {}", e.getMessage());
            return null;
        }
    }

    // ========== 内部方法 ==========

    private void upsertMapping(Long distUserId, String openid, String phone, String source)
    {
        WxDistFanMapping exist = mappingMapper.selectByDistAndOpenid(distUserId, openid);
        if (exist != null)
        {
            // 已存在，更新手机号
            exist.setFanPhone(phone);
            exist.setSyncSource(source);
            mappingMapper.updateWxDistFanMapping(exist);
        }
        else
        {
            // 新增映射
            WxDistFanMapping mapping = new WxDistFanMapping();
            mapping.setDistUserId(distUserId);
            mapping.setFanOpenid(openid);
            mapping.setFanPhone(phone);
            mapping.setSyncSource(source);
            mappingMapper.insertWxDistFanMapping(mapping);
        }
    }

    private WxMpUser findFanByPhone(String phone)
    {
        if (StringUtils.isEmpty(phone)) return null;
        WxMpUser q = new WxMpUser();
        q.setPhone(phone);
        List<WxMpUser> list = wxMpUserMapper.selectWxMpUserList(q);
        return list.isEmpty() ? null : list.get(0);
    }

    private WxMpUser findFanByOpenid(String openid)
    {
        if (StringUtils.isEmpty(openid)) return null;
        WxMpUser q = new WxMpUser();
        q.setOpenid(openid);
        List<WxMpUser> list = wxMpUserMapper.selectWxMpUserList(q);
        return list.isEmpty() ? null : list.get(0);
    }
}
