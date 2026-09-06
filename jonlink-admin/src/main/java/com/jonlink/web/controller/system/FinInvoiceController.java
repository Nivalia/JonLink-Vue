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
import com.jonlink.system.domain.FinInvoice;
import com.jonlink.system.service.IFinInvoiceService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 发票Controller
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@RestController
@RequestMapping("/finance/invoice")
public class FinInvoiceController extends BaseController
{
    @Autowired
    private IFinInvoiceService finInvoiceService;

    /**
     * 查询发票列表
     */
    @PreAuthorize("@ss.hasPermi('finance:invoice:list')")
    @GetMapping("/list")
    public TableDataInfo list(FinInvoice finInvoice)
    {
        startPage();
        List<FinInvoice> list = finInvoiceService.selectFinInvoiceList(finInvoice);
        return getDataTable(list);
    }

    /**
     * 导出发票列表
     */
    @PreAuthorize("@ss.hasPermi('finance:invoice:export')")
    @Log(title = "发票", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinInvoice finInvoice)
    {
        List<FinInvoice> list = finInvoiceService.selectFinInvoiceList(finInvoice);
        ExcelUtil<FinInvoice> util = new ExcelUtil<FinInvoice>(FinInvoice.class);
        util.exportExcel(response, list, "发票数据");
    }

    /**
     * 获取发票详细信息
     */
    @PreAuthorize("@ss.hasPermi('finance:invoice:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(finInvoiceService.selectFinInvoiceById(id));
    }

    /**
     * 新增发票
     */
    @PreAuthorize("@ss.hasPermi('finance:invoice:add')")
    @Log(title = "发票", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FinInvoice finInvoice)
    {
        return toAjax(finInvoiceService.insertFinInvoice(finInvoice));
    }

    /**
     * 修改发票
     */
    @PreAuthorize("@ss.hasPermi('finance:invoice:edit')")
    @Log(title = "发票", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FinInvoice finInvoice)
    {
        return toAjax(finInvoiceService.updateFinInvoice(finInvoice));
    }

    /**
     * 删除发票
     */
    @PreAuthorize("@ss.hasPermi('finance:invoice:remove')")
    @Log(title = "发票", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(finInvoiceService.deleteFinInvoiceByIds(ids));
    }

    /**
     * 发票核验 (mock: 仅校验金额/发票号, 不调税局)
     */
    @PreAuthorize("@ss.hasPermi('finance:invoice:verify')")
    @Log(title = "发票核验", businessType = BusinessType.UPDATE)
    @PostMapping("/verify/{id}")
    public AjaxResult verify(@PathVariable Long id)
    {
        FinInvoice inv = finInvoiceService.selectFinInvoiceById(id);
        if (inv == null) return AjaxResult.error("发票不存在");
        if (inv.getAmount() == null || inv.getAmount().signum() <= 0) {
            return AjaxResult.error("发票金额必须大于 0");
        }
        if (inv.getInvoiceNo() == null || inv.getInvoiceNo().trim().length() < 4) {
            return AjaxResult.error("发票号不合法");
        }
        inv.setStatus("1");
        inv.setUpdateBy(getUsername());
        return toAjax(finInvoiceService.updateFinInvoice(inv));
    }
}
