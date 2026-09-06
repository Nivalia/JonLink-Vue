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
import com.jonlink.system.domain.FinAllocation;
import com.jonlink.system.service.IFinAllocationService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 核销记录Controller
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@RestController
@RequestMapping("/finance/allocation")
public class FinAllocationController extends BaseController
{
    @Autowired
    private IFinAllocationService finAllocationService;

    /**
     * 查询核销记录列表
     */
    @PreAuthorize("@ss.hasPermi('finance:allocation:list')")
    @GetMapping("/list")
    public TableDataInfo list(FinAllocation finAllocation)
    {
        startPage();
        List<FinAllocation> list = finAllocationService.selectFinAllocationList(finAllocation);
        return getDataTable(list);
    }

    /**
     * 导出核销记录列表
     */
    @PreAuthorize("@ss.hasPermi('finance:allocation:export')")
    @Log(title = "核销记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinAllocation finAllocation)
    {
        List<FinAllocation> list = finAllocationService.selectFinAllocationList(finAllocation);
        ExcelUtil<FinAllocation> util = new ExcelUtil<FinAllocation>(FinAllocation.class);
        util.exportExcel(response, list, "核销记录数据");
    }

    /**
     * 获取核销记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('finance:allocation:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(finAllocationService.selectFinAllocationById(id));
    }

    /**
     * 新增核销记录
     */
    @PreAuthorize("@ss.hasPermi('finance:allocation:add')")
    @Log(title = "核销记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FinAllocation finAllocation)
    {
        return toAjax(finAllocationService.insertFinAllocation(finAllocation));
    }

    /**
     * 修改核销记录
     */
    @PreAuthorize("@ss.hasPermi('finance:allocation:edit')")
    @Log(title = "核销记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FinAllocation finAllocation)
    {
        return toAjax(finAllocationService.updateFinAllocation(finAllocation));
    }

    /**
     * 删除核销记录
     */
    @PreAuthorize("@ss.hasPermi('finance:allocation:remove')")
    @Log(title = "核销记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(finAllocationService.deleteFinAllocationByIds(ids));
    }
}
