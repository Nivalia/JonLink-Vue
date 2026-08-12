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
import com.jonlink.system.domain.WxMsgRule;
import com.jonlink.system.service.IWxMsgRuleService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 推送规则Controller
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@RestController
@RequestMapping("/wx/msgRule")
public class WxMsgRuleController extends BaseController
{
    @Autowired
    private IWxMsgRuleService wxMsgRuleService;

    /**
     * 查询推送规则列表
     */
    @PreAuthorize("@ss.hasPermi('wx:msgRule:list')")
    @GetMapping("/list")
    public TableDataInfo list(WxMsgRule wxMsgRule)
    {
        startPage();
        List<WxMsgRule> list = wxMsgRuleService.selectWxMsgRuleList(wxMsgRule);
        return getDataTable(list);
    }

    /**
     * 导出推送规则列表
     */
    @PreAuthorize("@ss.hasPermi('wx:msgRule:export')")
    @Log(title = "推送规则", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WxMsgRule wxMsgRule)
    {
        List<WxMsgRule> list = wxMsgRuleService.selectWxMsgRuleList(wxMsgRule);
        ExcelUtil<WxMsgRule> util = new ExcelUtil<WxMsgRule>(WxMsgRule.class);
        util.exportExcel(response, list, "推送规则数据");
    }

    /**
     * 获取推送规则详细信息
     */
    @PreAuthorize("@ss.hasPermi('wx:msgRule:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wxMsgRuleService.selectWxMsgRuleById(id));
    }

    /**
     * 新增推送规则
     */
    @PreAuthorize("@ss.hasPermi('wx:msgRule:add')")
    @Log(title = "推送规则", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WxMsgRule wxMsgRule)
    {
        return toAjax(wxMsgRuleService.insertWxMsgRule(wxMsgRule));
    }

    /**
     * 修改推送规则
     */
    @PreAuthorize("@ss.hasPermi('wx:msgRule:edit')")
    @Log(title = "推送规则", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WxMsgRule wxMsgRule)
    {
        return toAjax(wxMsgRuleService.updateWxMsgRule(wxMsgRule));
    }

    /**
     * 删除推送规则
     */
    @PreAuthorize("@ss.hasPermi('wx:msgRule:remove')")
    @Log(title = "推送规则", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wxMsgRuleService.deleteWxMsgRuleByIds(ids));
    }
}
