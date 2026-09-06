package com.jonlink.web.controller.system;

import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.jonlink.common.annotation.Log;
import com.jonlink.common.core.controller.BaseController;
import com.jonlink.common.core.domain.AjaxResult;
import com.jonlink.common.enums.BusinessType;
import com.jonlink.system.service.IFinRevenueService;

/**
 * 保单收入确认Controller
 *
 * @author jonlink
 * @date 2026-08-23
 */
@RestController
@RequestMapping("/finance/revenue")
public class FinRevenueController extends BaseController
{
    @Autowired
    private IFinRevenueService finRevenueService;

    /**
     * 确认保单收入
     */
    @PreAuthorize("@ss.hasPermi('finance:revenue:confirm')")
    @Log(title = "保单收入确认", businessType = BusinessType.UPDATE)
    @PostMapping("/confirm/{policyId}")
    public AjaxResult confirm(@PathVariable("policyId") Long policyId)
    {
        finRevenueService.confirmRevenue(policyId, getUsername());
        return success();
    }

    /**
     * 反确认
     */
    @PreAuthorize("@ss.hasPermi('finance:revenue:reverse')")
    @Log(title = "保单收入反确认", businessType = BusinessType.UPDATE)
    @PostMapping("/reverse/{policyId}")
    public AjaxResult reverse(@PathVariable("policyId") Long policyId)
    {
        finRevenueService.reverseRevenue(policyId, getUsername());
        return success();
    }

    /**
     * 查询收入汇总
     */
    @PreAuthorize("@ss.hasPermi('finance:revenue:summary')")
    @GetMapping("/summary")
    public AjaxResult summary(@RequestParam(required = false) String startDate,
                              @RequestParam(required = false) String endDate)
    {
        Map<String, Object> result = finRevenueService.summary(startDate, endDate);
        return success(result);
    }
}
