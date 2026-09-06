package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.JonlinkChannel;

/**
 * 上游渠道商Mapper接口
 *
 * @author jonlink
 * @date 2026-08-13
 */
public interface JonlinkChannelMapper
{
    /**
     * 查询上游渠道商
     *
     * @param id 上游渠道商主键
     * @return 上游渠道商
     */
    public JonlinkChannel selectJonlinkChannelById(Long id);

    /**
     * 查询上游渠道商列表
     *
     * @param jonlinkChannel 上游渠道商
     * @return 上游渠道商集合
     */
    public List<JonlinkChannel> selectJonlinkChannelList(JonlinkChannel jonlinkChannel);

    /**
     * 查询启用的上游渠道商（下拉用，仅 id + channel_name）
     *
     * @return 渠道商简表
     */
    public List<JonlinkChannel> selectJonlinkChannelOptions();

    /**
     * 新增上游渠道商
     *
     * @param jonlinkChannel 上游渠道商
     * @return 结果
     */
    public int insertJonlinkChannel(JonlinkChannel jonlinkChannel);

    /**
     * 修改上游渠道商
     *
     * @param jonlinkChannel 上游渠道商
     * @return 结果
     */
    public int updateJonlinkChannel(JonlinkChannel jonlinkChannel);

    /**
     * 删除上游渠道商
     *
     * @param id 上游渠道商主键
     * @return 结果
     */
    public int deleteJonlinkChannelById(Long id);

    /**
     * 批量删除上游渠道商
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteJonlinkChannelByIds(Long[] ids);

    /**
     * 校验渠道商名称唯一
     *
     * @param jonlinkChannel 渠道商（id + channelName）
     * @return 行数（0/1）
     */
    public int checkChannelNameUnique(JonlinkChannel jonlinkChannel);
}
