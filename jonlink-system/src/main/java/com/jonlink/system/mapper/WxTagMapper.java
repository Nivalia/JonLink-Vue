package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.WxTag;

/**
 * 公众号粉丝标签 Mapper
 * 
 * @author jonlink
 */
public interface WxTagMapper
{
    public WxTag selectWxTagById(Long id);

    public List<WxTag> selectWxTagList(WxTag wxTag);

    public int insertWxTag(WxTag wxTag);

    public int updateWxTag(WxTag wxTag);

    public int deleteWxTagById(Long id);

    public int deleteWxTagByIds(Long[] ids);
}
