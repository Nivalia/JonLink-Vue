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
import com.jonlink.system.domain.WxMpTemplate;
import com.jonlink.system.service.IWxMpTemplateService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 模板管理Controller
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@RestController
@RequestMapping("/wx/template")
public class WxMpTemplateController extends BaseController
{
    @Autowired
    private IWxMpTemplateService wxMpTemplateService;

    /**
     * 查询模板管理列表
     */
    @PreAuthorize("@ss.hasPermi('wx:template:list')")
    @GetMapping("/list")
    public TableDataInfo list(WxMpTemplate wxMpTemplate)
    {
        startPage();
        List<WxMpTemplate> list = wxMpTemplateService.selectWxMpTemplateList(wxMpTemplate);
        return getDataTable(list);
    }

    /**
     * 导出模板管理列表
     */
    @PreAuthorize("@ss.hasPermi('wx:template:export')")
    @Log(title = "模板管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WxMpTemplate wxMpTemplate)
    {
        List<WxMpTemplate> list = wxMpTemplateService.selectWxMpTemplateList(wxMpTemplate);
        ExcelUtil<WxMpTemplate> util = new ExcelUtil<WxMpTemplate>(WxMpTemplate.class);
        util.exportExcel(response, list, "模板管理数据");
    }

    /**
     * 获取模板管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('wx:template:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wxMpTemplateService.selectWxMpTemplateById(id));
    }

    /**
     * 新增模板管理
     */
    @PreAuthorize("@ss.hasPermi('wx:template:add')")
    @Log(title = "模板管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WxMpTemplate wxMpTemplate)
    {
        return toAjax(wxMpTemplateService.insertWxMpTemplate(wxMpTemplate));
    }

    /**
     * 修改模板管理
     */
    @PreAuthorize("@ss.hasPermi('wx:template:edit')")
    @Log(title = "模板管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WxMpTemplate wxMpTemplate)
    {
        return toAjax(wxMpTemplateService.updateWxMpTemplate(wxMpTemplate));
    }

    /**
     * 删除模板管理
     */
    @PreAuthorize("@ss.hasPermi('wx:template:remove')")
    @Log(title = "模板管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wxMpTemplateService.deleteWxMpTemplateByIds(ids));
    }
}
