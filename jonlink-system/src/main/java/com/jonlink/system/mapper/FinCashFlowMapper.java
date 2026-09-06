package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.FinCashFlow;

/**
 * 资金流水Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public interface FinCashFlowMapper 
{
    /**
     * 查询资金流水
     * 
     * @param id 资金流水主键
     * @return 资金流水
     */
    public FinCashFlow selectFinCashFlowById(Long id);

    /**
     * 查询资金流水列表
     * 
     * @param finCashFlow 资金流水
     * @return 资金流水集合
     */
    public List<FinCashFlow> selectFinCashFlowList(FinCashFlow finCashFlow);

    /**
     * 新增资金流水
     * 
     * @param finCashFlow 资金流水
     * @return 结果
     */
    public int insertFinCashFlow(FinCashFlow finCashFlow);

    /**
     * 修改资金流水
     * 
     * @param finCashFlow 资金流水
     * @return 结果
     */
    public int updateFinCashFlow(FinCashFlow finCashFlow);

    /**
     * 删除资金流水
     * 
     * @param id 资金流水主键
     * @return 结果
     */
    public int deleteFinCashFlowById(Long id);

    /**
     * 批量删除资金流水
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinCashFlowByIds(Long[] ids);
}
