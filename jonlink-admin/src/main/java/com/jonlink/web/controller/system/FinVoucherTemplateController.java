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
import com.jonlink.system.domain.FinVoucherTemplate;
import com.jonlink.system.service.IFinVoucherTemplateService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 凭证模板Controller
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@RestController
@RequestMapping("/finance/template")
public class FinVoucherTemplateController extends BaseController
{
    @Autowired
    private IFinVoucherTemplateService finVoucherTemplateService;

    /**
     * 查询凭证模板列表
     */
    @PreAuthorize("@ss.hasPermi('finance:template:list')")
    @GetMapping("/list")
    public TableDataInfo list(FinVoucherTemplate finVoucherTemplate)
    {
        startPage();
        List<FinVoucherTemplate> list = finVoucherTemplateService.selectFinVoucherTemplateList(finVoucherTemplate);
        return getDataTable(list);
    }

    /**
     * 导出凭证模板列表
     */
    @PreAuthorize("@ss.hasPermi('finance:template:export')")
    @Log(title = "凭证模板", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinVoucherTemplate finVoucherTemplate)
    {
        List<FinVoucherTemplate> list = finVoucherTemplateService.selectFinVoucherTemplateList(finVoucherTemplate);
        ExcelUtil<FinVoucherTemplate> util = new ExcelUtil<FinVoucherTemplate>(FinVoucherTemplate.class);
        util.exportExcel(response, list, "凭证模板数据");
    }

    /**
     * 获取凭证模板详细信息
     */
    @PreAuthorize("@ss.hasPermi('finance:template:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(finVoucherTemplateService.selectFinVoucherTemplateById(id));
    }

    /**
     * 新增凭证模板
     */
    @PreAuthorize("@ss.hasPermi('finance:template:add')")
    @Log(title = "凭证模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FinVoucherTemplate finVoucherTemplate)
    {
        return toAjax(finVoucherTemplateService.insertFinVoucherTemplate(finVoucherTemplate));
    }

    /**
     * 修改凭证模板
     */
    @PreAuthorize("@ss.hasPermi('finance:template:edit')")
    @Log(title = "凭证模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FinVoucherTemplate finVoucherTemplate)
    {
        return toAjax(finVoucherTemplateService.updateFinVoucherTemplate(finVoucherTemplate));
    }

    /**
     * 删除凭证模板
     */
    @PreAuthorize("@ss.hasPermi('finance:template:remove')")
    @Log(title = "凭证模板", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(finVoucherTemplateService.deleteFinVoucherTemplateByIds(ids));
    }
}
