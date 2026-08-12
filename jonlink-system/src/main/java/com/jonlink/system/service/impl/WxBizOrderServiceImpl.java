package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.WxBizOrderMapper;
import com.jonlink.system.domain.WxBizOrder;
import com.jonlink.system.service.IWxBizOrderService;

/**
 * 核销管理Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@Service
public class WxBizOrderServiceImpl implements IWxBizOrderService 
{
    @Autowired
    private WxBizOrderMapper wxBizOrderMapper;

    /**
     * 查询核销管理
     * 
     * @param id 核销管理主键
     * @return 核销管理
     */
    @Override
    public WxBizOrder selectWxBizOrderById(Long id)
    {
        return wxBizOrderMapper.selectWxBizOrderById(id);
    }

    /**
     * 查询核销管理列表
     * 
     * @param wxBizOrder 核销管理
     * @return 核销管理
     */
    @Override
    public List<WxBizOrder> selectWxBizOrderList(WxBizOrder wxBizOrder)
    {
        return wxBizOrderMapper.selectWxBizOrderList(wxBizOrder);
    }

    /**
     * 新增核销管理
     * 
     * @param wxBizOrder 核销管理
     * @return 结果
     */
    @Override
    public int insertWxBizOrder(WxBizOrder wxBizOrder)
    {
        wxBizOrder.setCreateTime(DateUtils.getNowDate());
        return wxBizOrderMapper.insertWxBizOrder(wxBizOrder);
    }

    /**
     * 修改核销管理
     * 
     * @param wxBizOrder 核销管理
     * @return 结果
     */
    @Override
    public int updateWxBizOrder(WxBizOrder wxBizOrder)
    {
        wxBizOrder.setUpdateTime(DateUtils.getNowDate());
        return wxBizOrderMapper.updateWxBizOrder(wxBizOrder);
    }

    /**
     * 批量删除核销管理
     * 
     * @param ids 需要删除的核销管理主键
     * @return 结果
     */
    @Override
    public int deleteWxBizOrderByIds(Long[] ids)
    {
        return wxBizOrderMapper.deleteWxBizOrderByIds(ids);
    }

    /**
     * 删除核销管理信息
     * 
     * @param id 核销管理主键
     * @return 结果
     */
    @Override
    public int deleteWxBizOrderById(Long id)
    {
        return wxBizOrderMapper.deleteWxBizOrderById(id);
    }
}
