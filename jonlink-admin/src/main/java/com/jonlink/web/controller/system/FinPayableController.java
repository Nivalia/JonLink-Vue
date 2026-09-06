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
import com.jonlink.system.domain.FinPayable;
import com.jonlink.system.service.IFinPayableService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 应付单Controller
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@RestController
@RequestMapping("/finance/payable")
public class FinPayableController extends BaseController
{
    @Autowired
    private IFinPayableService finPayableService;

    /**
     * 查询应付单列表
     */
    @PreAuthorize("@ss.hasPermi('finance:payable:list')")
    @GetMapping("/list")
    public TableDataInfo list(FinPayable finPayable)
    {
        startPage();
        List<FinPayable> list = finPayableService.selectFinPayableList(finPayable);
        return getDataTable(list);
    }

    /**
     * 导出应付单列表
     */
    @PreAuthorize("@ss.hasPermi('finance:payable:export')")
    @Log(title = "应付单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinPayable finPayable)
    {
        List<FinPayable> list = finPayableService.selectFinPayableList(finPayable);
        ExcelUtil<FinPayable> util = new ExcelUtil<FinPayable>(FinPayable.class);
        util.exportExcel(response, list, "应付单数据");
    }

    /**
     * 获取应付单详细信息
     */
    @PreAuthorize("@ss.hasPermi('finance:payable:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(finPayableService.selectFinPayableById(id));
    }

    /**
     * 新增应付单
     */
    @PreAuthorize("@ss.hasPermi('finance:payable:add')")
    @Log(title = "应付单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FinPayable finPayable)
    {
        return toAjax(finPayableService.insertFinPayable(finPayable));
    }

    /**
     * 修改应付单
     */
    @PreAuthorize("@ss.hasPermi('finance:payable:edit')")
    @Log(title = "应付单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FinPayable finPayable)
    {
        return toAjax(finPayableService.updateFinPayable(finPayable));
    }

    /**
     * 删除应付单
     */
    @PreAuthorize("@ss.hasPermi('finance:payable:remove')")
    @Log(title = "应付单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(finPayableService.deleteFinPayableByIds(ids));
    }
}
