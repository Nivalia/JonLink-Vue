package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.JonlinkSettleRecord;

/**
 * 结算记录Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public interface JonlinkSettleRecordMapper 
{
    /**
     * 查询结算记录
     * 
     * @param id 结算记录主键
     * @return 结算记录
     */
    public JonlinkSettleRecord selectJonlinkSettleRecordById(Long id);

    /**
     * 查询结算记录列表
     * 
     * @param jonlinkSettleRecord 结算记录
     * @return 结算记录集合
     */
    public List<JonlinkSettleRecord> selectJonlinkSettleRecordList(JonlinkSettleRecord jonlinkSettleRecord);

    /**
     * 新增结算记录
     * 
     * @param jonlinkSettleRecord 结算记录
     * @return 结果
     */
    public int insertJonlinkSettleRecord(JonlinkSettleRecord jonlinkSettleRecord);

    /**
     * 修改结算记录
     * 
     * @param jonlinkSettleRecord 结算记录
     * @return 结果
     */
    public int updateJonlinkSettleRecord(JonlinkSettleRecord jonlinkSettleRecord);

    /**
     * 删除结算记录
     * 
     * @param id 结算记录主键
     * @return 结果
     */
    public int deleteJonlinkSettleRecordById(Long id);

    /**
     * 批量删除结算记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteJonlinkSettleRecordByIds(Long[] ids);
}
