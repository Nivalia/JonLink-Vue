package com.jonlink.web.controller.system;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jonlink.common.annotation.Log;
import com.jonlink.common.core.controller.BaseController;
import com.jonlink.common.core.domain.AjaxResult;
import com.jonlink.common.enums.BusinessType;
import com.jonlink.system.domain.FinAuditLog;
import com.jonlink.system.service.IFinAuditLogService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 审计日志Controller
 * 
 * @author jonlink
 * @date 2026-08-23
 */
@RestController
@RequestMapping("/system/auditLog")
public class FinAuditLogController extends BaseController
{
    @Autowired
    private IFinAuditLogService finAuditLogService;

    /**
     * 查询审计日志列表
     */
    @PreAuthorize("@ss.hasPermi('system:auditLog:list')")
    @GetMapping("/list")
    public TableDataInfo list(FinAuditLog finAuditLog)
    {
        startPage();
        List<FinAuditLog> list = finAuditLogService.selectFinAuditLogList(finAuditLog);
        return getDataTable(list);
    }

    /**
     * 导出审计日志列表
     */
    @PreAuthorize("@ss.hasPermi('system:auditLog:export')")
    @Log(title = "审计日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinAuditLog finAuditLog)
    {
        List<FinAuditLog> list = finAuditLogService.selectFinAuditLogList(finAuditLog);
        ExcelUtil<FinAuditLog> util = new ExcelUtil<FinAuditLog>(FinAuditLog.class);
        util.exportExcel(response, list, "审计日志数据");
    }

    /**
     * 获取审计日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:auditLog:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(finAuditLogService.selectFinAuditLogById(id));
    }

    /**
     * 删除审计日志
     */
    @PreAuthorize("@ss.hasPermi('system:auditLog:remove')")
    @Log(title = "审计日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(finAuditLogService.deleteFinAuditLogByIds(ids));
    }
}
