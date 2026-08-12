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
import com.jonlink.system.domain.JonlinkContactPerson;
import com.jonlink.system.service.IJonlinkContactPersonService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 联系人管理Controller
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@RestController
@RequestMapping("/ledger/contact")
public class JonlinkContactPersonController extends BaseController
{
    @Autowired
    private IJonlinkContactPersonService jonlinkContactPersonService;

    /**
     * 查询联系人管理列表
     */
    @PreAuthorize("@ss.hasPermi('ledger:contact:list')")
    @GetMapping("/list")
    public TableDataInfo list(JonlinkContactPerson jonlinkContactPerson)
    {
        startPage();
        List<JonlinkContactPerson> list = jonlinkContactPersonService.selectJonlinkContactPersonList(jonlinkContactPerson);
        return getDataTable(list);
    }

    /**
     * 导出联系人管理列表
     */
    @PreAuthorize("@ss.hasPermi('ledger:contact:export')")
    @Log(title = "联系人管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JonlinkContactPerson jonlinkContactPerson)
    {
        List<JonlinkContactPerson> list = jonlinkContactPersonService.selectJonlinkContactPersonList(jonlinkContactPerson);
        ExcelUtil<JonlinkContactPerson> util = new ExcelUtil<JonlinkContactPerson>(JonlinkContactPerson.class);
        util.exportExcel(response, list, "联系人管理数据");
    }

    /**
     * 获取联系人管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('ledger:contact:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(jonlinkContactPersonService.selectJonlinkContactPersonById(id));
    }

    /**
     * 新增联系人管理
     */
    @PreAuthorize("@ss.hasPermi('ledger:contact:add')")
    @Log(title = "联系人管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JonlinkContactPerson jonlinkContactPerson)
    {
        return toAjax(jonlinkContactPersonService.insertJonlinkContactPerson(jonlinkContactPerson));
    }

    /**
     * 修改联系人管理
     */
    @PreAuthorize("@ss.hasPermi('ledger:contact:edit')")
    @Log(title = "联系人管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JonlinkContactPerson jonlinkContactPerson)
    {
        return toAjax(jonlinkContactPersonService.updateJonlinkContactPerson(jonlinkContactPerson));
    }

    /**
     * 删除联系人管理
     */
    @PreAuthorize("@ss.hasPermi('ledger:contact:remove')")
    @Log(title = "联系人管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(jonlinkContactPersonService.deleteJonlinkContactPersonByIds(ids));
    }
}
