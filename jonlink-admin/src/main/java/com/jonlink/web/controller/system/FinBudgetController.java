package com.jonlink.web.controller.system;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.jonlink.common.annotation.Log;
import com.jonlink.common.core.controller.BaseController;
import com.jonlink.common.core.domain.AjaxResult;
import com.jonlink.common.enums.BusinessType;
import com.jonlink.system.domain.FinBudget;
import com.jonlink.system.service.IFinBudgetService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 预算管理Controller
 * 
 * @author jonlink
 * @date 2026-08-23
 */
@RestController
@RequestMapping("/finance/budget")
public class FinBudgetController extends BaseController
{
    @Autowired
    private IFinBudgetService finBudgetService;

    /**
     * 查询预算管理列表
     */
    @PreAuthorize("@ss.hasPermi('finance:budget:list')")
    @GetMapping("/list")
    public TableDataInfo list(FinBudget finBudget)
    {
        startPage();
        List<FinBudget> list = finBudgetService.selectFinBudgetList(finBudget);
        return getDataTable(list);
    }

    /**
     * 导出预算管理列表
     */
    @PreAuthorize("@ss.hasPermi('finance:budget:export')")
    @Log(title = "预算管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinBudget finBudget)
    {
        List<FinBudget> list = finBudgetService.selectFinBudgetList(finBudget);
        ExcelUtil<FinBudget> util = new ExcelUtil<FinBudget>(FinBudget.class);
        util.exportExcel(response, list, "预算管理数据");
    }

    /**
     * 获取预算管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('finance:budget:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(finBudgetService.selectFinBudgetById(id));
    }

    /**
     * 新增预算管理
     */
    @PreAuthorize("@ss.hasPermi('finance:budget:add')")
    @Log(title = "预算管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FinBudget finBudget)
    {
        return toAjax(finBudgetService.insertFinBudget(finBudget));
    }

    /**
     * 修改预算管理
     */
    @PreAuthorize("@ss.hasPermi('finance:budget:edit')")
    @Log(title = "预算管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FinBudget finBudget)
    {
        return toAjax(finBudgetService.updateFinBudget(finBudget));
    }

    /**
     * 删除预算管理
     */
    @PreAuthorize("@ss.hasPermi('finance:budget:remove')")
    @Log(title = "预算管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(finBudgetService.deleteFinBudgetByIds(ids));
    }

    /**
     * 检查是否超预算
     */
    @PreAuthorize("@ss.hasPermi('finance:budget:query')")
    @GetMapping("/check")
    public AjaxResult checkBudget(@RequestParam String subjectCode, 
                                  @RequestParam String periodCode, 
                                  @RequestParam BigDecimal amount)
    {
        boolean isOver = finBudgetService.checkBudget(subjectCode, periodCode, amount);
        return AjaxResult.success("isOver", isOver);
    }

    /**
     * 获取预算执行报告
     */
    @PreAuthorize("@ss.hasPermi('finance:budget:query')")
    @GetMapping("/report")
    public AjaxResult getBudgetReport(@RequestParam String periodCode)
    {
        Map<String, Object> report = finBudgetService.getBudgetReport(periodCode);
        return AjaxResult.success(report);
    }
}
