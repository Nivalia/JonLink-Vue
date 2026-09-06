package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.FinAllocation;

/**
 * 核销记录Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public interface FinAllocationMapper 
{
    /**
     * 查询核销记录
     * 
     * @param id 核销记录主键
     * @return 核销记录
     */
    public FinAllocation selectFinAllocationById(Long id);

    /**
     * 查询核销记录列表
     * 
     * @param finAllocation 核销记录
     * @return 核销记录集合
     */
    public List<FinAllocation> selectFinAllocationList(FinAllocation finAllocation);

    /**
     * 新增核销记录
     * 
     * @param finAllocation 核销记录
     * @return 结果
     */
    public int insertFinAllocation(FinAllocation finAllocation);

    /**
     * 修改核销记录
     * 
     * @param finAllocation 核销记录
     * @return 结果
     */
    public int updateFinAllocation(FinAllocation finAllocation);

    /**
     * 删除核销记录
     * 
     * @param id 核销记录主键
     * @return 结果
     */
    public int deleteFinAllocationById(Long id);

    /**
     * 批量删除核销记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinAllocationByIds(Long[] ids);
}
