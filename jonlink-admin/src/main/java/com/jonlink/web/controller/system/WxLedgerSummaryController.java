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
import com.jonlink.system.domain.WxLedgerSummary;
import com.jonlink.system.service.IWxLedgerSummaryService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 台账日汇总Controller
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@RestController
@RequestMapping("/ledger/wxLedgerSummary")
public class WxLedgerSummaryController extends BaseController
{
    @Autowired
    private IWxLedgerSummaryService wxLedgerSummaryService;

    /**
     * 查询台账日汇总列表
     */
    @PreAuthorize("@ss.hasPermi('ledger:wxLedgerSummary:list')")
    @GetMapping("/list")
    public TableDataInfo list(WxLedgerSummary wxLedgerSummary)
    {
        startPage();
        List<WxLedgerSummary> list = wxLedgerSummaryService.selectWxLedgerSummaryList(wxLedgerSummary);
        return getDataTable(list);
    }

    /**
     * 导出台账日汇总列表
     */
    @PreAuthorize("@ss.hasPermi('ledger:wxLedgerSummary:export')")
    @Log(title = "台账日汇总", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WxLedgerSummary wxLedgerSummary)
    {
        List<WxLedgerSummary> list = wxLedgerSummaryService.selectWxLedgerSummaryList(wxLedgerSummary);
        ExcelUtil<WxLedgerSummary> util = new ExcelUtil<WxLedgerSummary>(WxLedgerSummary.class);
        util.exportExcel(response, list, "台账日汇总数据");
    }

    /**
     * 获取台账日汇总详细信息
     */
    @PreAuthorize("@ss.hasPermi('ledger:wxLedgerSummary:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wxLedgerSummaryService.selectWxLedgerSummaryById(id));
    }

    /**
     * 新增台账日汇总
     */
    @PreAuthorize("@ss.hasPermi('ledger:wxLedgerSummary:add')")
    @Log(title = "台账日汇总", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WxLedgerSummary wxLedgerSummary)
    {
        return toAjax(wxLedgerSummaryService.insertWxLedgerSummary(wxLedgerSummary));
    }

    /**
     * 修改台账日汇总
     */
    @PreAuthorize("@ss.hasPermi('ledger:wxLedgerSummary:edit')")
    @Log(title = "台账日汇总", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WxLedgerSummary wxLedgerSummary)
    {
        return toAjax(wxLedgerSummaryService.updateWxLedgerSummary(wxLedgerSummary));
    }

    /**
     * 删除台账日汇总
     */
    @PreAuthorize("@ss.hasPermi('ledger:wxLedgerSummary:remove')")
    @Log(title = "台账日汇总", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wxLedgerSummaryService.deleteWxLedgerSummaryByIds(ids));
    }
}
