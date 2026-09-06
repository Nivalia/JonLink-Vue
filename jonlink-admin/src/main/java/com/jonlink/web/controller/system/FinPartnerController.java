package com.jonlink.web.controller.system;

import com.jonlink.common.core.controller.BaseController;
import com.jonlink.common.core.domain.AjaxResult;
import com.jonlink.common.core.page.TableDataInfo;
import com.jonlink.system.domain.FinPartner;
import com.jonlink.system.domain.FinPayable;
import com.jonlink.system.domain.FinReceivable;
import com.jonlink.system.service.IFinPartnerService;
import com.jonlink.system.service.IFinPayableService;
import com.jonlink.system.service.IFinReceivableService;
import com.jonlink.system.service.IFinSettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 往来管理 Controller(伙伴/应收/应付/核销)
 * @author jonlink
 */
@RestController
@RequestMapping("/finance/partner")
public class FinPartnerController extends BaseController
{
    @Autowired private IFinPartnerService partnerService;
    @Autowired private IFinReceivableService receivableService;
    @Autowired private IFinPayableService payableService;
    @Autowired private IFinSettlementService settlementService;

    // ===== partner =====
    @PreAuthorize("@ss.hasPermi('finance:partner:list')")
    @GetMapping("/list")
    public TableDataInfo list(FinPartner q)
    {
        startPage(); return getDataTable(partnerService.selectFinPartnerList(q));
    }

    @PreAuthorize("@ss.hasPermi('finance:partner:add')")
    @PostMapping
    public AjaxResult add(@RequestBody FinPartner p)
    {
        p.setCreateBy(getLoginUser().getUsername());
        return toAjax(partnerService.insertFinPartner(p));
    }

    @PreAuthorize("@ss.hasPermi('finance:partner:upsert')")
    @PostMapping("/upsert")
    public AjaxResult upsert(@RequestBody FinPartner p)
    {
        Long id = partnerService.upsertByName(p.getPartnerName(), p.getPartnerType(), p.getRefTable(), p.getRefId(), getLoginUser().getUsername());
        return AjaxResult.success("OK", id);
    }

    @PreAuthorize("@ss.hasPermi('finance:partner:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(partnerService.deleteFinPartnerByIds(ids));
    }

    // ===== receivable =====
    @PreAuthorize("@ss.hasPermi('finance:receivable:list')")
    @GetMapping("/receivable/list")
    public TableDataInfo receivableList(FinReceivable q)
    {
        startPage(); return getDataTable(receivableService.selectFinReceivableList(q));
    }

    @PreAuthorize("@ss.hasPermi('finance:receivable:add')")
    @PostMapping("/receivable")
    public AjaxResult addReceivable(@RequestBody java.util.Map<String, Object> body)
    {
        Long pid = Long.valueOf(body.get("partnerId").toString());
        BigDecimal amount = new BigDecimal(body.get("amount").toString());
        Long id = receivableService.createForPartner(pid,
            (String) body.getOrDefault("bizType", "DEFAULT"),
            (String) body.get("sourceType"),
            body.get("sourceId") == null ? null : Long.valueOf(body.get("sourceId").toString()),
            amount, new Date(), getLoginUser().getUsername());
        return AjaxResult.success("OK", id);
    }

    // ===== payable =====
    @PreAuthorize("@ss.hasPermi('finance:payable:list')")
    @GetMapping("/payable/list")
    public TableDataInfo payableList(FinPayable q)
    {
        startPage(); return getDataTable(payableService.selectFinPayableList(q));
    }

    @PreAuthorize("@ss.hasPermi('finance:payable:add')")
    @PostMapping("/payable")
    public AjaxResult addPayable(@RequestBody java.util.Map<String, Object> body)
    {
        Long pid = Long.valueOf(body.get("partnerId").toString());
        BigDecimal amount = new BigDecimal(body.get("amount").toString());
        Long id = payableService.createForPartner(pid,
            (String) body.getOrDefault("bizType", "DEFAULT"),
            (String) body.get("sourceType"),
            body.get("sourceId") == null ? null : Long.valueOf(body.get("sourceId").toString()),
            amount, new Date(), getLoginUser().getUsername());
        return AjaxResult.success("OK", id);
    }

    // ===== settlement =====
    @PreAuthorize("@ss.hasPermi('finance:settlement:cancel')")
    @PostMapping("/receivable/{id}/cancel")
    public AjaxResult cancelR(@PathVariable Long id, @RequestBody java.util.Map<String, Object> body)
    {
        BigDecimal amt = new BigDecimal(body.get("amount").toString());
        return AjaxResult.success("OK", settlementService.cancelReceivable(id, amt, getLoginUser().getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('finance:settlement:cancel')")
    @PostMapping("/payable/{id}/cancel")
    public AjaxResult cancelP(@PathVariable Long id, @RequestBody java.util.Map<String, Object> body)
    {
        BigDecimal amt = new BigDecimal(body.get("amount").toString());
        return AjaxResult.success("OK", settlementService.cancelPayable(id, amt, getLoginUser().getUsername()));
    }
}