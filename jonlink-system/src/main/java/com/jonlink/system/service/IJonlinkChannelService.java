package com.jonlink.system.service;

import java.util.List;
import com.jonlink.system.domain.JonlinkChannel;

/**
 * 上游渠道商Service接口
 *
 * @author jonlink
 * @date 2026-08-13
 */
public interface IJonlinkChannelService
{
    /**
     * 查询上游渠道商
     */
    public JonlinkChannel selectJonlinkChannelById(Long id);

    /**
     * 查询上游渠道商列表
     */
    public List<JonlinkChannel> selectJonlinkChannelList(JonlinkChannel jonlinkChannel);

    /**
     * 查询启用的渠道商（下拉用）
     */
    public List<JonlinkChannel> selectJonlinkChannelOptions();

    /**
     * 新增上游渠道商
     */
    public int insertJonlinkChannel(JonlinkChannel jonlinkChannel);

    /**
     * 修改上游渠道商
     */
    public int updateJonlinkChannel(JonlinkChannel jonlinkChannel);

    /**
     * 批量删除上游渠道商
     */
    public int deleteJonlinkChannelByIds(Long[] ids);

    /**
     * 删除上游渠道商信息
     */
    public int deleteJonlinkChannelById(Long id);

    /**
     * 校验渠道商名称唯一
     */
    public boolean checkChannelNameUnique(JonlinkChannel jonlinkChannel);
}
