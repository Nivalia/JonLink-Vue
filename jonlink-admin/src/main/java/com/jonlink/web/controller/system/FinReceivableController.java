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
import com.jonlink.system.domain.FinReceivable;
import com.jonlink.system.service.IFinReceivableService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 应收单Controller
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@RestController
@RequestMapping("/finance/receivable")
public class FinReceivableController extends BaseController
{
    @Autowired
    private IFinReceivableService finReceivableService;

    /**
     * 查询应收单列表
     */
    @PreAuthorize("@ss.hasPermi('finance:receivable:list')")
    @GetMapping("/list")
    public TableDataInfo list(FinReceivable finReceivable)
    {
        startPage();
        List<FinReceivable> list = finReceivableService.selectFinReceivableList(finReceivable);
        return getDataTable(list);
    }

    /**
     * 导出应收单列表
     */
    @PreAuthorize("@ss.hasPermi('finance:receivable:export')")
    @Log(title = "应收单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinReceivable finReceivable)
    {
        List<FinReceivable> list = finReceivableService.selectFinReceivableList(finReceivable);
        ExcelUtil<FinReceivable> util = new ExcelUtil<FinReceivable>(FinReceivable.class);
        util.exportExcel(response, list, "应收单数据");
    }

    /**
     * 获取应收单详细信息
     */
    @PreAuthorize("@ss.hasPermi('finance:receivable:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(finReceivableService.selectFinReceivableById(id));
    }

    /**
     * 新增应收单
     */
    @PreAuthorize("@ss.hasPermi('finance:receivable:add')")
    @Log(title = "应收单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FinReceivable finReceivable)
    {
        return toAjax(finReceivableService.insertFinReceivable(finReceivable));
    }

    /**
     * 修改应收单
     */
    @PreAuthorize("@ss.hasPermi('finance:receivable:edit')")
    @Log(title = "应收单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FinReceivable finReceivable)
    {
        return toAjax(finReceivableService.updateFinReceivable(finReceivable));
    }

    /**
     * 删除应收单
     */
    @PreAuthorize("@ss.hasPermi('finance:receivable:remove')")
    @Log(title = "应收单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(finReceivableService.deleteFinReceivableByIds(ids));
    }
}
