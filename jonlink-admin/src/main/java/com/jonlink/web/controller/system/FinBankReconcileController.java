package com.jonlink.web.controller.system;

import java.util.List;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jonlink.common.annotation.Log;
import com.jonlink.common.core.controller.BaseController;
import com.jonlink.common.core.domain.AjaxResult;
import com.jonlink.common.core.page.TableDataInfo;
import com.jonlink.common.enums.BusinessType;
import com.jonlink.common.utils.SecurityUtils;
import com.jonlink.system.domain.FinBankReconcile;
import com.jonlink.system.service.IFinBankReconcileService;

/**
 * 银行对账 Controller
 */
@RestController
@RequestMapping("/finance/bank-reconcile")
public class FinBankReconcileController extends BaseController
{
    @Autowired private IFinBankReconcileService reconcileService;

    @PreAuthorize("@ss.hasPermi('finance:bankReconcile:list')")
    @GetMapping("/list")
    public TableDataInfo list(FinBankReconcile query) {
        startPage();
        List<FinBankReconcile> list = reconcileService.list(query);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('finance:bankReconcile:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(reconcileService.getById(id));
    }

    @PreAuthorize("@ss.hasPermi('finance:bankReconcile:import')")
    @Log(title = "银行对账-导入", businessType = BusinessType.INSERT)
    @PostMapping("/import/{bankAccountId}")
    public AjaxResult importData(@PathVariable Long bankAccountId, @RequestBody List<FinBankReconcile> records) {
        int count = reconcileService.batchImport(bankAccountId, records, SecurityUtils.getUsername());
        return success(count);
    }

    @PreAuthorize("@ss.hasPermi('finance:bankReconcile:reconcile')")
    @Log(title = "银行对账-对账", businessType = BusinessType.UPDATE)
    @PostMapping("/reconcile/{reconcileId}/{cashFlowId}")
    public AjaxResult reconcile(@PathVariable Long reconcileId, @PathVariable Long cashFlowId) {
        reconcileService.reconcile(reconcileId, cashFlowId, SecurityUtils.getUsername());
        return success();
    }

    @PreAuthorize("@ss.hasPermi('finance:bankReconcile:reconcile')")
    @Log(title = "银行对账-取消对账", businessType = BusinessType.UPDATE)
    @PostMapping("/cancel/{reconcileId}")
    public AjaxResult cancelReconcile(@PathVariable Long reconcileId) {
        reconcileService.cancelReconcile(reconcileId, SecurityUtils.getUsername());
        return success();
    }

    @PreAuthorize("@ss.hasPermi('finance:bankReconcile:query')")
    @GetMapping("/unmatched/{bankAccountId}")
    public AjaxResult getUnmatched(@PathVariable Long bankAccountId) {
        return success(reconcileService.getUnmatched(bankAccountId));
    }

    @PreAuthorize("@ss.hasPermi('finance:bankReconcile:query')")
    @GetMapping("/report/{bankAccountId}/{periodCode}")
    public AjaxResult getReport(@PathVariable Long bankAccountId, @PathVariable String periodCode) {
        return success(reconcileService.getReconciliationReport(bankAccountId, periodCode));
    }
}
