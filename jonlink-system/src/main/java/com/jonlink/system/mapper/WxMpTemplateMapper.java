package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.WxMpTemplate;

/**
 * 模板管理Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public interface WxMpTemplateMapper 
{
    /**
     * 查询模板管理
     * 
     * @param id 模板管理主键
     * @return 模板管理
     */
    public WxMpTemplate selectWxMpTemplateById(Long id);

    /**
     * 查询模板管理列表
     * 
     * @param wxMpTemplate 模板管理
     * @return 模板管理集合
     */
    public List<WxMpTemplate> selectWxMpTemplateList(WxMpTemplate wxMpTemplate);

    /**
     * 新增模板管理
     * 
     * @param wxMpTemplate 模板管理
     * @return 结果
     */
    public int insertWxMpTemplate(WxMpTemplate wxMpTemplate);

    /**
     * 修改模板管理
     * 
     * @param wxMpTemplate 模板管理
     * @return 结果
     */
    public int updateWxMpTemplate(WxMpTemplate wxMpTemplate);

    /**
     * 删除模板管理
     * 
     * @param id 模板管理主键
     * @return 结果
     */
    public int deleteWxMpTemplateById(Long id);

    /**
     * 批量删除模板管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWxMpTemplateByIds(Long[] ids);
}
