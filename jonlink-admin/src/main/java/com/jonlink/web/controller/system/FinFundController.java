package com.jonlink.web.controller.system;

import com.jonlink.common.core.controller.BaseController;
import com.jonlink.common.core.domain.AjaxResult;
import com.jonlink.common.core.page.TableDataInfo;
import com.jonlink.system.domain.FinBankAccount;
import com.jonlink.system.domain.FinCashFlow;
import com.jonlink.system.domain.FinPayment;
import com.jonlink.system.domain.FinReceipt;
import com.jonlink.system.mapper.FinBankAccountMapper;
import com.jonlink.system.mapper.FinCashFlowMapper;
import com.jonlink.system.mapper.FinPaymentMapper;
import com.jonlink.system.mapper.FinReceiptMapper;
import com.jonlink.system.service.IFinFundEngine;
import com.jonlink.system.service.IFinPaymentService;
import com.jonlink.system.service.IFinReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 资金管理 Controller(账户/收款/付款/流水)
 * @author jonlink
 */
@RestController
@RequestMapping("/finance/fund")
public class FinFundController extends BaseController
{
    @Autowired private IFinFundEngine fundEngine;
    @Autowired private IFinReceiptService receiptService;
    @Autowired private IFinPaymentService paymentService;
    @Autowired private FinBankAccountMapper accountMapper;
    @Autowired private FinReceiptMapper receiptMapper;
    @Autowired private FinPaymentMapper paymentMapper;
    @Autowired private FinCashFlowMapper flowMapper;

    // ===== 账户 =====
    @PreAuthorize("@ss.hasPermi('finance:account:list')")
    @GetMapping("/account/list")
    public TableDataInfo accountList(FinBankAccount q)
    {
        startPage(); return getDataTable(accountMapper.selectFinBankAccountList(q));
    }

    @PreAuthorize("@ss.hasPermi('finance:account:add')")
    @PostMapping("/account")
    public AjaxResult addAccount(@RequestBody FinBankAccount a)
    {
        a.setCreateBy(getLoginUser().getUsername());
        if (a.getCurrentBalance() == null) a.setCurrentBalance(a.getInitBalance() == null ? BigDecimal.ZERO : a.getInitBalance());
        return toAjax(accountMapper.insertFinBankAccount(a));
    }

    @PreAuthorize("@ss.hasPermi('finance:account:active')")
    @GetMapping("/account/active")
    public AjaxResult listActive() { return AjaxResult.success(fundEngine.listActiveAccounts()); }

    // ===== 收款 =====
    @PreAuthorize("@ss.hasPermi('finance:receipt:list')")
    @GetMapping("/receipt/list")
    public TableDataInfo receiptList(FinReceipt q)
    {
        startPage(); return getDataTable(receiptMapper.selectFinReceiptList(q));
    }

    @PreAuthorize("@ss.hasPermi('finance:receipt:add')")
    @PostMapping("/receipt")
    public AjaxResult addReceipt(@RequestBody java.util.Map<String, Object> body)
    {
        Long pid = body.get("partnerId") == null ? null : Long.valueOf(body.get("partnerId").toString());
        Long bid = body.get("bankAccountId") == null ? null : Long.valueOf(body.get("bankAccountId").toString());
        BigDecimal amt = new BigDecimal(body.get("amount").toString());
        Long rid = receiptService.create(pid, bid, amt,
            (String) body.getOrDefault("bizType", "DEFAULT"),
            (String) body.get("sourceType"),
            body.get("sourceId") == null ? null : Long.valueOf(body.get("sourceId").toString()),
            getLoginUser().getUsername());
        return AjaxResult.success("OK", rid);
    }

    @PreAuthorize("@ss.hasPermi('finance:receipt:confirm')")
    @PostMapping("/receipt/{id}/confirm")
    public AjaxResult confirmR(@PathVariable Long id)
    {
        return AjaxResult.success("OK", fundEngine.confirmReceipt(id, getLoginUser().getUsername()));
    }

    // ===== 付款 =====
    @PreAuthorize("@ss.hasPermi('finance:payment:list')")
    @GetMapping("/payment/list")
    public TableDataInfo paymentList(FinPayment q)
    {
        startPage(); return getDataTable(paymentMapper.selectFinPaymentList(q));
    }

    @PreAuthorize("@ss.hasPermi('finance:payment:add')")
    @PostMapping("/payment")
    public AjaxResult addPayment(@RequestBody java.util.Map<String, Object> body)
    {
        Long pid = body.get("partnerId") == null ? null : Long.valueOf(body.get("partnerId").toString());
        Long bid = body.get("bankAccountId") == null ? null : Long.valueOf(body.get("bankAccountId").toString());
        BigDecimal amt = new BigDecimal(body.get("amount").toString());
        Long pid2 = paymentService.create(pid, bid, amt,
            (String) body.getOrDefault("bizType", "DEFAULT"),
            (String) body.get("sourceType"),
            body.get("sourceId") == null ? null : Long.valueOf(body.get("sourceId").toString()),
            getLoginUser().getUsername());
        return AjaxResult.success("OK", pid2);
    }

    @PreAuthorize("@ss.hasPermi('finance:payment:confirm')")
    @PostMapping("/payment/{id}/confirm")
    public AjaxResult confirmP(@PathVariable Long id)
    {
        return AjaxResult.success("OK", fundEngine.confirmPayment(id, getLoginUser().getUsername()));
    }

    // ===== 流水 =====
    @PreAuthorize("@ss.hasPermi('finance:flow:list')")
    @GetMapping("/flow/list")
    public TableDataInfo flowList(FinCashFlow q)
    {
        startPage(); return getDataTable(flowMapper.selectFinCashFlowList(q));
    }
}