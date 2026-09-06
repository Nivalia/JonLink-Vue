package com.jonlink.system.service;

import java.util.List;
import com.jonlink.system.domain.JonlinkChannelUser;

/**
 * 渠道/业务员档案Service接口
 *
 * @author jonlink
 * @date 2026-08-13
 */
public interface IJonlinkChannelUserService
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
     * 查询启用的渠道/业务员（下拉用，台账渠道类型=2 数据源）
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
     * 批量删除渠道/业务员
     */
    public int deleteJonlinkChannelUserByIds(Long[] ids);

    /**
     * 删除渠道/业务员信息
     */
    public int deleteJonlinkChannelUserById(Long id);
}
