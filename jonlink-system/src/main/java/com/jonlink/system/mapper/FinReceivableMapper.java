package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.FinReceivable;

/**
 * 应收单Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public interface FinReceivableMapper 
{
    /**
     * 查询应收单
     * 
     * @param id 应收单主键
     * @return 应收单
     */
    public FinReceivable selectFinReceivableById(Long id);

    /**
     * 查询应收单列表
     * 
     * @param finReceivable 应收单
     * @return 应收单集合
     */
    public List<FinReceivable> selectFinReceivableList(FinReceivable finReceivable);

    /**
     * 新增应收单
     * 
     * @param finReceivable 应收单
     * @return 结果
     */
    public int insertFinReceivable(FinReceivable finReceivable);

    /**
     * 修改应收单
     * 
     * @param finReceivable 应收单
     * @return 结果
     */
    public int updateFinReceivable(FinReceivable finReceivable);

    /**
     * 删除应收单
     * 
     * @param id 应收单主键
     * @return 结果
     */
    public int deleteFinReceivableById(Long id);

    /**
     * 批量删除应收单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinReceivableByIds(Long[] ids);
}
