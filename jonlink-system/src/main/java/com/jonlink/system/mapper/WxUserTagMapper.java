package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.WxUserTag;

/**
 * 粉丝标签关联Mapper接口
 * 
 * @author jonlink
 * @date 2026-09-05
 */
public interface WxUserTagMapper 
{
    /**
     * 查询粉丝标签关联
     */
    public WxUserTag selectWxUserTagById(Long id);

    /**
     * 查询粉丝标签关联列表
     */
    public List<WxUserTag> selectWxUserTagList(WxUserTag wxUserTag);

    /**
     * 查询粉丝的所有标签
     */
    public List<WxUserTag> selectTagsByUserId(Long userId);

    /**
     * 新增粉丝标签关联
     */
    public int insertWxUserTag(WxUserTag wxUserTag);

    /**
     * 修改粉丝标签关联
     */
    public int updateWxUserTag(WxUserTag wxUserTag);

    /**
     * 删除粉丝标签关联
     */
    public int deleteWxUserTagById(Long id);

    /**
     * 批量删除粉丝标签关联
     */
    public int deleteWxUserTagByIds(Long[] ids);

    /**
     * 删除粉丝的所有标签
     */
    public int deleteTagsByUserId(Long userId);

    /**
     * 批量添加粉丝标签
     */
    public int batchInsertUserTags(List<WxUserTag> list);
}
