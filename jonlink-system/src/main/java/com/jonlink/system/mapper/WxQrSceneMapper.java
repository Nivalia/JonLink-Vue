package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.WxQrScene;

/**
 * 二维码管理Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public interface WxQrSceneMapper 
{
    /**
     * 查询二维码管理
     * 
     * @param id 二维码管理主键
     * @return 二维码管理
     */
    public WxQrScene selectWxQrSceneById(Long id);

    /**
     * 查询二维码管理列表
     * 
     * @param wxQrScene 二维码管理
     * @return 二维码管理集合
     */
    public List<WxQrScene> selectWxQrSceneList(WxQrScene wxQrScene);

    /**
     * 新增二维码管理
     * 
     * @param wxQrScene 二维码管理
     * @return 结果
     */
    public int insertWxQrScene(WxQrScene wxQrScene);

    /**
     * 修改二维码管理
     * 
     * @param wxQrScene 二维码管理
     * @return 结果
     */
    public int updateWxQrScene(WxQrScene wxQrScene);

    /**
     * 删除二维码管理
     * 
     * @param id 二维码管理主键
     * @return 结果
     */
    public int deleteWxQrSceneById(Long id);

    /**
     * 批量删除二维码管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWxQrSceneByIds(Long[] ids);
}
