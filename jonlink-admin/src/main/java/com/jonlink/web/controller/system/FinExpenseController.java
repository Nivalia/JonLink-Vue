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
import org.springframework.web.bind.annotation.RestController;
import com.jonlink.common.annotation.Log;
import com.jonlink.common.core.controller.BaseController;
import com.jonlink.common.core.domain.AjaxResult;
import com.jonlink.common.enums.BusinessType;
import com.jonlink.system.domain.FinExpense;
import com.jonlink.system.domain.FinExpenseItem;
import com.jonlink.system.mapper.FinExpenseItemMapper;
import com.jonlink.system.service.IFinExpenseService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 费用报销单Controller
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@RestController
@RequestMapping("/finance/expense")
public class FinExpenseController extends BaseController
{
    @Autowired
    private IFinExpenseService finExpenseService;

    @Autowired
    private FinExpenseItemMapper itemMapper;

    /**
     * 查询费用报销单列表
     */
    @PreAuthorize("@ss.hasPermi('finance:expense:list')")
    @GetMapping("/list")
    public TableDataInfo list(FinExpense finExpense)
    {
        startPage();
        List<FinExpense> list = finExpenseService.selectFinExpenseList(finExpense);
        return getDataTable(list);
    }

    /**
     * 导出费用报销单列表
     */
    @PreAuthorize("@ss.hasPermi('finance:expense:export')")
    @Log(title = "费用报销单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinExpense finExpense)
    {
        List<FinExpense> list = finExpenseService.selectFinExpenseList(finExpense);
        ExcelUtil<FinExpense> util = new ExcelUtil<FinExpense>(FinExpense.class);
        util.exportExcel(response, list, "费用报销单数据");
    }

    /**
     * 获取费用报销单详细信息
     */
    @PreAuthorize("@ss.hasPermi('finance:expense:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        FinExpense e = finExpenseService.selectFinExpenseById(id);
        List<FinExpenseItem> items = itemMapper.selectFinExpenseItemListByExpenseId(id);
        return success(Map.of("expense", e, "items", items));
    }

    /**
     * 新增报销单 (带 items, M6 业务扩展)
     */
    @PreAuthorize("@ss.hasPermi('finance:expense:add')")
    @Log(title = "费用报销单", businessType = BusinessType.INSERT)
    @PostMapping("/withItems")
    public AjaxResult addWithItems(@RequestBody Map<String, Object> body)
    {
        Long id = finExpenseService.createWithItems(
            (String) body.get("applicant"),
            (String) body.get("deptName"),
            (String) body.getOrDefault("expenseType", "0"),
            new BigDecimal(body.get("totalAmount").toString()),
            (List<Map<String, Object>>) body.get("items"),
            getUsername());
        return AjaxResult.success(id);
    }

    /**
     * 新增费用报销单
     */
    @PreAuthorize("@ss.hasPermi('finance:expense:add')")
    @Log(title = "费用报销单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FinExpense finExpense)
    {
        return toAjax(finExpenseService.insertFinExpense(finExpense));
    }

    /**
     * 修改费用报销单
     */
    @PreAuthorize("@ss.hasPermi('finance:expense:edit')")
    @Log(title = "费用报销单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FinExpense finExpense)
    {
        return toAjax(finExpenseService.updateFinExpense(finExpense));
    }

    /**
     * 删除费用报销单
     */
    @PreAuthorize("@ss.hasPermi('finance:expense:remove')")
    @Log(title = "费用报销单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(finExpenseService.deleteFinExpenseByIds(ids));
    }
}
