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
import com.jonlink.system.domain.FinCashFlow;
import com.jonlink.system.service.IFinCashFlowService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 资金流水Controller
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@RestController
@RequestMapping("/finance/flow")
public class FinCashFlowController extends BaseController
{
    @Autowired
    private IFinCashFlowService finCashFlowService;

    /**
     * 查询资金流水列表
     */
    @PreAuthorize("@ss.hasPermi('finance:flow:list')")
    @GetMapping("/list")
    public TableDataInfo list(FinCashFlow finCashFlow)
    {
        startPage();
        List<FinCashFlow> list = finCashFlowService.selectFinCashFlowList(finCashFlow);
        return getDataTable(list);
    }

    /**
     * 导出资金流水列表
     */
    @PreAuthorize("@ss.hasPermi('finance:flow:export')")
    @Log(title = "资金流水", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinCashFlow finCashFlow)
    {
        List<FinCashFlow> list = finCashFlowService.selectFinCashFlowList(finCashFlow);
        ExcelUtil<FinCashFlow> util = new ExcelUtil<FinCashFlow>(FinCashFlow.class);
        util.exportExcel(response, list, "资金流水数据");
    }

    /**
     * 获取资金流水详细信息
     */
    @PreAuthorize("@ss.hasPermi('finance:flow:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(finCashFlowService.selectFinCashFlowById(id));
    }

    /**
     * 新增资金流水
     */
    @PreAuthorize("@ss.hasPermi('finance:flow:add')")
    @Log(title = "资金流水", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FinCashFlow finCashFlow)
    {
        return toAjax(finCashFlowService.insertFinCashFlow(finCashFlow));
    }

    /**
     * 修改资金流水
     */
    @PreAuthorize("@ss.hasPermi('finance:flow:edit')")
    @Log(title = "资金流水", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FinCashFlow finCashFlow)
    {
        return toAjax(finCashFlowService.updateFinCashFlow(finCashFlow));
    }

    /**
     * 删除资金流水
     */
    @PreAuthorize("@ss.hasPermi('finance:flow:remove')")
    @Log(title = "资金流水", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(finCashFlowService.deleteFinCashFlowByIds(ids));
    }
}
