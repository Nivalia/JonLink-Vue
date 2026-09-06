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
import com.jonlink.system.domain.FinVoucherEntry;
import com.jonlink.system.service.IFinVoucherEntryService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 凭证分录Controller
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@RestController
@RequestMapping("/finance/entry")
public class FinVoucherEntryController extends BaseController
{
    @Autowired
    private IFinVoucherEntryService finVoucherEntryService;

    /**
     * 查询凭证分录列表
     */
    @PreAuthorize("@ss.hasPermi('finance:entry:list')")
    @GetMapping("/list")
    public TableDataInfo list(FinVoucherEntry finVoucherEntry)
    {
        startPage();
        List<FinVoucherEntry> list = finVoucherEntryService.selectFinVoucherEntryList(finVoucherEntry);
        return getDataTable(list);
    }

    /**
     * 导出凭证分录列表
     */
    @PreAuthorize("@ss.hasPermi('finance:entry:export')")
    @Log(title = "凭证分录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinVoucherEntry finVoucherEntry)
    {
        List<FinVoucherEntry> list = finVoucherEntryService.selectFinVoucherEntryList(finVoucherEntry);
        ExcelUtil<FinVoucherEntry> util = new ExcelUtil<FinVoucherEntry>(FinVoucherEntry.class);
        util.exportExcel(response, list, "凭证分录数据");
    }

    /**
     * 获取凭证分录详细信息
     */
    @PreAuthorize("@ss.hasPermi('finance:entry:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(finVoucherEntryService.selectFinVoucherEntryById(id));
    }

    /**
     * 新增凭证分录
     */
    @PreAuthorize("@ss.hasPermi('finance:entry:add')")
    @Log(title = "凭证分录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FinVoucherEntry finVoucherEntry)
    {
        return toAjax(finVoucherEntryService.insertFinVoucherEntry(finVoucherEntry));
    }

    /**
     * 修改凭证分录
     */
    @PreAuthorize("@ss.hasPermi('finance:entry:edit')")
    @Log(title = "凭证分录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FinVoucherEntry finVoucherEntry)
    {
        return toAjax(finVoucherEntryService.updateFinVoucherEntry(finVoucherEntry));
    }

    /**
     * 删除凭证分录
     */
    @PreAuthorize("@ss.hasPermi('finance:entry:remove')")
    @Log(title = "凭证分录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(finVoucherEntryService.deleteFinVoucherEntryByIds(ids));
    }
}
