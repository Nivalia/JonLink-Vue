package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.FinPartner;

/**
 * 往来单位Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public interface FinPartnerMapper 
{
    /**
     * 查询往来单位
     * 
     * @param id 往来单位主键
     * @return 往来单位
     */
    public FinPartner selectFinPartnerById(Long id);

    /**
     * 查询往来单位列表
     * 
     * @param finPartner 往来单位
     * @return 往来单位集合
     */
    public List<FinPartner> selectFinPartnerList(FinPartner finPartner);

    /**
     * 新增往来单位
     * 
     * @param finPartner 往来单位
     * @return 结果
     */
    public int insertFinPartner(FinPartner finPartner);

    /**
     * 修改往来单位
     * 
     * @param finPartner 往来单位
     * @return 结果
     */
    public int updateFinPartner(FinPartner finPartner);

    /**
     * 删除往来单位
     * 
     * @param id 往来单位主键
     * @return 结果
     */
    public int deleteFinPartnerById(Long id);

    /**
     * 批量删除往来单位
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinPartnerByIds(Long[] ids);
}
