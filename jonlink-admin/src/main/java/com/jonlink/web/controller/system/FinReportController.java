package com.jonlink.web.controller.system;

import com.jonlink.common.core.controller.BaseController;
import com.jonlink.common.core.domain.AjaxResult;
import com.jonlink.system.service.IFinReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 财务报表 Controller
 * @author jonlink
 */
@RestController
@RequestMapping("/finance/report")
public class FinReportController extends BaseController
{
    @Autowired private IFinReportService reportService;

    @PreAuthorize("@ss.hasPermi('finance:report:subjectBalance')")
    @GetMapping("/subjectBalance")
    public AjaxResult subjectBalance(@RequestParam String periodCode)
    {
        return AjaxResult.success(reportService.subjectBalance(periodCode));
    }

    @PreAuthorize("@ss.hasPermi('finance:report:incomeStatement')")
    @GetMapping("/incomeStatement")
    public AjaxResult incomeStatement(@RequestParam String periodCode)
    {
        return AjaxResult.success(reportService.incomeStatement(periodCode));
    }

    @PreAuthorize("@ss.hasPermi('finance:report:balanceSheet')")
    @GetMapping("/balanceSheet")
    public AjaxResult balanceSheet(@RequestParam String periodCode)
    {
        return AjaxResult.success(reportService.balanceSheet(periodCode));
    }

    @PreAuthorize("@ss.hasPermi('finance:report:ledgerPerformance')")
    @GetMapping("/ledgerPerformance")
    public AjaxResult ledgerPerformance(@RequestParam(required=false, defaultValue="2026-01-01") String startDate,
                                       @RequestParam(required=false, defaultValue="2026-12-31") String endDate)
    {
        return AjaxResult.success(reportService.ledgerPerformance(startDate, endDate));
    }
}