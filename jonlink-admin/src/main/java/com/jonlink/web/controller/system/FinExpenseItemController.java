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
import com.jonlink.system.domain.FinExpenseItem;
import com.jonlink.system.service.IFinExpenseItemService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 报销明细Controller
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@RestController
@RequestMapping("/finance/item")
public class FinExpenseItemController extends BaseController
{
    @Autowired
    private IFinExpenseItemService finExpenseItemService;

    /**
     * 查询报销明细列表
     */
    @PreAuthorize("@ss.hasPermi('finance:item:list')")
    @GetMapping("/list")
    public TableDataInfo list(FinExpenseItem finExpenseItem)
    {
        startPage();
        List<FinExpenseItem> list = finExpenseItemService.selectFinExpenseItemList(finExpenseItem);
        return getDataTable(list);
    }

    /**
     * 导出报销明细列表
     */
    @PreAuthorize("@ss.hasPermi('finance:item:export')")
    @Log(title = "报销明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinExpenseItem finExpenseItem)
    {
        List<FinExpenseItem> list = finExpenseItemService.selectFinExpenseItemList(finExpenseItem);
        ExcelUtil<FinExpenseItem> util = new ExcelUtil<FinExpenseItem>(FinExpenseItem.class);
        util.exportExcel(response, list, "报销明细数据");
    }

    /**
     * 获取报销明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('finance:item:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(finExpenseItemService.selectFinExpenseItemById(id));
    }

    /**
     * 新增报销明细
     */
    @PreAuthorize("@ss.hasPermi('finance:item:add')")
    @Log(title = "报销明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FinExpenseItem finExpenseItem)
    {
        return toAjax(finExpenseItemService.insertFinExpenseItem(finExpenseItem));
    }

    /**
     * 修改报销明细
     */
    @PreAuthorize("@ss.hasPermi('finance:item:edit')")
    @Log(title = "报销明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FinExpenseItem finExpenseItem)
    {
        return toAjax(finExpenseItemService.updateFinExpenseItem(finExpenseItem));
    }

    /**
     * 删除报销明细
     */
    @PreAuthorize("@ss.hasPermi('finance:item:remove')")
    @Log(title = "报销明细", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(finExpenseItemService.deleteFinExpenseItemByIds(ids));
    }
}
