package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.WxMpSendBatchMapper;
import com.jonlink.system.domain.WxMpSendBatch;
import com.jonlink.system.service.IWxMpSendBatchService;

/**
 * 发送批次Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@Service
public class WxMpSendBatchServiceImpl implements IWxMpSendBatchService 
{
    @Autowired
    private WxMpSendBatchMapper wxMpSendBatchMapper;

    /**
     * 查询发送批次
     * 
     * @param id 发送批次主键
     * @return 发送批次
     */
    @Override
    public WxMpSendBatch selectWxMpSendBatchById(Long id)
    {
        return wxMpSendBatchMapper.selectWxMpSendBatchById(id);
    }

    /**
     * 查询发送批次列表
     * 
     * @param wxMpSendBatch 发送批次
     * @return 发送批次
     */
    @Override
    public List<WxMpSendBatch> selectWxMpSendBatchList(WxMpSendBatch wxMpSendBatch)
    {
        return wxMpSendBatchMapper.selectWxMpSendBatchList(wxMpSendBatch);
    }

    /**
     * 新增发送批次
     * 
     * @param wxMpSendBatch 发送批次
     * @return 结果
     */
    @Override
    public int insertWxMpSendBatch(WxMpSendBatch wxMpSendBatch)
    {
        wxMpSendBatch.setCreateTime(DateUtils.getNowDate());
        return wxMpSendBatchMapper.insertWxMpSendBatch(wxMpSendBatch);
    }

    /**
     * 修改发送批次
     * 
     * @param wxMpSendBatch 发送批次
     * @return 结果
     */
    @Override
    public int updateWxMpSendBatch(WxMpSendBatch wxMpSendBatch)
    {
        wxMpSendBatch.setUpdateTime(DateUtils.getNowDate());
        return wxMpSendBatchMapper.updateWxMpSendBatch(wxMpSendBatch);
    }

    /**
     * 批量删除发送批次
     * 
     * @param ids 需要删除的发送批次主键
     * @return 结果
     */
    @Override
    public int deleteWxMpSendBatchByIds(Long[] ids)
    {
        return wxMpSendBatchMapper.deleteWxMpSendBatchByIds(ids);
    }

    /**
     * 删除发送批次信息
     * 
     * @param id 发送批次主键
     * @return 结果
     */
    @Override
    public int deleteWxMpSendBatchById(Long id)
    {
        return wxMpSendBatchMapper.deleteWxMpSendBatchById(id);
    }
}
