package com.jonlink.system.service;

import java.util.List;
import com.jonlink.system.domain.WxQrScanLog;

/**
 * 扫码日志Service接口
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public interface IWxQrScanLogService 
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
     * 批量删除扫码日志
     * 
     * @param ids 需要删除的扫码日志主键集合
     * @return 结果
     */
    public int deleteWxQrScanLogByIds(Long[] ids);

    /**
     * 删除扫码日志信息
     * 
     * @param id 扫码日志主键
     * @return 结果
     */
    public int deleteWxQrScanLogById(Long id);
}
