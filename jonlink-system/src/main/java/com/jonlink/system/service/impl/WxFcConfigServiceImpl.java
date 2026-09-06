package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.domain.WxFcConfig;
import com.jonlink.system.mapper.WxFcConfigMapper;
import com.jonlink.system.service.IWxFcConfigService;

/**
 * 核销表单字段配置Service业务层
 *
 * @author jonlink
 */
@Service
public class WxFcConfigServiceImpl implements IWxFcConfigService
{
    @Autowired
    private WxFcConfigMapper wxFcConfigMapper;

    @Override
    public WxFcConfig selectWxFcConfigById(Long id)
    {
        return wxFcConfigMapper.selectWxFcConfigById(id);
    }

    @Override
    public List<WxFcConfig> selectWxFcConfigList(WxFcConfig wxFcConfig)
    {
        return wxFcConfigMapper.selectWxFcConfigList(wxFcConfig);
    }

    @Override
    public List<WxFcConfig> selectWxFcConfigForRender(String sourceType)
    {
        return wxFcConfigMapper.selectWxFcConfigForRender(sourceType);
    }

    @Override
    public int insertWxFcConfig(WxFcConfig wxFcConfig)
    {
        wxFcConfig.setCreateTime(DateUtils.getNowDate());
        return wxFcConfigMapper.insertWxFcConfig(wxFcConfig);
    }

    @Override
    public int updateWxFcConfig(WxFcConfig wxFcConfig)
    {
        wxFcConfig.setUpdateTime(DateUtils.getNowDate());
        return wxFcConfigMapper.updateWxFcConfig(wxFcConfig);
    }

    @Override
    public int deleteWxFcConfigByIds(Long[] ids)
    {
        return wxFcConfigMapper.deleteWxFcConfigByIds(ids);
    }

    @Override
    public int deleteWxFcConfigById(Long id)
    {
        return wxFcConfigMapper.deleteWxFcConfigById(id);
    }
}
