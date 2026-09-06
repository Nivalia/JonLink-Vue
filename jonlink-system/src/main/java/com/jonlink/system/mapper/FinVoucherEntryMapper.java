package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.FinVoucherEntry;

/**
 * 凭证分录Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public interface FinVoucherEntryMapper 
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
     * 删除凭证分录
     * 
     * @param id 凭证分录主键
     * @return 结果
     */
    public int deleteFinVoucherEntryById(Long id);

    /**
     * 批量删除凭证分录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinVoucherEntryByIds(Long[] ids);

    /** 按 voucher_id 查全部分录(用于凭证展示/打印) */
    public List<FinVoucherEntry> selectByVoucherId(Long voucherId);

    /** 按 voucher_id 物理删除所有分录(配合头删除使用) */
    public int deleteByVoucherId(Long voucherId);
}
