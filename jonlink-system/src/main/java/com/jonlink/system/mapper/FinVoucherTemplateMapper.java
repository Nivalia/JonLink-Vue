package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.FinVoucherTemplate;

/**
 * 凭证模板Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public interface FinVoucherTemplateMapper 
{
    /**
     * 查询凭证模板
     * 
     * @param id 凭证模板主键
     * @return 凭证模板
     */
    public FinVoucherTemplate selectFinVoucherTemplateById(Long id);

    /** 按 template_code 查 */
    public FinVoucherTemplate selectFinVoucherTemplateByCode(String templateCode);

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
     * 删除凭证模板
     * 
     * @param id 凭证模板主键
     * @return 结果
     */
    public int deleteFinVoucherTemplateById(Long id);

    /**
     * 批量删除凭证模板
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinVoucherTemplateByIds(Long[] ids);
}
