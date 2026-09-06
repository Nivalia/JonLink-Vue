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
import com.jonlink.system.domain.FinCommission;
import com.jonlink.system.service.IFinCommissionService;

/**
 * 佣金结算 Controller
 */
@RestController
@RequestMapping("/finance/commission")
public class FinCommissionController extends BaseController
{
    @Autowired private IFinCommissionService commissionService;

    @PreAuthorize("@ss.hasPermi('finance:commission:list')")
    @GetMapping("/list")
    public TableDataInfo list(FinCommission query) {
        startPage();
        List<FinCommission> list = commissionService.list(query);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('finance:commission:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(commissionService.getById(id));
    }

    @PreAuthorize("@ss.hasPermi('finance:commission:add')")
    @Log(title = "佣金结算-批量创建", businessType = BusinessType.INSERT)
    @PostMapping("/batch")
    public AjaxResult batchCreate(@RequestBody List<FinCommission> list) {
        int count = commissionService.batchCreate(list, SecurityUtils.getUsername());
        return success(count);
    }

    @PreAuthorize("@ss.hasPermi('finance:commission:edit')")
    @Log(title = "佣金结算-确认", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/confirm")
    public AjaxResult confirm(@PathVariable Long id) {
        commissionService.confirm(id, SecurityUtils.getUsername());
        return success();
    }

    @PreAuthorize("@ss.hasPermi('finance:commission:edit')")
    @Log(title = "佣金结算-标记已支付", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/paid/{receiptOrPaymentId}")
    public AjaxResult markPaid(@PathVariable Long id, @PathVariable Long receiptOrPaymentId) {
        commissionService.markPaid(id, receiptOrPaymentId, SecurityUtils.getUsername());
        return success();
    }

    @PreAuthorize("@ss.hasPermi('finance:commission:query')")
    @GetMapping("/summary")
    public AjaxResult summary(@RequestParam String startDate, @RequestParam String endDate, @RequestParam String direction) {
        return success(commissionService.summary(startDate, endDate, direction));
    }
}
