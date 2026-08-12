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
import com.jonlink.system.domain.WxBizOrder;
import com.jonlink.system.service.IWxBizOrderService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 核销管理Controller
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@RestController
@RequestMapping("/wx/order")
public class WxBizOrderController extends BaseController
{
    @Autowired
    private IWxBizOrderService wxBizOrderService;

    /**
     * 查询核销管理列表
     */
    @PreAuthorize("@ss.hasPermi('wx:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(WxBizOrder wxBizOrder)
    {
        startPage();
        List<WxBizOrder> list = wxBizOrderService.selectWxBizOrderList(wxBizOrder);
        return getDataTable(list);
    }

    /**
     * 导出核销管理列表
     */
    @PreAuthorize("@ss.hasPermi('wx:order:export')")
    @Log(title = "核销管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WxBizOrder wxBizOrder)
    {
        List<WxBizOrder> list = wxBizOrderService.selectWxBizOrderList(wxBizOrder);
        ExcelUtil<WxBizOrder> util = new ExcelUtil<WxBizOrder>(WxBizOrder.class);
        util.exportExcel(response, list, "核销管理数据");
    }

    /**
     * 获取核销管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('wx:order:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wxBizOrderService.selectWxBizOrderById(id));
    }

    /**
     * 新增核销管理
     */
    @PreAuthorize("@ss.hasPermi('wx:order:add')")
    @Log(title = "核销管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WxBizOrder wxBizOrder)
    {
        return toAjax(wxBizOrderService.insertWxBizOrder(wxBizOrder));
    }

    /**
     * 修改核销管理
     */
    @PreAuthorize("@ss.hasPermi('wx:order:edit')")
    @Log(title = "核销管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WxBizOrder wxBizOrder)
    {
        return toAjax(wxBizOrderService.updateWxBizOrder(wxBizOrder));
    }

    /**
     * 删除核销管理
     */
    @PreAuthorize("@ss.hasPermi('wx:order:remove')")
    @Log(title = "核销管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wxBizOrderService.deleteWxBizOrderByIds(ids));
    }
}
