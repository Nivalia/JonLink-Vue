package com.jonlink.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.domain.WxMpCallbackLog;
import com.jonlink.system.mapper.WxMpCallbackLogMapper;
import com.jonlink.system.wx.service.IWxMpCallbackLogService;

/**
 * 公众号回调日志 Service 实现
 * 
 * @author jonlink
 */
@Service
public class WxMpCallbackLogServiceImpl implements IWxMpCallbackLogService
{
    @Autowired
    private WxMpCallbackLogMapper wxMpCallbackLogMapper;

    @Override
    public List<WxMpCallbackLog> selectWxMpCallbackLogList(WxMpCallbackLog wxMpCallbackLog)
    {
        return wxMpCallbackLogMapper.selectWxMpCallbackLogList(wxMpCallbackLog);
    }

    @Override
    public WxMpCallbackLog selectWxMpCallbackLogById(Long id)
    {
        return wxMpCallbackLogMapper.selectWxMpCallbackLogById(id);
    }

    @Override
    public int insertWxMpCallbackLog(WxMpCallbackLog wxMpCallbackLog)
    {
        return wxMpCallbackLogMapper.insertWxMpCallbackLog(wxMpCallbackLog);
    }

    @Override
    public int deleteWxMpCallbackLogById(Long id)
    {
        return wxMpCallbackLogMapper.deleteWxMpCallbackLogById(id);
    }

    @Override
    public int deleteAllWxMpCallbackLog()
    {
        return wxMpCallbackLogMapper.deleteAllWxMpCallbackLog();
    }
}
