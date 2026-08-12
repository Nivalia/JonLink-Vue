package com.jonlink.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.domain.WxTag;
import com.jonlink.system.mapper.WxTagMapper;
import com.jonlink.system.wx.service.IWxTagService;

/**
 * 公众号粉丝标签 Service 实现
 * 
 * @author jonlink
 */
@Service
public class WxTagServiceImpl implements IWxTagService
{
    @Autowired
    private WxTagMapper wxTagMapper;

    @Override
    public WxTag selectWxTagById(Long id)
    {
        return wxTagMapper.selectWxTagById(id);
    }

    @Override
    public List<WxTag> selectWxTagList(WxTag wxTag)
    {
        return wxTagMapper.selectWxTagList(wxTag);
    }

    @Override
    public int insertWxTag(WxTag wxTag)
    {
        return wxTagMapper.insertWxTag(wxTag);
    }

    @Override
    public int updateWxTag(WxTag wxTag)
    {
        return wxTagMapper.updateWxTag(wxTag);
    }

    @Override
    public int deleteWxTagByIds(Long[] ids)
    {
        return wxTagMapper.deleteWxTagByIds(ids);
    }
}
