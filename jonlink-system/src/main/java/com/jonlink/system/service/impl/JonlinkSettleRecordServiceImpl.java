package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.JonlinkSettleRecordMapper;
import com.jonlink.system.domain.JonlinkSettleRecord;
import com.jonlink.system.service.IJonlinkSettleRecordService;

/**
 * 结算记录Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@Service
public class JonlinkSettleRecordServiceImpl implements IJonlinkSettleRecordService 
{
    @Autowired
    private JonlinkSettleRecordMapper jonlinkSettleRecordMapper;

    /**
     * 查询结算记录
     * 
     * @param id 结算记录主键
     * @return 结算记录
     */
    @Override
    public JonlinkSettleRecord selectJonlinkSettleRecordById(Long id)
    {
        return jonlinkSettleRecordMapper.selectJonlinkSettleRecordById(id);
    }

    /**
     * 查询结算记录列表
     * 
     * @param jonlinkSettleRecord 结算记录
     * @return 结算记录
     */
    @Override
    public List<JonlinkSettleRecord> selectJonlinkSettleRecordList(JonlinkSettleRecord jonlinkSettleRecord)
    {
        return jonlinkSettleRecordMapper.selectJonlinkSettleRecordList(jonlinkSettleRecord);
    }

    /**
     * 新增结算记录
     * 
     * @param jonlinkSettleRecord 结算记录
     * @return 结果
     */
    @Override
    public int insertJonlinkSettleRecord(JonlinkSettleRecord jonlinkSettleRecord)
    {
        jonlinkSettleRecord.setCreateTime(DateUtils.getNowDate());
        return jonlinkSettleRecordMapper.insertJonlinkSettleRecord(jonlinkSettleRecord);
    }

    /**
     * 修改结算记录
     * 
     * @param jonlinkSettleRecord 结算记录
     * @return 结果
     */
    @Override
    public int updateJonlinkSettleRecord(JonlinkSettleRecord jonlinkSettleRecord)
    {
        jonlinkSettleRecord.setUpdateTime(DateUtils.getNowDate());
        return jonlinkSettleRecordMapper.updateJonlinkSettleRecord(jonlinkSettleRecord);
    }

    /**
     * 批量删除结算记录
     * 
     * @param ids 需要删除的结算记录主键
     * @return 结果
     */
    @Override
    public int deleteJonlinkSettleRecordByIds(Long[] ids)
    {
        return jonlinkSettleRecordMapper.deleteJonlinkSettleRecordByIds(ids);
    }

    /**
     * 删除结算记录信息
     * 
     * @param id 结算记录主键
     * @return 结果
     */
    @Override
    public int deleteJonlinkSettleRecordById(Long id)
    {
        return jonlinkSettleRecordMapper.deleteJonlinkSettleRecordById(id);
    }
}
