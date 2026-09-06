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
import com.jonlink.system.domain.FinPayment;
import com.jonlink.system.service.IFinPaymentService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 付款单Controller
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@RestController
@RequestMapping("/finance/payment")
public class FinPaymentController extends BaseController
{
    @Autowired
    private IFinPaymentService finPaymentService;

    /**
     * 查询付款单列表
     */
    @PreAuthorize("@ss.hasPermi('finance:payment:list')")
    @GetMapping("/list")
    public TableDataInfo list(FinPayment finPayment)
    {
        startPage();
        List<FinPayment> list = finPaymentService.selectFinPaymentList(finPayment);
        return getDataTable(list);
    }

    /**
     * 导出付款单列表
     */
    @PreAuthorize("@ss.hasPermi('finance:payment:export')")
    @Log(title = "付款单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinPayment finPayment)
    {
        List<FinPayment> list = finPaymentService.selectFinPaymentList(finPayment);
        ExcelUtil<FinPayment> util = new ExcelUtil<FinPayment>(FinPayment.class);
        util.exportExcel(response, list, "付款单数据");
    }

    /**
     * 获取付款单详细信息
     */
    @PreAuthorize("@ss.hasPermi('finance:payment:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(finPaymentService.selectFinPaymentById(id));
    }

    /**
     * 新增付款单
     */
    @PreAuthorize("@ss.hasPermi('finance:payment:add')")
    @Log(title = "付款单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FinPayment finPayment)
    {
        return toAjax(finPaymentService.insertFinPayment(finPayment));
    }

    /**
     * 修改付款单
     */
    @PreAuthorize("@ss.hasPermi('finance:payment:edit')")
    @Log(title = "付款单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FinPayment finPayment)
    {
        return toAjax(finPaymentService.updateFinPayment(finPayment));
    }

    /**
     * 删除付款单
     */
    @PreAuthorize("@ss.hasPermi('finance:payment:remove')")
    @Log(title = "付款单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(finPaymentService.deleteFinPaymentByIds(ids));
    }
}
