package com.jonlink.web.controller.system;

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
import com.jonlink.system.domain.WxH5Page;
import com.jonlink.system.service.IWxH5PageService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * H5页面Controller
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@RestController
@RequestMapping("/wx/h5Page")
public class WxH5PageController extends BaseController
{
    @Autowired
    private IWxH5PageService wxH5PageService;

    /**
     * 查询H5页面列表
     */
    @PreAuthorize("@ss.hasPermi('wx:h5Page:list')")
    @GetMapping("/list")
    public TableDataInfo list(WxH5Page wxH5Page)
    {
        startPage();
        List<WxH5Page> list = wxH5PageService.selectWxH5PageList(wxH5Page);
        return getDataTable(list);
    }

    /**
     * 导出H5页面列表
     */
    @PreAuthorize("@ss.hasPermi('wx:h5Page:export')")
    @Log(title = "H5页面", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WxH5Page wxH5Page)
    {
        List<WxH5Page> list = wxH5PageService.selectWxH5PageList(wxH5Page);
        ExcelUtil<WxH5Page> util = new ExcelUtil<WxH5Page>(WxH5Page.class);
        util.exportExcel(response, list, "H5页面数据");
    }

    /**
     * 获取H5页面详细信息
     */
    @PreAuthorize("@ss.hasPermi('wx:h5Page:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wxH5PageService.selectWxH5PageById(id));
    }

    /**
     * 新增H5页面
     */
    @PreAuthorize("@ss.hasPermi('wx:h5Page:add')")
    @Log(title = "H5页面", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WxH5Page wxH5Page)
    {
        return toAjax(wxH5PageService.insertWxH5Page(wxH5Page));
    }

    /**
     * 修改H5页面
     */
    @PreAuthorize("@ss.hasPermi('wx:h5Page:edit')")
    @Log(title = "H5页面", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WxH5Page wxH5Page)
    {
        return toAjax(wxH5PageService.updateWxH5Page(wxH5Page));
    }

    /**
     * 删除H5页面
     */
    @PreAuthorize("@ss.hasPermi('wx:h5Page:remove')")
    @Log(title = "H5页面", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wxH5PageService.deleteWxH5PageByIds(ids));
    }

    /**
     * 生成 H5 访问链接（ticket 24h 防伪，绑定 openid）
     */
    @PreAuthorize("@ss.hasPermi('wx:h5Page:query')")
    @PostMapping("/ticket")
    public AjaxResult genTicket(@RequestBody Map<String, Object> body)
    {
        String openid = String.valueOf(body.getOrDefault("openid", ""));
        if (openid.isEmpty() || "null".equals(openid))
        {
            return error("openid 不能为空");
        }
        com.jonlink.system.wx.service.TicketService ticketService =
                com.jonlink.common.utils.spring.SpringUtils.getBean(com.jonlink.system.wx.service.TicketService.class);
        String ticket = ticketService.create(openid);
        if (ticket == null)
        {
            return error("ticket 生成失败");
        }
        String path = String.valueOf(body.getOrDefault("pagePath", "/h5/order-dashboard.html"));
        return success(Map.of("ticket", ticket, "url", path + "?ticket=" + ticket));
    }
}
