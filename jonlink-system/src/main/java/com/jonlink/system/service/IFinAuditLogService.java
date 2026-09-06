package com.jonlink.system.service;

import java.util.Date;
import java.util.List;
import com.jonlink.system.domain.FinAuditLog;

/**
 * 审计日志Service接口
 * 
 * @author jonlink
 * @date 2026-08-23
 */
public interface IFinAuditLogService 
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
     * 批量删除审计日志
     * 
     * @param ids 需要删除的审计日志主键集合
     * @return 结果
     */
    public int deleteFinAuditLogByIds(Long[] ids);

    /**
     * 删除审计日志信息
     * 
     * @param id 审计日志主键
     * @return 结果
     */
    public int deleteFinAuditLogById(Long id);

    /**
     * 记录审计日志
     * 
     * @param module 模块
     * @param action 操作
     * @param targetId 目标ID
     * @param targetNo 目标编号
     * @param detail 详情(JSON)
     * @param operator 操作人
     */
    public void record(String module, String action, Long targetId, String targetNo, String detail, String operator);

    /**
     * 查询审计日志列表(按模块和日期范围)
     * 
     * @param module 模块
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 审计日志集合
     */
    public List<FinAuditLog> list(String module, Date startDate, Date endDate);
}
