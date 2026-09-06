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
import com.jonlink.system.domain.FinReceipt;
import com.jonlink.system.service.IFinReceiptService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 收款单Controller
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@RestController
@RequestMapping("/finance/receipt")
public class FinReceiptController extends BaseController
{
    @Autowired
    private IFinReceiptService finReceiptService;

    /**
     * 查询收款单列表
     */
    @PreAuthorize("@ss.hasPermi('finance:receipt:list')")
    @GetMapping("/list")
    public TableDataInfo list(FinReceipt finReceipt)
    {
        startPage();
        List<FinReceipt> list = finReceiptService.selectFinReceiptList(finReceipt);
        return getDataTable(list);
    }

    /**
     * 导出收款单列表
     */
    @PreAuthorize("@ss.hasPermi('finance:receipt:export')")
    @Log(title = "收款单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinReceipt finReceipt)
    {
        List<FinReceipt> list = finReceiptService.selectFinReceiptList(finReceipt);
        ExcelUtil<FinReceipt> util = new ExcelUtil<FinReceipt>(FinReceipt.class);
        util.exportExcel(response, list, "收款单数据");
    }

    /**
     * 获取收款单详细信息
     */
    @PreAuthorize("@ss.hasPermi('finance:receipt:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(finReceiptService.selectFinReceiptById(id));
    }

    /**
     * 新增收款单
     */
    @PreAuthorize("@ss.hasPermi('finance:receipt:add')")
    @Log(title = "收款单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FinReceipt finReceipt)
    {
        return toAjax(finReceiptService.insertFinReceipt(finReceipt));
    }

    /**
     * 修改收款单
     */
    @PreAuthorize("@ss.hasPermi('finance:receipt:edit')")
    @Log(title = "收款单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FinReceipt finReceipt)
    {
        return toAjax(finReceiptService.updateFinReceipt(finReceipt));
    }

    /**
     * 删除收款单
     */
    @PreAuthorize("@ss.hasPermi('finance:receipt:remove')")
    @Log(title = "收款单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(finReceiptService.deleteFinReceiptByIds(ids));
    }
}
