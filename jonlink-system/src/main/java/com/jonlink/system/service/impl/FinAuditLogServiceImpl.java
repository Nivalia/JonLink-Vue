package com.jonlink.system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.common.utils.DateUtils;
import com.jonlink.system.mapper.FinAuditLogMapper;
import com.jonlink.system.domain.FinAuditLog;
import com.jonlink.system.service.IFinAuditLogService;

/**
 * 审计日志Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-23
 */
@Service
public class FinAuditLogServiceImpl implements IFinAuditLogService
{
    @Autowired
    private FinAuditLogMapper finAuditLogMapper;

    /**
     * 查询审计日志
     * 
     * @param id 审计日志主键
     * @return 审计日志
     */
    @Override
    public FinAuditLog selectFinAuditLogById(Long id)
    {
        return finAuditLogMapper.selectFinAuditLogById(id);
    }

    /**
     * 查询审计日志列表
     * 
     * @param finAuditLog 审计日志
     * @return 审计日志
     */
    @Override
    public List<FinAuditLog> selectFinAuditLogList(FinAuditLog finAuditLog)
    {
        return finAuditLogMapper.selectFinAuditLogList(finAuditLog);
    }

    /**
     * 新增审计日志
     * 
     * @param finAuditLog 审计日志
     * @return 结果
     */
    @Override
    public int insertFinAuditLog(FinAuditLog finAuditLog)
    {
        finAuditLog.setCreateTime(DateUtils.getNowDate());
        return finAuditLogMapper.insertFinAuditLog(finAuditLog);
    }

    /**
     * 修改审计日志
     * 
     * @param finAuditLog 审计日志
     * @return 结果
     */
    @Override
    public int updateFinAuditLog(FinAuditLog finAuditLog)
    {
        finAuditLog.setUpdateTime(DateUtils.getNowDate());
        return finAuditLogMapper.updateFinAuditLog(finAuditLog);
    }

    /**
     * 批量删除审计日志
     * 
     * @param ids 需要删除的审计日志主键
     * @return 结果
     */
    @Override
    public int deleteFinAuditLogByIds(Long[] ids)
    {
        return finAuditLogMapper.deleteFinAuditLogByIds(ids);
    }

    /**
     * 删除审计日志信息
     * 
     * @param id 审计日志主键
     * @return 结果
     */
    @Override
    public int deleteFinAuditLogById(Long id)
    {
        return finAuditLogMapper.deleteFinAuditLogById(id);
    }

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
    @Override
    public void record(String module, String action, Long targetId, String targetNo, String detail, String operator)
    {
        FinAuditLog log = new FinAuditLog();
        log.setModule(module);
        log.setAction(action);
        log.setTargetId(targetId);
        log.setTargetNo(targetNo);
        log.setDetail(detail);
        log.setOperator(operator);
        log.setOperateTime(new Date());
        insertFinAuditLog(log);
    }

    /**
     * 查询审计日志列表(按模块和日期范围)
     * 
     * @param module 模块
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 审计日志集合
     */
    @Override
    public List<FinAuditLog> list(String module, Date startDate, Date endDate)
    {
        FinAuditLog query = new FinAuditLog();
        query.setModule(module);
        if (startDate != null || endDate != null) {
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            if (startDate != null) {
                params.put("beginTime", new java.text.SimpleDateFormat("yyyy-MM-dd").format(startDate));
            }
            if (endDate != null) {
                params.put("endTime", new java.text.SimpleDateFormat("yyyy-MM-dd").format(endDate));
            }
            query.setParams(params);
        }
        return finAuditLogMapper.selectFinAuditLogList(query);
    }
}
