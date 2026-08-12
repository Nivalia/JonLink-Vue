package com.jonlink.system.service;

import java.util.List;
import com.jonlink.system.domain.WxMpTemplateMsg;

/**
 * 发送记录Service接口
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public interface IWxMpTemplateMsgService 
{
    /**
     * 查询发送记录
     * 
     * @param id 发送记录主键
     * @return 发送记录
     */
    public WxMpTemplateMsg selectWxMpTemplateMsgById(Long id);

    /**
     * 查询发送记录列表
     * 
     * @param wxMpTemplateMsg 发送记录
     * @return 发送记录集合
     */
    public List<WxMpTemplateMsg> selectWxMpTemplateMsgList(WxMpTemplateMsg wxMpTemplateMsg);

    /**
     * 新增发送记录
     * 
     * @param wxMpTemplateMsg 发送记录
     * @return 结果
     */
    public int insertWxMpTemplateMsg(WxMpTemplateMsg wxMpTemplateMsg);

    /**
     * 修改发送记录
     * 
     * @param wxMpTemplateMsg 发送记录
     * @return 结果
     */
    public int updateWxMpTemplateMsg(WxMpTemplateMsg wxMpTemplateMsg);

    /**
     * 批量删除发送记录
     * 
     * @param ids 需要删除的发送记录主键集合
     * @return 结果
     */
    public int deleteWxMpTemplateMsgByIds(Long[] ids);

    /**
     * 删除发送记录信息
     * 
     * @param id 发送记录主键
     * @return 结果
     */
    public int deleteWxMpTemplateMsgById(Long id);
}
