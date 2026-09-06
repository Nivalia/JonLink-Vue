package com.jonlink.system.service;

import java.util.List;
import com.jonlink.system.domain.FinLedgerVoucherLog;

/**
 * 台账记账日志Service接口
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public interface IFinLedgerVoucherLogService 
{
    /**
     * 查询台账记账日志
     * 
     * @param id 台账记账日志主键
     * @return 台账记账日志
     */
    public FinLedgerVoucherLog selectFinLedgerVoucherLogById(Long id);

    /**
     * 查询台账记账日志列表
     * 
     * @param finLedgerVoucherLog 台账记账日志
     * @return 台账记账日志集合
     */
    public List<FinLedgerVoucherLog> selectFinLedgerVoucherLogList(FinLedgerVoucherLog finLedgerVoucherLog);

    /**
     * 新增台账记账日志
     * 
     * @param finLedgerVoucherLog 台账记账日志
     * @return 结果
     */
    public int insertFinLedgerVoucherLog(FinLedgerVoucherLog finLedgerVoucherLog);

    /**
     * 修改台账记账日志
     * 
     * @param finLedgerVoucherLog 台账记账日志
     * @return 结果
     */
    public int updateFinLedgerVoucherLog(FinLedgerVoucherLog finLedgerVoucherLog);

    /**
     * 批量删除台账记账日志
     * 
     * @param ids 需要删除的台账记账日志主键集合
     * @return 结果
     */
    public int deleteFinLedgerVoucherLogByIds(Long[] ids);

    /**
     * 删除台账记账日志信息
     * 
     * @param id 台账记账日志主键
     * @return 结果
     */
    public int deleteFinLedgerVoucherLogById(Long id);
}
