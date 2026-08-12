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
import com.jonlink.system.domain.WxQrScanLog;
import com.jonlink.system.service.IWxQrScanLogService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 扫码日志Controller
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@RestController
@RequestMapping("/wx/qrScanLog")
public class WxQrScanLogController extends BaseController
{
    @Autowired
    private IWxQrScanLogService wxQrScanLogService;

    /**
     * 查询扫码日志列表
     */
    @PreAuthorize("@ss.hasPermi('wx:qrScanLog:list')")
    @GetMapping("/list")
    public TableDataInfo list(WxQrScanLog wxQrScanLog)
    {
        startPage();
        List<WxQrScanLog> list = wxQrScanLogService.selectWxQrScanLogList(wxQrScanLog);
        return getDataTable(list);
    }

    /**
     * 导出扫码日志列表
     */
    @PreAuthorize("@ss.hasPermi('wx:qrScanLog:export')")
    @Log(title = "扫码日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WxQrScanLog wxQrScanLog)
    {
        List<WxQrScanLog> list = wxQrScanLogService.selectWxQrScanLogList(wxQrScanLog);
        ExcelUtil<WxQrScanLog> util = new ExcelUtil<WxQrScanLog>(WxQrScanLog.class);
        util.exportExcel(response, list, "扫码日志数据");
    }

    /**
     * 获取扫码日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('wx:qrScanLog:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wxQrScanLogService.selectWxQrScanLogById(id));
    }

    /**
     * 新增扫码日志
     */
    @PreAuthorize("@ss.hasPermi('wx:qrScanLog:add')")
    @Log(title = "扫码日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WxQrScanLog wxQrScanLog)
    {
        return toAjax(wxQrScanLogService.insertWxQrScanLog(wxQrScanLog));
    }

    /**
     * 修改扫码日志
     */
    @PreAuthorize("@ss.hasPermi('wx:qrScanLog:edit')")
    @Log(title = "扫码日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WxQrScanLog wxQrScanLog)
    {
        return toAjax(wxQrScanLogService.updateWxQrScanLog(wxQrScanLog));
    }

    /**
     * 删除扫码日志
     */
    @PreAuthorize("@ss.hasPermi('wx:qrScanLog:remove')")
    @Log(title = "扫码日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wxQrScanLogService.deleteWxQrScanLogByIds(ids));
    }
}
