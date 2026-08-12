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
import com.jonlink.system.domain.WxMpUser;
import com.jonlink.system.service.IWxMpUserService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 粉丝管理Controller
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@RestController
@RequestMapping("/wx/user")
public class WxMpUserController extends BaseController
{
    @Autowired
    private IWxMpUserService wxMpUserService;

    /**
     * 查询粉丝管理列表
     */
    @PreAuthorize("@ss.hasPermi('wx:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(WxMpUser wxMpUser)
    {
        startPage();
        List<WxMpUser> list = wxMpUserService.selectWxMpUserList(wxMpUser);
        return getDataTable(list);
    }

    /**
     * 导出粉丝管理列表
     */
    @PreAuthorize("@ss.hasPermi('wx:user:export')")
    @Log(title = "粉丝管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WxMpUser wxMpUser)
    {
        List<WxMpUser> list = wxMpUserService.selectWxMpUserList(wxMpUser);
        ExcelUtil<WxMpUser> util = new ExcelUtil<WxMpUser>(WxMpUser.class);
        util.exportExcel(response, list, "粉丝管理数据");
    }

    /**
     * 获取粉丝管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('wx:user:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wxMpUserService.selectWxMpUserById(id));
    }

    /**
     * 新增粉丝管理
     */
    @PreAuthorize("@ss.hasPermi('wx:user:add')")
    @Log(title = "粉丝管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WxMpUser wxMpUser)
    {
        return toAjax(wxMpUserService.insertWxMpUser(wxMpUser));
    }

    /**
     * 修改粉丝管理
     */
    @PreAuthorize("@ss.hasPermi('wx:user:edit')")
    @Log(title = "粉丝管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WxMpUser wxMpUser)
    {
        return toAjax(wxMpUserService.updateWxMpUser(wxMpUser));
    }

    /**
     * 删除粉丝管理
     */
    @PreAuthorize("@ss.hasPermi('wx:user:remove')")
    @Log(title = "粉丝管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wxMpUserService.deleteWxMpUserByIds(ids));
    }
}
