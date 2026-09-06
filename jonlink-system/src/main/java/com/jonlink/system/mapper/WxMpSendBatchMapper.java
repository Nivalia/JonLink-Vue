package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.WxMpSendBatch;

/**
 * 发送批次Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public interface WxMpSendBatchMapper 
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

    public int updateCheckStats(WxMpSendBatch wxMpSendBatch);

    /**
     * 删除发送批次
     * 
     * @param id 发送批次主键
     * @return 结果
     */
    public int deleteWxMpSendBatchById(Long id);

    /**
     * 批量删除发送批次
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWxMpSendBatchByIds(Long[] ids);
}
