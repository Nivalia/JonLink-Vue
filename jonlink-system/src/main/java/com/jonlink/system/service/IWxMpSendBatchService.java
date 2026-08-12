package com.jonlink.system.service;

import java.util.List;
import com.jonlink.system.domain.WxMpSendBatch;

/**
 * 发送批次Service接口
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public interface IWxMpSendBatchService 
{
    /**
     * 查询发送批次
     * 
     * @param id 发送批次主键
     * @return 发送批次
     */
    public WxMpSendBatch selectWxMpSendBatchById(Long id);

    /**
     * 查询发送批次列表
     * 
     * @param wxMpSendBatch 发送批次
     * @return 发送批次集合
     */
    public List<WxMpSendBatch> selectWxMpSendBatchList(WxMpSendBatch wxMpSendBatch);

    /**
     * 新增发送批次
     * 
     * @param wxMpSendBatch 发送批次
     * @return 结果
     */
    public int insertWxMpSendBatch(WxMpSendBatch wxMpSendBatch);

    /**
     * 修改发送批次
     * 
     * @param wxMpSendBatch 发送批次
     * @return 结果
     */
    public int updateWxMpSendBatch(WxMpSendBatch wxMpSendBatch);

    /**
     * 批量删除发送批次
     * 
     * @param ids 需要删除的发送批次主键集合
     * @return 结果
     */
    public int deleteWxMpSendBatchByIds(Long[] ids);

    /**
     * 删除发送批次信息
     * 
     * @param id 发送批次主键
     * @return 结果
     */
    public int deleteWxMpSendBatchById(Long id);
}
