package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.WxMpTemplateMapper;
import com.jonlink.system.domain.WxMpTemplate;
import com.jonlink.system.service.IWxMpTemplateService;

/**
 * 模板管理Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@Service
public class WxMpTemplateServiceImpl implements IWxMpTemplateService 
{
    @Autowired
    private WxMpTemplateMapper wxMpTemplateMapper;

    /**
     * 查询模板管理
     * 
     * @param id 模板管理主键
     * @return 模板管理
     */
    @Override
    public WxMpTemplate selectWxMpTemplateById(Long id)
    {
        return wxMpTemplateMapper.selectWxMpTemplateById(id);
    }

    /**
     * 查询模板管理列表
     * 
     * @param wxMpTemplate 模板管理
     * @return 模板管理
     */
    @Override
    public List<WxMpTemplate> selectWxMpTemplateList(WxMpTemplate wxMpTemplate)
    {
        return wxMpTemplateMapper.selectWxMpTemplateList(wxMpTemplate);
    }

    /**
     * 新增模板管理
     * 
     * @param wxMpTemplate 模板管理
     * @return 结果
     */
    @Override
    public int insertWxMpTemplate(WxMpTemplate wxMpTemplate)
    {
        wxMpTemplate.setCreateTime(DateUtils.getNowDate());
        return wxMpTemplateMapper.insertWxMpTemplate(wxMpTemplate);
    }

    /**
     * 修改模板管理
     * 
     * @param wxMpTemplate 模板管理
     * @return 结果
     */
    @Override
    public int updateWxMpTemplate(WxMpTemplate wxMpTemplate)
    {
        wxMpTemplate.setUpdateTime(DateUtils.getNowDate());
        return wxMpTemplateMapper.updateWxMpTemplate(wxMpTemplate);
    }

    /**
     * 批量删除模板管理
     * 
     * @param ids 需要删除的模板管理主键
     * @return 结果
     */
    @Override
    public int deleteWxMpTemplateByIds(Long[] ids)
    {
        return wxMpTemplateMapper.deleteWxMpTemplateByIds(ids);
    }

    /**
     * 删除模板管理信息
     * 
     * @param id 模板管理主键
     * @return 结果
     */
    @Override
    public int deleteWxMpTemplateById(Long id)
    {
        return wxMpTemplateMapper.deleteWxMpTemplateById(id);
    }
}
