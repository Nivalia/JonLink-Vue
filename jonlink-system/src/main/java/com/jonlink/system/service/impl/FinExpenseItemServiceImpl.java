package com.jonlink.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.FinExpenseItemMapper;
import com.jonlink.system.domain.FinExpenseItem;
import com.jonlink.system.service.IFinExpenseItemService;

/**
 * 报销明细Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@Service
public class FinExpenseItemServiceImpl implements IFinExpenseItemService 
{
    @Autowired
    private FinExpenseItemMapper finExpenseItemMapper;

    /**
     * 查询报销明细
     * 
     * @param id 报销明细主键
     * @return 报销明细
     */
    @Override
    public FinExpenseItem selectFinExpenseItemById(Long id)
    {
        return finExpenseItemMapper.selectFinExpenseItemById(id);
    }

    /**
     * 查询报销明细列表
     * 
     * @param finExpenseItem 报销明细
     * @return 报销明细
     */
    @Override
    public List<FinExpenseItem> selectFinExpenseItemList(FinExpenseItem finExpenseItem)
    {
        return finExpenseItemMapper.selectFinExpenseItemList(finExpenseItem);
    }

    /**
     * 新增报销明细
     * 
     * @param finExpenseItem 报销明细
     * @return 结果
     */
    @Override
    public int insertFinExpenseItem(FinExpenseItem finExpenseItem)
    {
        return finExpenseItemMapper.insertFinExpenseItem(finExpenseItem);
    }

    /**
     * 修改报销明细
     * 
     * @param finExpenseItem 报销明细
     * @return 结果
     */
    @Override
    public int updateFinExpenseItem(FinExpenseItem finExpenseItem)
    {
        return finExpenseItemMapper.updateFinExpenseItem(finExpenseItem);
    }

    /**
     * 批量删除报销明细
     * 
     * @param ids 需要删除的报销明细主键
     * @return 结果
     */
    @Override
    public int deleteFinExpenseItemByIds(Long[] ids)
    {
        return finExpenseItemMapper.deleteFinExpenseItemByIds(ids);
    }

    /**
     * 删除报销明细信息
     * 
     * @param id 报销明细主键
     * @return 结果
     */
    @Override
    public int deleteFinExpenseItemById(Long id)
    {
        return finExpenseItemMapper.deleteFinExpenseItemById(id);
    }
}
