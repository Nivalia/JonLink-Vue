package com.jonlink.system.service;

import java.util.List;
import com.jonlink.system.domain.WxBizOrder;

/**
 * 核销管理Service接口
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public interface IWxBizOrderService 
{
    /**
     * 查询核销管理
     * 
     * @param id 核销管理主键
     * @return 核销管理
     */
    public WxBizOrder selectWxBizOrderById(Long id);

    /**
     * 查询核销管理列表
     * 
     * @param wxBizOrder 核销管理
     * @return 核销管理集合
     */
    public List<WxBizOrder> selectWxBizOrderList(WxBizOrder wxBizOrder);

    /**
     * 新增核销管理
     * 
     * @param wxBizOrder 核销管理
     * @return 结果
     */
    public int insertWxBizOrder(WxBizOrder wxBizOrder);

    /**
     * 修改核销管理
     * 
     * @param wxBizOrder 核销管理
     * @return 结果
     */
    public int updateWxBizOrder(WxBizOrder wxBizOrder);

    /**
     * 批量删除核销管理
     * 
     * @param ids 需要删除的核销管理主键集合
     * @return 结果
     */
    public int deleteWxBizOrderByIds(Long[] ids);

    /**
     * 删除核销管理信息
     * 
     * @param id 核销管理主键
     * @return 结果
     */
    public int deleteWxBizOrderById(Long id);
}
