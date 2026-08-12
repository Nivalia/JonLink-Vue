package com.jonlink.system.service;

import java.util.List;
import com.jonlink.system.domain.WxLedgerSummary;

/**
 * 台账日汇总Service接口
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public interface IWxLedgerSummaryService 
{
    /**
     * 查询台账日汇总
     * 
     * @param id 台账日汇总主键
     * @return 台账日汇总
     */
    public WxLedgerSummary selectWxLedgerSummaryById(Long id);

    /**
     * 查询台账日汇总列表
     * 
     * @param wxLedgerSummary 台账日汇总
     * @return 台账日汇总集合
     */
    public List<WxLedgerSummary> selectWxLedgerSummaryList(WxLedgerSummary wxLedgerSummary);

    /**
     * 新增台账日汇总
     * 
     * @param wxLedgerSummary 台账日汇总
     * @return 结果
     */
    public int insertWxLedgerSummary(WxLedgerSummary wxLedgerSummary);

    /**
     * 修改台账日汇总
     * 
     * @param wxLedgerSummary 台账日汇总
     * @return 结果
     */
    public int updateWxLedgerSummary(WxLedgerSummary wxLedgerSummary);

    /**
     * 批量删除台账日汇总
     * 
     * @param ids 需要删除的台账日汇总主键集合
     * @return 结果
     */
    public int deleteWxLedgerSummaryByIds(Long[] ids);

    /**
     * 删除台账日汇总信息
     * 
     * @param id 台账日汇总主键
     * @return 结果
     */
    public int deleteWxLedgerSummaryById(Long id);
}
