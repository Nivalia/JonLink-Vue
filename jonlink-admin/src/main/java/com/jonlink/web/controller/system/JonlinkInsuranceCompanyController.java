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
import com.jonlink.system.domain.JonlinkInsuranceCompany;
import com.jonlink.system.service.IJonlinkInsuranceCompanyService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 保险公司Controller
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@RestController
@RequestMapping("/ledger/company")
public class JonlinkInsuranceCompanyController extends BaseController
{
    @Autowired
    private IJonlinkInsuranceCompanyService jonlinkInsuranceCompanyService;

    /**
     * 查询保险公司列表
     */
    @PreAuthorize("@ss.hasPermi('ledger:company:list')")
    @GetMapping("/list")
    public TableDataInfo list(JonlinkInsuranceCompany jonlinkInsuranceCompany)
    {
        startPage();
        List<JonlinkInsuranceCompany> list = jonlinkInsuranceCompanyService.selectJonlinkInsuranceCompanyList(jonlinkInsuranceCompany);
        return getDataTable(list);
    }

    /**
     * 导出保险公司列表
     */
    @PreAuthorize("@ss.hasPermi('ledger:company:export')")
    @Log(title = "保险公司", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JonlinkInsuranceCompany jonlinkInsuranceCompany)
    {
        List<JonlinkInsuranceCompany> list = jonlinkInsuranceCompanyService.selectJonlinkInsuranceCompanyList(jonlinkInsuranceCompany);
        ExcelUtil<JonlinkInsuranceCompany> util = new ExcelUtil<JonlinkInsuranceCompany>(JonlinkInsuranceCompany.class);
        util.exportExcel(response, list, "保险公司数据");
    }

    /**
     * 获取保险公司详细信息
     */
    @PreAuthorize("@ss.hasPermi('ledger:company:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(jonlinkInsuranceCompanyService.selectJonlinkInsuranceCompanyById(id));
    }

    /**
     * 新增保险公司
     */
    @PreAuthorize("@ss.hasPermi('ledger:company:add')")
    @Log(title = "保险公司", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JonlinkInsuranceCompany jonlinkInsuranceCompany)
    {
        return toAjax(jonlinkInsuranceCompanyService.insertJonlinkInsuranceCompany(jonlinkInsuranceCompany));
    }

    /**
     * 修改保险公司
     */
    @PreAuthorize("@ss.hasPermi('ledger:company:edit')")
    @Log(title = "保险公司", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JonlinkInsuranceCompany jonlinkInsuranceCompany)
    {
        return toAjax(jonlinkInsuranceCompanyService.updateJonlinkInsuranceCompany(jonlinkInsuranceCompany));
    }

    /**
     * 删除保险公司
     */
    @PreAuthorize("@ss.hasPermi('ledger:company:remove')")
    @Log(title = "保险公司", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(jonlinkInsuranceCompanyService.deleteJonlinkInsuranceCompanyByIds(ids));
    }
}
