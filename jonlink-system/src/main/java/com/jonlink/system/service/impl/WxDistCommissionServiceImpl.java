package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.WxDistCommissionMapper;
import com.jonlink.system.domain.WxDistCommission;
import com.jonlink.system.service.IWxDistCommissionService;

/**
 * 佣金积分Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@Service
public class WxDistCommissionServiceImpl implements IWxDistCommissionService 
{
    @Autowired
    private WxDistCommissionMapper wxDistCommissionMapper;

    /**
     * 查询佣金积分
     * 
     * @param id 佣金积分主键
     * @return 佣金积分
     */
    @Override
    public WxDistCommission selectWxDistCommissionById(Long id)
    {
        return wxDistCommissionMapper.selectWxDistCommissionById(id);
    }

    /**
     * 查询佣金积分列表
     * 
     * @param wxDistCommission 佣金积分
     * @return 佣金积分
     */
    @Override
    public List<WxDistCommission> selectWxDistCommissionList(WxDistCommission wxDistCommission)
    {
        return wxDistCommissionMapper.selectWxDistCommissionList(wxDistCommission);
    }

    /**
     * 新增佣金积分
     * 
     * @param wxDistCommission 佣金积分
     * @return 结果
     */
    @Override
    public int insertWxDistCommission(WxDistCommission wxDistCommission)
    {
        wxDistCommission.setCreateTime(DateUtils.getNowDate());
        return wxDistCommissionMapper.insertWxDistCommission(wxDistCommission);
    }

    /**
     * 修改佣金积分
     * 
     * @param wxDistCommission 佣金积分
     * @return 结果
     */
    @Override
    public int updateWxDistCommission(WxDistCommission wxDistCommission)
    {
        wxDistCommission.setUpdateTime(DateUtils.getNowDate());
        return wxDistCommissionMapper.updateWxDistCommission(wxDistCommission);
    }

    /**
     * 批量删除佣金积分
     * 
     * @param ids 需要删除的佣金积分主键
     * @return 结果
     */
    @Override
    public int deleteWxDistCommissionByIds(Long[] ids)
    {
        return wxDistCommissionMapper.deleteWxDistCommissionByIds(ids);
    }

    /**
     * 删除佣金积分信息
     * 
     * @param id 佣金积分主键
     * @return 结果
     */
    @Override
    public int deleteWxDistCommissionById(Long id)
    {
        return wxDistCommissionMapper.deleteWxDistCommissionById(id);
    }
}
