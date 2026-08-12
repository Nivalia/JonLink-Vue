package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.WxQrScanLog;

/**
 * 扫码日志Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public interface WxQrScanLogMapper 
{
    /**
     * 查询扫码日志
     * 
     * @param id 扫码日志主键
     * @return 扫码日志
     */
    public WxQrScanLog selectWxQrScanLogById(Long id);

    /**
     * 查询扫码日志列表
     * 
     * @param wxQrScanLog 扫码日志
     * @return 扫码日志集合
     */
    public List<WxQrScanLog> selectWxQrScanLogList(WxQrScanLog wxQrScanLog);

    /**
     * 新增扫码日志
     * 
     * @param wxQrScanLog 扫码日志
     * @return 结果
     */
    public int insertWxQrScanLog(WxQrScanLog wxQrScanLog);

    /**
     * 修改扫码日志
     * 
     * @param wxQrScanLog 扫码日志
     * @return 结果
     */
    public int updateWxQrScanLog(WxQrScanLog wxQrScanLog);

    /**
     * 删除扫码日志
     * 
     * @param id 扫码日志主键
     * @return 结果
     */
    public int deleteWxQrScanLogById(Long id);

    /**
     * 批量删除扫码日志
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWxQrScanLogByIds(Long[] ids);
}
