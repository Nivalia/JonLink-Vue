package com.jonlink.web.controller.wx;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jonlink.common.annotation.Log;
import com.jonlink.common.annotation.Anonymous;
import com.jonlink.common.core.controller.BaseController;
import com.jonlink.common.core.domain.AjaxResult;
import com.jonlink.common.core.page.TableDataInfo;
import com.jonlink.common.enums.BusinessType;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.system.domain.WxFcConfig;
import com.jonlink.system.service.IWxFcConfigService;

/**
 * 核销表单字段配置Controller
 * - 匿名接口 /wx/h5/fcConfig/{sourceType}: H5 表单按配置动态渲染
 *
 * @author jonlink
 */
@RestController
@RequestMapping("/wx/fcConfig")
public class WxFcConfigController extends BaseController
{
    @Autowired
    private IWxFcConfigService wxFcConfigService;

    @PreAuthorize("@ss.hasPermi('wx:fcConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(WxFcConfig wxFcConfig)
    {
        startPage();
        List<WxFcConfig> list = wxFcConfigService.selectWxFcConfigList(wxFcConfig);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('wx:fcConfig:export')")
    @Log(title = "字段配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WxFcConfig wxFcConfig)
    {
        List<WxFcConfig> list = wxFcConfigService.selectWxFcConfigList(wxFcConfig);
        ExcelUtil<WxFcConfig> util = new ExcelUtil<>(WxFcConfig.class);
        util.exportExcel(response, list, "字段配置数据");
    }

    @PreAuthorize("@ss.hasPermi('wx:fcConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wxFcConfigService.selectWxFcConfigById(id));
    }

    @PreAuthorize("@ss.hasPermi('wx:fcConfig:add')")
    @Log(title = "字段配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WxFcConfig wxFcConfig)
    {
        return toAjax(wxFcConfigService.insertWxFcConfig(wxFcConfig));
    }

    @PreAuthorize("@ss.hasPermi('wx:fcConfig:edit')")
    @Log(title = "字段配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WxFcConfig wxFcConfig)
    {
        return toAjax(wxFcConfigService.updateWxFcConfig(wxFcConfig));
    }

    @PreAuthorize("@ss.hasPermi('wx:fcConfig:remove')")
    @Log(title = "字段配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wxFcConfigService.deleteWxFcConfigByIds(ids));
    }

    /** H5 匿名接口: 按 sourceType 拉可见字段,用于表单动态渲染 */
    @Anonymous
    @GetMapping("/render/{sourceType}")
    public AjaxResult render(@PathVariable("sourceType") String sourceType)
    {
        return success(wxFcConfigService.selectWxFcConfigForRender(sourceType));
    }
}
