package com.jonlink.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.WxQrScanLogMapper;
import com.jonlink.system.domain.WxQrScanLog;
import com.jonlink.system.service.IWxQrScanLogService;

/**
 * 扫码日志Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@Service
public class WxQrScanLogServiceImpl implements IWxQrScanLogService 
{
    @Autowired
    private WxQrScanLogMapper wxQrScanLogMapper;

    /**
     * 查询扫码日志
     * 
     * @param id 扫码日志主键
     * @return 扫码日志
     */
    @Override
    public WxQrScanLog selectWxQrScanLogById(Long id)
    {
        return wxQrScanLogMapper.selectWxQrScanLogById(id);
    }

    /**
     * 查询扫码日志列表
     * 
     * @param wxQrScanLog 扫码日志
     * @return 扫码日志
     */
    @Override
    public List<WxQrScanLog> selectWxQrScanLogList(WxQrScanLog wxQrScanLog)
    {
        return wxQrScanLogMapper.selectWxQrScanLogList(wxQrScanLog);
    }

    /**
     * 新增扫码日志
     * 
     * @param wxQrScanLog 扫码日志
     * @return 结果
     */
    @Override
    public int insertWxQrScanLog(WxQrScanLog wxQrScanLog)
    {
        return wxQrScanLogMapper.insertWxQrScanLog(wxQrScanLog);
    }

    /**
     * 修改扫码日志
     * 
     * @param wxQrScanLog 扫码日志
     * @return 结果
     */
    @Override
    public int updateWxQrScanLog(WxQrScanLog wxQrScanLog)
    {
        return wxQrScanLogMapper.updateWxQrScanLog(wxQrScanLog);
    }

    /**
     * 批量删除扫码日志
     * 
     * @param ids 需要删除的扫码日志主键
     * @return 结果
     */
    @Override
    public int deleteWxQrScanLogByIds(Long[] ids)
    {
        return wxQrScanLogMapper.deleteWxQrScanLogByIds(ids);
    }

    /**
     * 删除扫码日志信息
     * 
     * @param id 扫码日志主键
     * @return 结果
     */
    @Override
    public int deleteWxQrScanLogById(Long id)
    {
        return wxQrScanLogMapper.deleteWxQrScanLogById(id);
    }
}
