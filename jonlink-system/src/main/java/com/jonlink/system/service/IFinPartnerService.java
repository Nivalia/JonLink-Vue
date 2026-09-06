package com.jonlink.system.service;

import java.util.List;
import com.jonlink.system.domain.FinPartner;

/**
 * 往来单位Service接口
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public interface IFinPartnerService 
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
     * 批量删除往来单位
     * 
     * @param ids 需要删除的往来单位主键集合
     * @return 结果
     */
    public int deleteFinPartnerByIds(Long[] ids);

    /**
     * 删除往来单位信息
     * 
     * @param id 往来单位主键
     * @return 结果
     */
    public int deleteFinPartnerById(Long id);

    /** 按 name 精确查 */
    public FinPartner selectByName(String partnerName);

    /** 自动建档(按 name),返回 partner_id */
    public Long upsertByName(String partnerName, String partnerType, String refTable, Long refId, String operator);
}
