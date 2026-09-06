package com.jonlink.web.controller.system;

import com.jonlink.common.core.controller.BaseController;
import com.jonlink.common.core.domain.AjaxResult;
import com.jonlink.system.domain.FinLedgerVoucherLog;
import com.jonlink.system.domain.FinVoucher;
import com.jonlink.system.service.IFinLedgerBookEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 台账记账引擎 Controller
 * @author jonlink
 */
@RestController
@RequestMapping("/finance/ledgerBook")
public class FinLedgerBookController extends BaseController
{
    @Autowired
    private IFinLedgerBookEngine engine;

    /**
     * POST /finance/ledgerBook/book
     * body: { templateCode, ledgerId, policyNo, amountFieldMap, periodCode, voucherDate }
     */
    @PreAuthorize("@ss.hasPermi('finance:ledgerBook:book')")
    @PostMapping("/book")
    public AjaxResult book(@RequestBody Map<String, Object> body)
    {
        String templateCode = (String) body.get("templateCode");
        Object lidObj = body.get("ledgerId");
        Long ledgerId = lidObj == null ? 0L : Long.valueOf(lidObj.toString());
        String policyNo = (String) body.get("policyNo");
        @SuppressWarnings("unchecked")
        Map<String, Object> context = (Map<String, Object>) body.get("amountFieldMap");
        String periodCode = (String) body.get("periodCode");
        String voucherDate = (String) body.get("voucherDate");
        if (context == null) return AjaxResult.error("amountFieldMap 不能为空");

        FinVoucher v = engine.book(templateCode, ledgerId, policyNo, context, periodCode, voucherDate, getLoginUser().getUsername());
        return AjaxResult.success("记账成功", v);
    }

    /**
     * POST /finance/ledgerBook/unbook/{logId}
     */
    @PreAuthorize("@ss.hasPermi('finance:ledgerBook:unbook')")
    @PostMapping("/unbook/{logId}")
    public AjaxResult unbook(@PathVariable Long logId)
    {
        engine.unbook(logId, getLoginUser().getUsername());
        return AjaxResult.success("反记账成功");
    }

    /**
     * GET /finance/ledgerBook/list/{ledgerId}
     */
    @PreAuthorize("@ss.hasPermi('finance:ledgerBook:list')")
    @GetMapping("/list/{ledgerId}")
    public AjaxResult listByLedgerId(@PathVariable Long ledgerId)
    {
        List<FinLedgerVoucherLog> list = engine.listByLedgerId(ledgerId);
        return AjaxResult.success(list);
    }
}