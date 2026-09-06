package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.FinAuditLog;

/**
 * 审计日志Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-23
 */
public interface FinAuditLogMapper 
{
    /**
     * 查询审计日志
     * 
     * @param id 审计日志主键
     * @return 审计日志
     */
    public FinAuditLog selectFinAuditLogById(Long id);

    /**
     * 查询审计日志列表
     * 
     * @param finAuditLog 审计日志
     * @return 审计日志集合
     */
    public List<FinAuditLog> selectFinAuditLogList(FinAuditLog finAuditLog);

    /**
     * 新增审计日志
     * 
     * @param finAuditLog 审计日志
     * @return 结果
     */
    public int insertFinAuditLog(FinAuditLog finAuditLog);

    /**
     * 修改审计日志
     * 
     * @param finAuditLog 审计日志
     * @return 结果
     */
    public int updateFinAuditLog(FinAuditLog finAuditLog);

    /**
     * 删除审计日志
     * 
     * @param id 审计日志主键
     * @return 结果
     */
    public int deleteFinAuditLogById(Long id);

    /**
     * 批量删除审计日志
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinAuditLogByIds(Long[] ids);
}
