package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.FinInvoice;

/**
 * 发票Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public interface FinInvoiceMapper 
{
    /**
     * 查询发票
     * 
     * @param id 发票主键
     * @return 发票
     */
    public FinInvoice selectFinInvoiceById(Long id);

    /**
     * 查询发票列表
     * 
     * @param finInvoice 发票
     * @return 发票集合
     */
    public List<FinInvoice> selectFinInvoiceList(FinInvoice finInvoice);

    /**
     * 新增发票
     * 
     * @param finInvoice 发票
     * @return 结果
     */
    public int insertFinInvoice(FinInvoice finInvoice);

    /**
     * 修改发票
     * 
     * @param finInvoice 发票
     * @return 结果
     */
    public int updateFinInvoice(FinInvoice finInvoice);

    /**
     * 删除发票
     * 
     * @param id 发票主键
     * @return 结果
     */
    public int deleteFinInvoiceById(Long id);

    /**
     * 批量删除发票
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinInvoiceByIds(Long[] ids);
}
