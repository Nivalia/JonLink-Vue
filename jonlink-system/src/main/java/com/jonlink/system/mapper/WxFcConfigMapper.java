package com.jonlink.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.jonlink.system.domain.WxFcConfig;

/**
 * 核销表单字段配置Mapper
 *
 * @author jonlink
 */
public interface WxFcConfigMapper
{
    WxFcConfig selectWxFcConfigById(Long id);

    List<WxFcConfig> selectWxFcConfigList(WxFcConfig wxFcConfig);

    /** 按 source_type + visible 拉取 H5 渲染用的字段列表(按 sort_no asc) */
    List<WxFcConfig> selectWxFcConfigForRender(@Param("sourceType") String sourceType);

    int insertWxFcConfig(WxFcConfig wxFcConfig);

    int updateWxFcConfig(WxFcConfig wxFcConfig);

    int deleteWxFcConfigByIds(Long[] ids);

    int deleteWxFcConfigById(Long id);
}
