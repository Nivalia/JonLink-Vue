package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.FinVoucherTemplateMapper;
import com.jonlink.system.domain.FinVoucherTemplate;
import com.jonlink.system.service.IFinVoucherTemplateService;

/**
 * 凭证模板Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@Service
public class FinVoucherTemplateServiceImpl implements IFinVoucherTemplateService 
{
    @Autowired
    private FinVoucherTemplateMapper finVoucherTemplateMapper;

    /**
     * 查询凭证模板
     * 
     * @param id 凭证模板主键
     * @return 凭证模板
     */
    @Override
    public FinVoucherTemplate selectFinVoucherTemplateById(Long id)
    {
        return finVoucherTemplateMapper.selectFinVoucherTemplateById(id);
    }

    /**
     * 查询凭证模板列表
     * 
     * @param finVoucherTemplate 凭证模板
     * @return 凭证模板
     */
    @Override
    public List<FinVoucherTemplate> selectFinVoucherTemplateList(FinVoucherTemplate finVoucherTemplate)
    {
        return finVoucherTemplateMapper.selectFinVoucherTemplateList(finVoucherTemplate);
    }

    /**
     * 新增凭证模板
     * 
     * @param finVoucherTemplate 凭证模板
     * @return 结果
     */
    @Override
    public int insertFinVoucherTemplate(FinVoucherTemplate finVoucherTemplate)
    {
        finVoucherTemplate.setCreateTime(DateUtils.getNowDate());
        return finVoucherTemplateMapper.insertFinVoucherTemplate(finVoucherTemplate);
    }

    /**
     * 修改凭证模板
     * 
     * @param finVoucherTemplate 凭证模板
     * @return 结果
     */
    @Override
    public int updateFinVoucherTemplate(FinVoucherTemplate finVoucherTemplate)
    {
        finVoucherTemplate.setUpdateTime(DateUtils.getNowDate());
        return finVoucherTemplateMapper.updateFinVoucherTemplate(finVoucherTemplate);
    }

    /**
     * 批量删除凭证模板
     * 
     * @param ids 需要删除的凭证模板主键
     * @return 结果
     */
    @Override
    public int deleteFinVoucherTemplateByIds(Long[] ids)
    {
        return finVoucherTemplateMapper.deleteFinVoucherTemplateByIds(ids);
    }

    /**
     * 删除凭证模板信息
     * 
     * @param id 凭证模板主键
     * @return 结果
     */
    @Override
    public int deleteFinVoucherTemplateById(Long id)
    {
        return finVoucherTemplateMapper.deleteFinVoucherTemplateById(id);
    }
}
