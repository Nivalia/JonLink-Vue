package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.WxQrSceneMapper;
import com.jonlink.system.domain.WxQrScene;
import com.jonlink.system.service.IWxQrSceneService;

/**
 * 二维码管理Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@Service
public class WxQrSceneServiceImpl implements IWxQrSceneService 
{
    @Autowired
    private WxQrSceneMapper wxQrSceneMapper;

    /**
     * 查询二维码管理
     * 
     * @param id 二维码管理主键
     * @return 二维码管理
     */
    @Override
    public WxQrScene selectWxQrSceneById(Long id)
    {
        return wxQrSceneMapper.selectWxQrSceneById(id);
    }

    /**
     * 查询二维码管理列表
     * 
     * @param wxQrScene 二维码管理
     * @return 二维码管理
     */
    @Override
    public List<WxQrScene> selectWxQrSceneList(WxQrScene wxQrScene)
    {
        return wxQrSceneMapper.selectWxQrSceneList(wxQrScene);
    }

    /**
     * 新增二维码管理
     * 
     * @param wxQrScene 二维码管理
     * @return 结果
     */
    @Override
    public int insertWxQrScene(WxQrScene wxQrScene)
    {
        wxQrScene.setCreateTime(DateUtils.getNowDate());
        return wxQrSceneMapper.insertWxQrScene(wxQrScene);
    }

    /**
     * 修改二维码管理
     * 
     * @param wxQrScene 二维码管理
     * @return 结果
     */
    @Override
    public int updateWxQrScene(WxQrScene wxQrScene)
    {
        wxQrScene.setUpdateTime(DateUtils.getNowDate());
        return wxQrSceneMapper.updateWxQrScene(wxQrScene);
    }

    /**
     * 批量删除二维码管理
     * 
     * @param ids 需要删除的二维码管理主键
     * @return 结果
     */
    @Override
    public int deleteWxQrSceneByIds(Long[] ids)
    {
        return wxQrSceneMapper.deleteWxQrSceneByIds(ids);
    }

    /**
     * 删除二维码管理信息
     * 
     * @param id 二维码管理主键
     * @return 结果
     */
    @Override
    public int deleteWxQrSceneById(Long id)
    {
        return wxQrSceneMapper.deleteWxQrSceneById(id);
    }
}
