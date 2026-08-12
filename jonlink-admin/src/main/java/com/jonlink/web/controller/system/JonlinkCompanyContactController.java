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
import com.jonlink.system.domain.JonlinkCompanyContact;
import com.jonlink.system.service.IJonlinkCompanyContactService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 公司联系人Controller
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@RestController
@RequestMapping("/ledger/companyContact")
public class JonlinkCompanyContactController extends BaseController
{
    @Autowired
    private IJonlinkCompanyContactService jonlinkCompanyContactService;

    /**
     * 查询公司联系人列表
     */
    @PreAuthorize("@ss.hasPermi('ledger:companyContact:list')")
    @GetMapping("/list")
    public TableDataInfo list(JonlinkCompanyContact jonlinkCompanyContact)
    {
        startPage();
        List<JonlinkCompanyContact> list = jonlinkCompanyContactService.selectJonlinkCompanyContactList(jonlinkCompanyContact);
        return getDataTable(list);
    }

    /**
     * 导出公司联系人列表
     */
    @PreAuthorize("@ss.hasPermi('ledger:companyContact:export')")
    @Log(title = "公司联系人", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JonlinkCompanyContact jonlinkCompanyContact)
    {
        List<JonlinkCompanyContact> list = jonlinkCompanyContactService.selectJonlinkCompanyContactList(jonlinkCompanyContact);
        ExcelUtil<JonlinkCompanyContact> util = new ExcelUtil<JonlinkCompanyContact>(JonlinkCompanyContact.class);
        util.exportExcel(response, list, "公司联系人数据");
    }

    /**
     * 获取公司联系人详细信息
     */
    @PreAuthorize("@ss.hasPermi('ledger:companyContact:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(jonlinkCompanyContactService.selectJonlinkCompanyContactById(id));
    }

    /**
     * 新增公司联系人
     */
    @PreAuthorize("@ss.hasPermi('ledger:companyContact:add')")
    @Log(title = "公司联系人", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JonlinkCompanyContact jonlinkCompanyContact)
    {
        return toAjax(jonlinkCompanyContactService.insertJonlinkCompanyContact(jonlinkCompanyContact));
    }

    /**
     * 修改公司联系人
     */
    @PreAuthorize("@ss.hasPermi('ledger:companyContact:edit')")
    @Log(title = "公司联系人", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JonlinkCompanyContact jonlinkCompanyContact)
    {
        return toAjax(jonlinkCompanyContactService.updateJonlinkCompanyContact(jonlinkCompanyContact));
    }

    /**
     * 删除公司联系人
     */
    @PreAuthorize("@ss.hasPermi('ledger:companyContact:remove')")
    @Log(title = "公司联系人", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(jonlinkCompanyContactService.deleteJonlinkCompanyContactByIds(ids));
    }
}
