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
import com.jonlink.system.domain.FinPeriod;
import com.jonlink.system.service.IFinPeriodService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 会计期间Controller
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@RestController
@RequestMapping("/finance/period")
public class FinPeriodController extends BaseController
{
    @Autowired
    private IFinPeriodService finPeriodService;

    /**
     * 查询会计期间列表
     */
    @PreAuthorize("@ss.hasPermi('finance:period:list')")
    @GetMapping("/list")
    public TableDataInfo list(FinPeriod finPeriod)
    {
        startPage();
        List<FinPeriod> list = finPeriodService.selectFinPeriodList(finPeriod);
        return getDataTable(list);
    }

    /**
     * 导出会计期间列表
     */
    @PreAuthorize("@ss.hasPermi('finance:period:export')")
    @Log(title = "会计期间", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinPeriod finPeriod)
    {
        List<FinPeriod> list = finPeriodService.selectFinPeriodList(finPeriod);
        ExcelUtil<FinPeriod> util = new ExcelUtil<FinPeriod>(FinPeriod.class);
        util.exportExcel(response, list, "会计期间数据");
    }

    /**
     * 获取会计期间详细信息
     */
    @PreAuthorize("@ss.hasPermi('finance:period:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(finPeriodService.selectFinPeriodById(id));
    }

    /**
     * 新增会计期间
     */
    @PreAuthorize("@ss.hasPermi('finance:period:add')")
    @Log(title = "会计期间", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FinPeriod finPeriod)
    {
        return toAjax(finPeriodService.insertFinPeriod(finPeriod));
    }

    /**
     * 修改会计期间
     */
    @PreAuthorize("@ss.hasPermi('finance:period:edit')")
    @Log(title = "会计期间", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FinPeriod finPeriod)
    {
        return toAjax(finPeriodService.updateFinPeriod(finPeriod));
    }

    /**
     * 删除会计期间
     */
    @PreAuthorize("@ss.hasPermi('finance:period:remove')")
    @Log(title = "会计期间", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(finPeriodService.deleteFinPeriodByIds(ids));
    }

    /**
     * 期末结账
     */
    @PreAuthorize("@ss.hasPermi('finance:period:close')")
    @Log(title = "会计期间-结账", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/close")
    public AjaxResult close(@PathVariable("id") Long id)
    {
        finPeriodService.closePeriod(id, com.jonlink.common.utils.SecurityUtils.getUsername());
        return success();
    }

    /**
     * 反结账
     */
    @PreAuthorize("@ss.hasPermi('finance:period:close')")
    @Log(title = "会计期间-反结账", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/reopen")
    public AjaxResult reopen(@PathVariable("id") Long id)
    {
        finPeriodService.reopenPeriod(id, com.jonlink.common.utils.SecurityUtils.getUsername());
        return success();
    }
}
