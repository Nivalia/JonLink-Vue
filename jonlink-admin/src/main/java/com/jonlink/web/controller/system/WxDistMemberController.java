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
import com.jonlink.system.domain.WxDistMember;
import com.jonlink.system.service.IWxDistMemberService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 分销员档案Controller
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@RestController
@RequestMapping("/wx/distMember")
public class WxDistMemberController extends BaseController
{
    @Autowired
    private IWxDistMemberService wxDistMemberService;

    /**
     * 查询分销员档案列表
     */
    @PreAuthorize("@ss.hasPermi('wx:dist:member:list')")
    @GetMapping("/list")
    public TableDataInfo list(WxDistMember wxDistMember)
    {
        startPage();
        List<WxDistMember> list = wxDistMemberService.selectWxDistMemberList(wxDistMember);
        return getDataTable(list);
    }

    /**
     * 导出分销员档案列表
     */
    @PreAuthorize("@ss.hasPermi('wx:dist:member:export')")
    @Log(title = "分销员档案", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WxDistMember wxDistMember)
    {
        List<WxDistMember> list = wxDistMemberService.selectWxDistMemberList(wxDistMember);
        ExcelUtil<WxDistMember> util = new ExcelUtil<WxDistMember>(WxDistMember.class);
        util.exportExcel(response, list, "分销员档案数据");
    }

    /**
     * 获取分销员档案详细信息
     */
    @PreAuthorize("@ss.hasPermi('wx:dist:member:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wxDistMemberService.selectWxDistMemberById(id));
    }

    /**
     * 新增分销员档案
     */
    @PreAuthorize("@ss.hasPermi('wx:dist:member:add')")
    @Log(title = "分销员档案", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WxDistMember wxDistMember)
    {
        return toAjax(wxDistMemberService.insertWxDistMember(wxDistMember));
    }

    /**
     * 修改分销员档案
     */
    @PreAuthorize("@ss.hasPermi('wx:dist:member:edit')")
    @Log(title = "分销员档案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WxDistMember wxDistMember)
    {
        return toAjax(wxDistMemberService.updateWxDistMember(wxDistMember));
    }

    /**
     * 删除分销员档案
     */
    @PreAuthorize("@ss.hasPermi('wx:dist:member:remove')")
    @Log(title = "分销员档案", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wxDistMemberService.deleteWxDistMemberByIds(ids));
    }
}
