package com.jonlink.system.service;

import java.util.List;
import com.jonlink.system.domain.WxFcConfig;

/**
 * 核销表单字段配置Service
 *
 * @author jonlink
 */
public interface IWxFcConfigService
{
    WxFcConfig selectWxFcConfigById(Long id);
    List<WxFcConfig> selectWxFcConfigList(WxFcConfig wxFcConfig);
    List<WxFcConfig> selectWxFcConfigForRender(String sourceType);
    int insertWxFcConfig(WxFcConfig wxFcConfig);
    int updateWxFcConfig(WxFcConfig wxFcConfig);
    int deleteWxFcConfigByIds(Long[] ids);
    int deleteWxFcConfigById(Long id);
}
