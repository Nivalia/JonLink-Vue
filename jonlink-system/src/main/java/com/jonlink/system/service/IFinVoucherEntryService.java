package com.jonlink.system.service;

import java.util.List;
import com.jonlink.system.domain.FinVoucherEntry;

/**
 * 凭证分录Service接口
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public interface IFinVoucherEntryService 
{
    /**
     * 查询凭证分录
     * 
     * @param id 凭证分录主键
     * @return 凭证分录
     */
    public FinVoucherEntry selectFinVoucherEntryById(Long id);

    /**
     * 查询凭证分录列表
     * 
     * @param finVoucherEntry 凭证分录
     * @return 凭证分录集合
     */
    public List<FinVoucherEntry> selectFinVoucherEntryList(FinVoucherEntry finVoucherEntry);

    /**
     * 新增凭证分录
     * 
     * @param finVoucherEntry 凭证分录
     * @return 结果
     */
    public int insertFinVoucherEntry(FinVoucherEntry finVoucherEntry);

    /**
     * 修改凭证分录
     * 
     * @param finVoucherEntry 凭证分录
     * @return 结果
     */
    public int updateFinVoucherEntry(FinVoucherEntry finVoucherEntry);

    /**
     * 批量删除凭证分录
     * 
     * @param ids 需要删除的凭证分录主键集合
     * @return 结果
     */
    public int deleteFinVoucherEntryByIds(Long[] ids);

    /**
     * 删除凭证分录信息
     * 
     * @param id 凭证分录主键
     * @return 结果
     */
    public int deleteFinVoucherEntryById(Long id);
}
