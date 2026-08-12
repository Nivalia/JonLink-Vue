package com.jonlink.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.WxLedgerSummaryMapper;
import com.jonlink.system.domain.WxLedgerSummary;
import com.jonlink.system.service.IWxLedgerSummaryService;

/**
 * 台账日汇总Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@Service
public class WxLedgerSummaryServiceImpl implements IWxLedgerSummaryService 
{
    @Autowired
    private WxLedgerSummaryMapper wxLedgerSummaryMapper;

    /**
     * 查询台账日汇总
     * 
     * @param id 台账日汇总主键
     * @return 台账日汇总
     */
    @Override
    public WxLedgerSummary selectWxLedgerSummaryById(Long id)
    {
        return wxLedgerSummaryMapper.selectWxLedgerSummaryById(id);
    }

    /**
     * 查询台账日汇总列表
     * 
     * @param wxLedgerSummary 台账日汇总
     * @return 台账日汇总
     */
    @Override
    public List<WxLedgerSummary> selectWxLedgerSummaryList(WxLedgerSummary wxLedgerSummary)
    {
        return wxLedgerSummaryMapper.selectWxLedgerSummaryList(wxLedgerSummary);
    }

    /**
     * 新增台账日汇总
     * 
     * @param wxLedgerSummary 台账日汇总
     * @return 结果
     */
    @Override
    public int insertWxLedgerSummary(WxLedgerSummary wxLedgerSummary)
    {
        return wxLedgerSummaryMapper.insertWxLedgerSummary(wxLedgerSummary);
    }

    /**
     * 修改台账日汇总
     * 
     * @param wxLedgerSummary 台账日汇总
     * @return 结果
     */
    @Override
    public int updateWxLedgerSummary(WxLedgerSummary wxLedgerSummary)
    {
        return wxLedgerSummaryMapper.updateWxLedgerSummary(wxLedgerSummary);
    }

    /**
     * 批量删除台账日汇总
     * 
     * @param ids 需要删除的台账日汇总主键
     * @return 结果
     */
    @Override
    public int deleteWxLedgerSummaryByIds(Long[] ids)
    {
        return wxLedgerSummaryMapper.deleteWxLedgerSummaryByIds(ids);
    }

    /**
     * 删除台账日汇总信息
     * 
     * @param id 台账日汇总主键
     * @return 结果
     */
    @Override
    public int deleteWxLedgerSummaryById(Long id)
    {
        return wxLedgerSummaryMapper.deleteWxLedgerSummaryById(id);
    }
}
