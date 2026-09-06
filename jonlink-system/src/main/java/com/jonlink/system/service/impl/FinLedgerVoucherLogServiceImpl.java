package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.FinLedgerVoucherLogMapper;
import com.jonlink.system.domain.FinLedgerVoucherLog;
import com.jonlink.system.service.IFinLedgerVoucherLogService;

/**
 * 台账记账日志Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@Service
public class FinLedgerVoucherLogServiceImpl implements IFinLedgerVoucherLogService 
{
    @Autowired
    private FinLedgerVoucherLogMapper finLedgerVoucherLogMapper;

    /**
     * 查询台账记账日志
     * 
     * @param id 台账记账日志主键
     * @return 台账记账日志
     */
    @Override
    public FinLedgerVoucherLog selectFinLedgerVoucherLogById(Long id)
    {
        return finLedgerVoucherLogMapper.selectFinLedgerVoucherLogById(id);
    }

    /**
     * 查询台账记账日志列表
     * 
     * @param finLedgerVoucherLog 台账记账日志
     * @return 台账记账日志
     */
    @Override
    public List<FinLedgerVoucherLog> selectFinLedgerVoucherLogList(FinLedgerVoucherLog finLedgerVoucherLog)
    {
        return finLedgerVoucherLogMapper.selectFinLedgerVoucherLogList(finLedgerVoucherLog);
    }

    /**
     * 新增台账记账日志
     * 
     * @param finLedgerVoucherLog 台账记账日志
     * @return 结果
     */
    @Override
    public int insertFinLedgerVoucherLog(FinLedgerVoucherLog finLedgerVoucherLog)
    {
        finLedgerVoucherLog.setCreateTime(DateUtils.getNowDate());
        return finLedgerVoucherLogMapper.insertFinLedgerVoucherLog(finLedgerVoucherLog);
    }

    /**
     * 修改台账记账日志
     * 
     * @param finLedgerVoucherLog 台账记账日志
     * @return 结果
     */
    @Override
    public int updateFinLedgerVoucherLog(FinLedgerVoucherLog finLedgerVoucherLog)
    {
        finLedgerVoucherLog.setUpdateTime(DateUtils.getNowDate());
        return finLedgerVoucherLogMapper.updateFinLedgerVoucherLog(finLedgerVoucherLog);
    }

    /**
     * 批量删除台账记账日志
     * 
     * @param ids 需要删除的台账记账日志主键
     * @return 结果
     */
    @Override
    public int deleteFinLedgerVoucherLogByIds(Long[] ids)
    {
        return finLedgerVoucherLogMapper.deleteFinLedgerVoucherLogByIds(ids);
    }

    /**
     * 删除台账记账日志信息
     * 
     * @param id 台账记账日志主键
     * @return 结果
     */
    @Override
    public int deleteFinLedgerVoucherLogById(Long id)
    {
        return finLedgerVoucherLogMapper.deleteFinLedgerVoucherLogById(id);
    }
}
