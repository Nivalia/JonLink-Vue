package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.FinPayable;

/**
 * 应付单Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public interface FinPayableMapper 
{
    /**
     * 查询应付单
     * 
     * @param id 应付单主键
     * @return 应付单
     */
    public FinPayable selectFinPayableById(Long id);

    /**
     * 查询应付单列表
     * 
     * @param finPayable 应付单
     * @return 应付单集合
     */
    public List<FinPayable> selectFinPayableList(FinPayable finPayable);

    /**
     * 新增应付单
     * 
     * @param finPayable 应付单
     * @return 结果
     */
    public int insertFinPayable(FinPayable finPayable);

    /**
     * 修改应付单
     * 
     * @param finPayable 应付单
     * @return 结果
     */
    public int updateFinPayable(FinPayable finPayable);

    /**
     * 删除应付单
     * 
     * @param id 应付单主键
     * @return 结果
     */
    public int deleteFinPayableById(Long id);

    /**
     * 批量删除应付单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinPayableByIds(Long[] ids);
}
