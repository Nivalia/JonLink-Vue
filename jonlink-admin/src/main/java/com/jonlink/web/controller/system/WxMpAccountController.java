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
import com.jonlink.system.domain.WxMpAccount;
import com.jonlink.system.service.IWxMpAccountService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 账号配置Controller
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@RestController
@RequestMapping("/wx/account")
public class WxMpAccountController extends BaseController
{
    @Autowired
    private IWxMpAccountService wxMpAccountService;

    /**
     * 查询账号配置列表
     */
    @PreAuthorize("@ss.hasPermi('wx:account:list')")
    @GetMapping("/list")
    public TableDataInfo list(WxMpAccount wxMpAccount)
    {
        startPage();
        List<WxMpAccount> list = wxMpAccountService.selectWxMpAccountList(wxMpAccount);
        return getDataTable(list);
    }

    /**
     * 导出账号配置列表
     */
    @PreAuthorize("@ss.hasPermi('wx:account:export')")
    @Log(title = "账号配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WxMpAccount wxMpAccount)
    {
        List<WxMpAccount> list = wxMpAccountService.selectWxMpAccountList(wxMpAccount);
        ExcelUtil<WxMpAccount> util = new ExcelUtil<WxMpAccount>(WxMpAccount.class);
        util.exportExcel(response, list, "账号配置数据");
    }

    /**
     * 获取账号配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('wx:account:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wxMpAccountService.selectWxMpAccountById(id));
    }

    /**
     * 新增账号配置
     */
    @PreAuthorize("@ss.hasPermi('wx:account:add')")
    @Log(title = "账号配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WxMpAccount wxMpAccount)
    {
        return toAjax(wxMpAccountService.insertWxMpAccount(wxMpAccount));
    }

    /**
     * 修改账号配置
     */
    @PreAuthorize("@ss.hasPermi('wx:account:edit')")
    @Log(title = "账号配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WxMpAccount wxMpAccount)
    {
        return toAjax(wxMpAccountService.updateWxMpAccount(wxMpAccount));
    }

    /**
     * 删除账号配置
     */
    @PreAuthorize("@ss.hasPermi('wx:account:remove')")
    @Log(title = "账号配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wxMpAccountService.deleteWxMpAccountByIds(ids));
    }
}
