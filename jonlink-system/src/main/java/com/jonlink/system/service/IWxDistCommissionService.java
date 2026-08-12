package com.jonlink.system.service;

import java.util.List;
import com.jonlink.system.domain.WxDistCommission;

/**
 * 佣金积分Service接口
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public interface IWxDistCommissionService 
{
    /**
     * 查询佣金积分
     * 
     * @param id 佣金积分主键
     * @return 佣金积分
     */
    public WxDistCommission selectWxDistCommissionById(Long id);

    /**
     * 查询佣金积分列表
     * 
     * @param wxDistCommission 佣金积分
     * @return 佣金积分集合
     */
    public List<WxDistCommission> selectWxDistCommissionList(WxDistCommission wxDistCommission);

    /**
     * 新增佣金积分
     * 
     * @param wxDistCommission 佣金积分
     * @return 结果
     */
    public int insertWxDistCommission(WxDistCommission wxDistCommission);

    /**
     * 修改佣金积分
     * 
     * @param wxDistCommission 佣金积分
     * @return 结果
     */
    public int updateWxDistCommission(WxDistCommission wxDistCommission);

    /**
     * 批量删除佣金积分
     * 
     * @param ids 需要删除的佣金积分主键集合
     * @return 结果
     */
    public int deleteWxDistCommissionByIds(Long[] ids);

    /**
     * 删除佣金积分信息
     * 
     * @param id 佣金积分主键
     * @return 结果
     */
    public int deleteWxDistCommissionById(Long id);
}
