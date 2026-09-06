package com.jonlink.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.FinVoucherEntryMapper;
import com.jonlink.system.domain.FinVoucherEntry;
import com.jonlink.system.service.IFinVoucherEntryService;

/**
 * 凭证分录Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@Service
public class FinVoucherEntryServiceImpl implements IFinVoucherEntryService 
{
    @Autowired
    private FinVoucherEntryMapper finVoucherEntryMapper;

    /**
     * 查询凭证分录
     * 
     * @param id 凭证分录主键
     * @return 凭证分录
     */
    @Override
    public FinVoucherEntry selectFinVoucherEntryById(Long id)
    {
        return finVoucherEntryMapper.selectFinVoucherEntryById(id);
    }

    /**
     * 查询凭证分录列表
     * 
     * @param finVoucherEntry 凭证分录
     * @return 凭证分录
     */
    @Override
    public List<FinVoucherEntry> selectFinVoucherEntryList(FinVoucherEntry finVoucherEntry)
    {
        return finVoucherEntryMapper.selectFinVoucherEntryList(finVoucherEntry);
    }

    /**
     * 新增凭证分录
     * 
     * @param finVoucherEntry 凭证分录
     * @return 结果
     */
    @Override
    public int insertFinVoucherEntry(FinVoucherEntry finVoucherEntry)
    {
        return finVoucherEntryMapper.insertFinVoucherEntry(finVoucherEntry);
    }

    /**
     * 修改凭证分录
     * 
     * @param finVoucherEntry 凭证分录
     * @return 结果
     */
    @Override
    public int updateFinVoucherEntry(FinVoucherEntry finVoucherEntry)
    {
        return finVoucherEntryMapper.updateFinVoucherEntry(finVoucherEntry);
    }

    /**
     * 批量删除凭证分录
     * 
     * @param ids 需要删除的凭证分录主键
     * @return 结果
     */
    @Override
    public int deleteFinVoucherEntryByIds(Long[] ids)
    {
        return finVoucherEntryMapper.deleteFinVoucherEntryByIds(ids);
    }

    /**
     * 删除凭证分录信息
     * 
     * @param id 凭证分录主键
     * @return 结果
     */
    @Override
    public int deleteFinVoucherEntryById(Long id)
    {
        return finVoucherEntryMapper.deleteFinVoucherEntryById(id);
    }
}
