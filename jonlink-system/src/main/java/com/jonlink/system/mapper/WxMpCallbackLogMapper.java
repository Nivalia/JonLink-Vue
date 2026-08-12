package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.WxMpCallbackLog;

/**
 * 公众号回调日志 Mapper
 * 
 * @author jonlink
 */
public interface WxMpCallbackLogMapper
{
    /** 分页查询(按时间倒序) */
    public List<WxMpCallbackLog> selectWxMpCallbackLogList(WxMpCallbackLog wxMpCallbackLog);

    /** 按主键查询 */
    public WxMpCallbackLog selectWxMpCallbackLogById(Long id);

    /** 插入日志 */
    public int insertWxMpCallbackLog(WxMpCallbackLog wxMpCallbackLog);

    /** 按主键删除 */
    public int deleteWxMpCallbackLogById(Long id);

    /** 清空全部日志 */
    public int deleteAllWxMpCallbackLog();
}
