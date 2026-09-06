package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.JonlinkChannelUser;

/**
 * 渠道/业务员档案Mapper接口
 *
 * @author jonlink
 * @date 2026-08-13
 */
public interface JonlinkChannelUserMapper
{
    /**
     * 查询渠道/业务员
     */
    public JonlinkChannelUser selectJonlinkChannelUserById(Long id);

    /**
     * 查询渠道/业务员列表
     */
    public List<JonlinkChannelUser> selectJonlinkChannelUserList(JonlinkChannelUser jonlinkChannelUser);

    /**
     * 查询启用的渠道/业务员（下拉用，仅 id + user_name）
     * type=2 时台账下拉取此数据
     */
    public List<JonlinkChannelUser> selectJonlinkChannelUserOptions();

    /**
     * 新增渠道/业务员
     */
    public int insertJonlinkChannelUser(JonlinkChannelUser jonlinkChannelUser);

    /**
     * 修改渠道/业务员
     */
    public int updateJonlinkChannelUser(JonlinkChannelUser jonlinkChannelUser);

    /**
     * 删除渠道/业务员
     */
    public int deleteJonlinkChannelUserById(Long id);

    /**
     * 批量删除渠道/业务员
     */
    public int deleteJonlinkChannelUserByIds(Long[] ids);
}
