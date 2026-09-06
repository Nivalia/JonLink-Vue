package com.jonlink.system.service;

import java.util.List;
import com.jonlink.system.domain.JonlinkLedgerAdjust;

/**
 * 台账批增退记录Service接口
 * 
 * @author jonlink
 * @date 2026-09-05
 */
public interface IJonlinkLedgerAdjustService 
{
    /**
     * 查询台账批增退记录
     */
    public JonlinkLedgerAdjust selectJonlinkLedgerAdjustById(Long id);

    /**
     * 查询台账批增退记录列表
     */
    public List<JonlinkLedgerAdjust> selectJonlinkLedgerAdjustList(JonlinkLedgerAdjust jonlinkLedgerAdjust);

    /**
     * 新增台账批增退记录
     */
    public int insertJonlinkLedgerAdjust(JonlinkLedgerAdjust jonlinkLedgerAdjust);

    /**
     * 修改台账批增退记录
     */
    public int updateJonlinkLedgerAdjust(JonlinkLedgerAdjust jonlinkLedgerAdjust);

    /**
     * 删除台账批增退记录
     */
    public int deleteJonlinkLedgerAdjustByIds(Long[] ids);

    /**
     * 执行批增/批退操作
     */
    public int executeAdjust(JonlinkLedgerAdjust adjust);
}
