package com.jonlink.system.wx.service;

import java.util.List;
import com.jonlink.system.domain.WxMpCallbackLog;

/**
 * 公众号回调日志 Service
 * 
 * @author jonlink
 */
public interface IWxMpCallbackLogService
{
    public List<WxMpCallbackLog> selectWxMpCallbackLogList(WxMpCallbackLog wxMpCallbackLog);

    public WxMpCallbackLog selectWxMpCallbackLogById(Long id);

    public int insertWxMpCallbackLog(WxMpCallbackLog wxMpCallbackLog);

    public int deleteWxMpCallbackLogById(Long id);

    public int deleteAllWxMpCallbackLog();
}
