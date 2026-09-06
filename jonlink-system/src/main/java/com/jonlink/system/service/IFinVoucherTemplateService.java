package com.jonlink.system.service;

import java.util.List;
import com.jonlink.system.domain.FinVoucherTemplate;

/**
 * 凭证模板Service接口
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public interface IFinVoucherTemplateService 
{
    /**
     * 查询凭证模板
     * 
     * @param id 凭证模板主键
     * @return 凭证模板
     */
    public FinVoucherTemplate selectFinVoucherTemplateById(Long id);

    /**
     * 查询凭证模板列表
     * 
     * @param finVoucherTemplate 凭证模板
     * @return 凭证模板集合
     */
    public List<FinVoucherTemplate> selectFinVoucherTemplateList(FinVoucherTemplate finVoucherTemplate);

    /**
     * 新增凭证模板
     * 
     * @param finVoucherTemplate 凭证模板
     * @return 结果
     */
    public int insertFinVoucherTemplate(FinVoucherTemplate finVoucherTemplate);

    /**
     * 修改凭证模板
     * 
     * @param finVoucherTemplate 凭证模板
     * @return 结果
     */
    public int updateFinVoucherTemplate(FinVoucherTemplate finVoucherTemplate);

    /**
     * 批量删除凭证模板
     * 
     * @param ids 需要删除的凭证模板主键集合
     * @return 结果
     */
    public int deleteFinVoucherTemplateByIds(Long[] ids);

    /**
     * 删除凭证模板信息
     * 
     * @param id 凭证模板主键
     * @return 结果
     */
    public int deleteFinVoucherTemplateById(Long id);
}
