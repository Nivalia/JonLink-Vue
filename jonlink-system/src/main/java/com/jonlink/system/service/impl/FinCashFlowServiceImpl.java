package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.FinCashFlowMapper;
import com.jonlink.system.domain.FinCashFlow;
import com.jonlink.system.service.IFinCashFlowService;

/**
 * 资金流水Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@Service
public class FinCashFlowServiceImpl implements IFinCashFlowService 
{
    @Autowired
    private FinCashFlowMapper finCashFlowMapper;

    /**
     * 查询资金流水
     * 
     * @param id 资金流水主键
     * @return 资金流水
     */
    @Override
    public FinCashFlow selectFinCashFlowById(Long id)
    {
        return finCashFlowMapper.selectFinCashFlowById(id);
    }

    /**
     * 查询资金流水列表
     * 
     * @param finCashFlow 资金流水
     * @return 资金流水
     */
    @Override
    public List<FinCashFlow> selectFinCashFlowList(FinCashFlow finCashFlow)
    {
        return finCashFlowMapper.selectFinCashFlowList(finCashFlow);
    }

    /**
     * 新增资金流水
     * 
     * @param finCashFlow 资金流水
     * @return 结果
     */
    @Override
    public int insertFinCashFlow(FinCashFlow finCashFlow)
    {
        finCashFlow.setCreateTime(DateUtils.getNowDate());
        return finCashFlowMapper.insertFinCashFlow(finCashFlow);
    }

    /**
     * 修改资金流水
     * 
     * @param finCashFlow 资金流水
     * @return 结果
     */
    @Override
    public int updateFinCashFlow(FinCashFlow finCashFlow)
    {
        return finCashFlowMapper.updateFinCashFlow(finCashFlow);
    }

    /**
     * 批量删除资金流水
     * 
     * @param ids 需要删除的资金流水主键
     * @return 结果
     */
    @Override
    public int deleteFinCashFlowByIds(Long[] ids)
    {
        return finCashFlowMapper.deleteFinCashFlowByIds(ids);
    }

    /**
     * 删除资金流水信息
     * 
     * @param id 资金流水主键
     * @return 结果
     */
    @Override
    public int deleteFinCashFlowById(Long id)
    {
        return finCashFlowMapper.deleteFinCashFlowById(id);
    }
}
