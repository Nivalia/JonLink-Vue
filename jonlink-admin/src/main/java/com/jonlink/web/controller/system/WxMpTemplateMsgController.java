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
import com.jonlink.system.domain.WxMpTemplateMsg;
import com.jonlink.system.service.IWxMpTemplateMsgService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 发送记录Controller
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@RestController
@RequestMapping("/wx/msg")
public class WxMpTemplateMsgController extends BaseController
{
    @Autowired
    private IWxMpTemplateMsgService wxMpTemplateMsgService;

    /**
     * 查询发送记录列表
     */
    @PreAuthorize("@ss.hasPermi('wx:msg:list')")
    @GetMapping("/list")
    public TableDataInfo list(WxMpTemplateMsg wxMpTemplateMsg)
    {
        startPage();
        List<WxMpTemplateMsg> list = wxMpTemplateMsgService.selectWxMpTemplateMsgList(wxMpTemplateMsg);
        return getDataTable(list);
    }

    /**
     * 导出发送记录列表
     */
    @PreAuthorize("@ss.hasPermi('wx:msg:export')")
    @Log(title = "发送记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WxMpTemplateMsg wxMpTemplateMsg)
    {
        List<WxMpTemplateMsg> list = wxMpTemplateMsgService.selectWxMpTemplateMsgList(wxMpTemplateMsg);
        ExcelUtil<WxMpTemplateMsg> util = new ExcelUtil<WxMpTemplateMsg>(WxMpTemplateMsg.class);
        util.exportExcel(response, list, "发送记录数据");
    }

    /**
     * 获取发送记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('wx:msg:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wxMpTemplateMsgService.selectWxMpTemplateMsgById(id));
    }

    /**
     * 新增发送记录
     */
    @PreAuthorize("@ss.hasPermi('wx:msg:add')")
    @Log(title = "发送记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WxMpTemplateMsg wxMpTemplateMsg)
    {
        return toAjax(wxMpTemplateMsgService.insertWxMpTemplateMsg(wxMpTemplateMsg));
    }

    /**
     * 修改发送记录
     */
    @PreAuthorize("@ss.hasPermi('wx:msg:edit')")
    @Log(title = "发送记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WxMpTemplateMsg wxMpTemplateMsg)
    {
        return toAjax(wxMpTemplateMsgService.updateWxMpTemplateMsg(wxMpTemplateMsg));
    }

    /**
     * 删除发送记录
     */
    @PreAuthorize("@ss.hasPermi('wx:msg:remove')")
    @Log(title = "发送记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wxMpTemplateMsgService.deleteWxMpTemplateMsgByIds(ids));
    }
}
