package com.jonlink.web.controller.wx;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jonlink.common.annotation.Log;
import com.jonlink.common.core.controller.BaseController;
import com.jonlink.common.core.domain.AjaxResult;
import com.jonlink.common.core.page.TableDataInfo;
import com.jonlink.common.enums.BusinessType;
import com.jonlink.system.domain.WxMpCallbackLog;
import com.jonlink.system.wx.service.IWxMpCallbackLogService;

/**
 * 公众号回调日志 Controller (README §12 菜单 2010)
 * 
 * @author jonlink
 */
@RestController
@RequestMapping("/wx/wxlog")
public class WxMpCallbackLogController extends BaseController
{
    @Autowired
    private IWxMpCallbackLogService wxMpCallbackLogService;

    /**
     * 查询回调日志列表
     */
    @PreAuthorize("@ss.hasPermi('wx:log:list')")
    @GetMapping("/list")
    public TableDataInfo list(WxMpCallbackLog wxMpCallbackLog)
    {
        startPage();
        List<WxMpCallbackLog> list = wxMpCallbackLogService.selectWxMpCallbackLogList(wxMpCallbackLog);
        return getDataTable(list);
    }

    /**
     * 获取回调日志详细
     */
    @PreAuthorize("@ss.hasPermi('wx:log:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wxMpCallbackLogService.selectWxMpCallbackLogById(id));
    }

    /**
     * 删除回调日志
     */
    @PreAuthorize("@ss.hasPermi('wx:log:remove')")
    @Log(title = "回调日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        int n = 0;
        for (Long id : ids)
        {
            n += wxMpCallbackLogService.deleteWxMpCallbackLogById(id);
        }
        return toAjax(n);
    }

    /**
     * 清空回调日志
     */
    @PreAuthorize("@ss.hasPermi('wx:log:remove')")
    @Log(title = "回调日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clear")
    public AjaxResult clear()
    {
        return toAjax(wxMpCallbackLogService.deleteAllWxMpCallbackLog());
    }
}
