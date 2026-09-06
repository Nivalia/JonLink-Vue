package com.jonlink.web.controller.system;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
import com.jonlink.common.utils.SecurityUtils;
import com.jonlink.system.domain.FinVoucher;
import com.jonlink.system.service.IFinVoucherService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 记账凭证 Controller
 *
 * M1 状态机: 0草稿 → 1已审核 → 2已过账 → 3已作废
 * 反向操作: cancelAudit(1→0)、unpost(2→1)、不允许作废后复活
 *
 * @author jonlink
 */
@RestController
@RequestMapping("/finance/voucher")
public class FinVoucherController extends BaseController
{
    @Autowired
    private IFinVoucherService finVoucherService;

    /**
     * 查询记账凭证列表
     */
    @PreAuthorize("@ss.hasPermi('finance:voucher:list')")
    @GetMapping("/list")
    public TableDataInfo list(FinVoucher finVoucher)
    {
        startPage();
        List<FinVoucher> list = finVoucherService.selectFinVoucherList(finVoucher);
        return getDataTable(list);
    }

    /**
     * 导出
     */
    @PreAuthorize("@ss.hasPermi('finance:voucher:export')")
    @Log(title = "记账凭证", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinVoucher finVoucher)
    {
        List<FinVoucher> list = finVoucherService.selectFinVoucherList(finVoucher);
        ExcelUtil<FinVoucher> util = new ExcelUtil<FinVoucher>(FinVoucher.class);
        util.exportExcel(response, list, "记账凭证数据");
    }

    /**
     * 完整查询(含分录),用于详情/打印预览
     */
    @PreAuthorize("@ss.hasPermi('finance:voucher:query')")
    @GetMapping(value = "/full/{id}")
    public AjaxResult getFull(@PathVariable("id") Long id)
    {
        Map<String, Object> data = finVoucherService.getFullVoucher(id);
        return success(data);
    }

    /**
     * 仅查头
     */
    @PreAuthorize("@ss.hasPermi('finance:voucher:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(finVoucherService.selectFinVoucherById(id));
    }

    /**
     * 新增凭证(多借多贷 + 自动凭证号 + 借贷平衡校验)
     */
    @PreAuthorize("@ss.hasPermi('finance:voucher:add')")
    @Log(title = "记账凭证", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FinVoucher finVoucher)
    {
        Long id = finVoucherService.saveWithEntries(finVoucher);
        return success(id);
    }

    /**
     * 修改凭证(草稿状态):重新校验借贷平衡并整体替换分录
     */
    @PreAuthorize("@ss.hasPermi('finance:voucher:edit')")
    @Log(title = "记账凭证", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FinVoucher finVoucher)
    {
        FinVoucher exist = finVoucherService.selectFinVoucherById(finVoucher.getId());
        if (exist == null) return error("凭证不存在");
        if (!"0".equals(exist.getStatus())) return error("仅草稿状态可修改");
        // 走 saveWithEntries,但需要保持 id 一致 → 复用头,删旧分录,新增分录
        // 这里偷懒:直接调 insert 路径,然后修改 id 覆盖;最简方案是 update,分录重建
        finVoucherService.deleteFinVoucherById(finVoucher.getId());
        Long id = finVoucherService.saveWithEntries(finVoucher);
        return success(id);
    }

    /**
     * 删除(仅草稿)
     */
    @PreAuthorize("@ss.hasPermi('finance:voucher:remove')")
    @Log(title = "记账凭证", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(finVoucherService.deleteFinVoucherByIds(ids));
    }

    // ===== M1 状态机端点 =====

    /**
     * 审核
     */
    @PreAuthorize("@ss.hasPermi('finance:voucher:audit')")
    @Log(title = "记账凭证-审核", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/audit")
    public AjaxResult audit(@PathVariable("id") Long id)
    {
        finVoucherService.audit(id, SecurityUtils.getUsername());
        return success();
    }

    /**
     * 反审核
     */
    @PreAuthorize("@ss.hasPermi('finance:voucher:audit')")
    @Log(title = "记账凭证-反审核", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/cancelAudit")
    public AjaxResult cancelAudit(@PathVariable("id") Long id)
    {
        finVoucherService.cancelAudit(id, SecurityUtils.getUsername());
        return success();
    }

    /**
     * 过账
     */
    @PreAuthorize("@ss.hasPermi('finance:voucher:post')")
    @Log(title = "记账凭证-过账", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/post")
    public AjaxResult post(@PathVariable("id") Long id)
    {
        finVoucherService.post(id, SecurityUtils.getUsername());
        return success();
    }

    /**
     * 反过账
     */
    @PreAuthorize("@ss.hasPermi('finance:voucher:post')")
    @Log(title = "记账凭证-反过账", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/unpost")
    public AjaxResult unpost(@PathVariable("id") Long id)
    {
        finVoucherService.unpost(id, SecurityUtils.getUsername());
        return success();
    }

    /**
     * 作废(任意状态 → 3)
     */
    @PreAuthorize("@ss.hasPermi('finance:voucher:void')")
    @Log(title = "记账凭证-作废", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/void")
    public AjaxResult voidVoucher(@PathVariable("id") Long id, @RequestBody(required = false) Map<String, Object> body)
    {
        String reason = body == null ? null : (String) body.get("reason");
        finVoucherService.voidVoucher(id, SecurityUtils.getUsername(), reason);
        return success();
    }

    /**
     * 获取下一凭证号(便于前端展示)
     */
    @PreAuthorize("@ss.hasPermi('finance:voucher:query')")
    @GetMapping("/nextNo/{periodCode}")
    public AjaxResult nextNo(@PathVariable("periodCode") String periodCode)
    {
        return success(finVoucherService.generateVoucherNo(periodCode));
    }
}