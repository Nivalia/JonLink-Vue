package com.jonlink.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jonlink.system.domain.WxUserTag;
import com.jonlink.system.mapper.WxUserTagMapper;
import com.jonlink.system.service.IWxUserTagService;

/**
 * 粉丝标签关联Service业务层处理
 * 
 * @author jonlink
 * @date 2026-09-05
 */
@Service
public class WxUserTagServiceImpl implements IWxUserTagService 
{
    @Autowired
    private WxUserTagMapper wxUserTagMapper;

    /**
     * 查询粉丝标签关联
     */
    @Override
    public WxUserTag selectWxUserTagById(Long id)
    {
        return wxUserTagMapper.selectWxUserTagById(id);
    }

    /**
     * 查询粉丝标签关联列表
     */
    @Override
    public List<WxUserTag> selectWxUserTagList(WxUserTag wxUserTag)
    {
        return wxUserTagMapper.selectWxUserTagList(wxUserTag);
    }

    /**
     * 查询粉丝的所有标签
     */
    @Override
    public List<WxUserTag> selectTagsByUserId(Long userId)
    {
        return wxUserTagMapper.selectTagsByUserId(userId);
    }

    /**
     * 新增粉丝标签关联
     */
    @Override
    public int insertWxUserTag(WxUserTag wxUserTag)
    {
        return wxUserTagMapper.insertWxUserTag(wxUserTag);
    }

    /**
     * 修改粉丝标签关联
     */
    @Override
    public int updateWxUserTag(WxUserTag wxUserTag)
    {
        return wxUserTagMapper.updateWxUserTag(wxUserTag);
    }

    /**
     * 批量删除粉丝标签关联
     */
    @Override
    public int deleteWxUserTagByIds(Long[] ids)
    {
        return wxUserTagMapper.deleteWxUserTagByIds(ids);
    }

    /**
     * 保存粉丝标签（先删后增）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveUserTags(Long userId, List<Long> tagIds)
    {
        // 先删除原有标签
        wxUserTagMapper.deleteTagsByUserId(userId);
        
        // 再批量插入新标签
        if (tagIds != null && tagIds.size() > 0)
        {
            List<WxUserTag> list = new ArrayList<>();
            Date now = new Date();
            for (Long tagId : tagIds)
            {
                WxUserTag userTag = new WxUserTag();
                userTag.setUserId(userId);
                userTag.setTagId(tagId);
                userTag.setCreateTime(now);
                list.add(userTag);
            }
            return wxUserTagMapper.batchInsertUserTags(list);
        }
        return 0;
    }
}
