package com.jonlink.web.controller.system;

import java.math.BigDecimal;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jonlink.common.annotation.Log;
import com.jonlink.common.core.controller.BaseController;
import com.jonlink.common.core.domain.AjaxResult;
import com.jonlink.common.enums.BusinessType;
import com.jonlink.system.service.IFinTaxService;

/**
 * 税务管理 Controller
 *
 * @author jonlink
 */
@RestController
@RequestMapping("/finance/tax")
public class FinTaxController extends BaseController
{
    @Autowired
    private IFinTaxService taxService;

    /**
     * 计算增值税(进项/销项)
     */
    @PreAuthorize("@ss.hasPermi('finance:tax:query')")
    @GetMapping("/vat/{periodCode}")
    public AjaxResult calculateVAT(@PathVariable("periodCode") String periodCode)
    {
        return success(taxService.calculateVAT(periodCode));
    }

    /**
     * 计算所得税
     */
    @PreAuthorize("@ss.hasPermi('finance:tax:query')")
    @GetMapping("/incomeTax/{periodCode}")
    public AjaxResult calculateIncomeTax(@PathVariable("periodCode") String periodCode)
    {
        BigDecimal incomeTax = taxService.calculateIncomeTax(periodCode);
        return success(incomeTax);
    }

    /**
     * 税务汇总
     */
    @PreAuthorize("@ss.hasPermi('finance:tax:query')")
    @GetMapping("/summary/{periodCode}")
    public AjaxResult taxSummary(@PathVariable("periodCode") String periodCode)
    {
        return success(taxService.taxSummary(periodCode));
    }
}
