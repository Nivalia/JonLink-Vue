package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.WxMpTemplateMsgMapper;
import com.jonlink.system.domain.WxMpTemplateMsg;
import com.jonlink.system.service.IWxMpTemplateMsgService;

/**
 * 发送记录Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@Service
public class WxMpTemplateMsgServiceImpl implements IWxMpTemplateMsgService 
{
    @Autowired
    private WxMpTemplateMsgMapper wxMpTemplateMsgMapper;

    /**
     * 查询发送记录
     * 
     * @param id 发送记录主键
     * @return 发送记录
     */
    @Override
    public WxMpTemplateMsg selectWxMpTemplateMsgById(Long id)
    {
        return wxMpTemplateMsgMapper.selectWxMpTemplateMsgById(id);
    }

    /**
     * 查询发送记录列表
     * 
     * @param wxMpTemplateMsg 发送记录
     * @return 发送记录
     */
    @Override
    public List<WxMpTemplateMsg> selectWxMpTemplateMsgList(WxMpTemplateMsg wxMpTemplateMsg)
    {
        return wxMpTemplateMsgMapper.selectWxMpTemplateMsgList(wxMpTemplateMsg);
    }

    /**
     * 新增发送记录
     * 
     * @param wxMpTemplateMsg 发送记录
     * @return 结果
     */
    @Override
    public int insertWxMpTemplateMsg(WxMpTemplateMsg wxMpTemplateMsg)
    {
        wxMpTemplateMsg.setCreateTime(DateUtils.getNowDate());
        if (wxMpTemplateMsg.getTransposeNo() == null || wxMpTemplateMsg.getTransposeNo().isEmpty())
        {
            wxMpTemplateMsg.setTransposeNo(java.util.UUID.randomUUID().toString().replace("-", ""));
        }
        return wxMpTemplateMsgMapper.insertWxMpTemplateMsg(wxMpTemplateMsg);
    }

    /**
     * 修改发送记录
     * 
     * @param wxMpTemplateMsg 发送记录
     * @return 结果
     */
    @Override
    public int updateWxMpTemplateMsg(WxMpTemplateMsg wxMpTemplateMsg)
    {
        wxMpTemplateMsg.setUpdateTime(DateUtils.getNowDate());
        return wxMpTemplateMsgMapper.updateWxMpTemplateMsg(wxMpTemplateMsg);
    }

    /**
     * 批量删除发送记录
     * 
     * @param ids 需要删除的发送记录主键
     * @return 结果
     */
    @Override
    public int deleteWxMpTemplateMsgByIds(Long[] ids)
    {
        return wxMpTemplateMsgMapper.deleteWxMpTemplateMsgByIds(ids);
    }

    /**
     * 删除发送记录信息
     * 
     * @param id 发送记录主键
     * @return 结果
     */
    @Override
    public int deleteWxMpTemplateMsgById(Long id)
    {
        return wxMpTemplateMsgMapper.deleteWxMpTemplateMsgById(id);
    }
}
