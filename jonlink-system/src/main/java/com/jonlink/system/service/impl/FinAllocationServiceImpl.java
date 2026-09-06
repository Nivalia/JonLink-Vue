package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.FinAllocationMapper;
import com.jonlink.system.domain.FinAllocation;
import com.jonlink.system.service.IFinAllocationService;

/**
 * 核销记录Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@Service
public class FinAllocationServiceImpl implements IFinAllocationService 
{
    @Autowired
    private FinAllocationMapper finAllocationMapper;

    /**
     * 查询核销记录
     * 
     * @param id 核销记录主键
     * @return 核销记录
     */
    @Override
    public FinAllocation selectFinAllocationById(Long id)
    {
        return finAllocationMapper.selectFinAllocationById(id);
    }

    /**
     * 查询核销记录列表
     * 
     * @param finAllocation 核销记录
     * @return 核销记录
     */
    @Override
    public List<FinAllocation> selectFinAllocationList(FinAllocation finAllocation)
    {
        return finAllocationMapper.selectFinAllocationList(finAllocation);
    }

    /**
     * 新增核销记录
     * 
     * @param finAllocation 核销记录
     * @return 结果
     */
    @Override
    public int insertFinAllocation(FinAllocation finAllocation)
    {
        finAllocation.setCreateTime(DateUtils.getNowDate());
        return finAllocationMapper.insertFinAllocation(finAllocation);
    }

    /**
     * 修改核销记录
     * 
     * @param finAllocation 核销记录
     * @return 结果
     */
    @Override
    public int updateFinAllocation(FinAllocation finAllocation)
    {
        return finAllocationMapper.updateFinAllocation(finAllocation);
    }

    /**
     * 批量删除核销记录
     * 
     * @param ids 需要删除的核销记录主键
     * @return 结果
     */
    @Override
    public int deleteFinAllocationByIds(Long[] ids)
    {
        return finAllocationMapper.deleteFinAllocationByIds(ids);
    }

    /**
     * 删除核销记录信息
     * 
     * @param id 核销记录主键
     * @return 结果
     */
    @Override
    public int deleteFinAllocationById(Long id)
    {
        return finAllocationMapper.deleteFinAllocationById(id);
    }
}
