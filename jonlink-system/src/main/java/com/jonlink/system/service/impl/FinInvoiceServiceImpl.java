package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.FinInvoiceMapper;
import com.jonlink.system.domain.FinInvoice;
import com.jonlink.system.service.IFinInvoiceService;

/**
 * 发票Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@Service
public class FinInvoiceServiceImpl implements IFinInvoiceService 
{
    @Autowired
    private FinInvoiceMapper finInvoiceMapper;

    /**
     * 查询发票
     * 
     * @param id 发票主键
     * @return 发票
     */
    @Override
    public FinInvoice selectFinInvoiceById(Long id)
    {
        return finInvoiceMapper.selectFinInvoiceById(id);
    }

    /**
     * 查询发票列表
     * 
     * @param finInvoice 发票
     * @return 发票
     */
    @Override
    public List<FinInvoice> selectFinInvoiceList(FinInvoice finInvoice)
    {
        return finInvoiceMapper.selectFinInvoiceList(finInvoice);
    }

    /**
     * 新增发票
     * 
     * @param finInvoice 发票
     * @return 结果
     */
    @Override
    public int insertFinInvoice(FinInvoice finInvoice)
    {
        finInvoice.setCreateTime(DateUtils.getNowDate());
        return finInvoiceMapper.insertFinInvoice(finInvoice);
    }

    /**
     * 修改发票
     * 
     * @param finInvoice 发票
     * @return 结果
     */
    @Override
    public int updateFinInvoice(FinInvoice finInvoice)
    {
        finInvoice.setUpdateTime(DateUtils.getNowDate());
        return finInvoiceMapper.updateFinInvoice(finInvoice);
    }

    /**
     * 批量删除发票
     * 
     * @param ids 需要删除的发票主键
     * @return 结果
     */
    @Override
    public int deleteFinInvoiceByIds(Long[] ids)
    {
        return finInvoiceMapper.deleteFinInvoiceByIds(ids);
    }

    /**
     * 删除发票信息
     * 
     * @param id 发票主键
     * @return 结果
     */
    @Override
    public int deleteFinInvoiceById(Long id)
    {
        return finInvoiceMapper.deleteFinInvoiceById(id);
    }
}
