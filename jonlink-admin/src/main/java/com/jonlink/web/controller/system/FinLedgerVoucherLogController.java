package com.jonlink.web.controller.system;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jonlink.common.annotation.Log;
import com.jonlink.common.core.controller.BaseController;
import com.jonlink.common.core.domain.AjaxResult;
import com.jonlink.common.enums.BusinessType;
import com.jonlink.system.domain.FinLedgerVoucherLog;
import com.jonlink.system.service.IFinLedgerVoucherLogService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 台账记账日志Controller
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@RestController
@RequestMapping("/finance/log")
public class FinLedgerVoucherLogController extends BaseController
{
    @Autowired
    private IFinLedgerVoucherLogService finLedgerVoucherLogService;

    /**
     * 查询台账记账日志列表
     */
    @PreAuthorize("@ss.hasPermi('finance:log:list')")
    @GetMapping("/list")
    public TableDataInfo list(FinLedgerVoucherLog finLedgerVoucherLog)
    {
        startPage();
        List<FinLedgerVoucherLog> list = finLedgerVoucherLogService.selectFinLedgerVoucherLogList(finLedgerVoucherLog);
        return getDataTable(list);
    }

    /**
     * 导出台账记账日志列表
     */
    @PreAuthorize("@ss.hasPermi('finance:log:export')")
    @Log(title = "台账记账日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinLedgerVoucherLog finLedgerVoucherLog)
    {
        List<FinLedgerVoucherLog> list = finLedgerVoucherLogService.selectFinLedgerVoucherLogList(finLedgerVoucherLog);
        ExcelUtil<FinLedgerVoucherLog> util = new ExcelUtil<FinLedgerVoucherLog>(FinLedgerVoucherLog.class);
        util.exportExcel(response, list, "台账记账日志数据");
    }

    /**
     * 获取台账记账日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('finance:log:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(finLedgerVoucherLogService.selectFinLedgerVoucherLogById(id));
    }

    /**
     * 新增台账记账日志
     */
    @PreAuthorize("@ss.hasPermi('finance:log:add')")
    @Log(title = "台账记账日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FinLedgerVoucherLog finLedgerVoucherLog)
    {
        return toAjax(finLedgerVoucherLogService.insertFinLedgerVoucherLog(finLedgerVoucherLog));
    }

    /**
     * 修改台账记账日志
     */
    @PreAuthorize("@ss.hasPermi('finance:log:edit')")
    @Log(title = "台账记账日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FinLedgerVoucherLog finLedgerVoucherLog)
    {
        return toAjax(finLedgerVoucherLogService.updateFinLedgerVoucherLog(finLedgerVoucherLog));
    }

    /**
     * 删除台账记账日志
     */
    @PreAuthorize("@ss.hasPermi('finance:log:remove')")
    @Log(title = "台账记账日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(finLedgerVoucherLogService.deleteFinLedgerVoucherLogByIds(ids));
    }
}
