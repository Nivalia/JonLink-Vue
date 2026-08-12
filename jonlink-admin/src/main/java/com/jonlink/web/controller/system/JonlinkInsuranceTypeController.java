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
import com.jonlink.system.domain.JonlinkInsuranceType;
import com.jonlink.system.service.IJonlinkInsuranceTypeService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 险种管理Controller
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@RestController
@RequestMapping("/ledger/insuranceType")
public class JonlinkInsuranceTypeController extends BaseController
{
    @Autowired
    private IJonlinkInsuranceTypeService jonlinkInsuranceTypeService;

    /**
     * 查询险种管理列表
     */
    @PreAuthorize("@ss.hasPermi('ledger:insuranceType:list')")
    @GetMapping("/list")
    public TableDataInfo list(JonlinkInsuranceType jonlinkInsuranceType)
    {
        startPage();
        List<JonlinkInsuranceType> list = jonlinkInsuranceTypeService.selectJonlinkInsuranceTypeList(jonlinkInsuranceType);
        return getDataTable(list);
    }

    /**
     * 导出险种管理列表
     */
    @PreAuthorize("@ss.hasPermi('ledger:insuranceType:export')")
    @Log(title = "险种管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JonlinkInsuranceType jonlinkInsuranceType)
    {
        List<JonlinkInsuranceType> list = jonlinkInsuranceTypeService.selectJonlinkInsuranceTypeList(jonlinkInsuranceType);
        ExcelUtil<JonlinkInsuranceType> util = new ExcelUtil<JonlinkInsuranceType>(JonlinkInsuranceType.class);
        util.exportExcel(response, list, "险种管理数据");
    }

    /**
     * 获取险种管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('ledger:insuranceType:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(jonlinkInsuranceTypeService.selectJonlinkInsuranceTypeById(id));
    }

    /**
     * 新增险种管理
     */
    @PreAuthorize("@ss.hasPermi('ledger:insuranceType:add')")
    @Log(title = "险种管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JonlinkInsuranceType jonlinkInsuranceType)
    {
        return toAjax(jonlinkInsuranceTypeService.insertJonlinkInsuranceType(jonlinkInsuranceType));
    }

    /**
     * 修改险种管理
     */
    @PreAuthorize("@ss.hasPermi('ledger:insuranceType:edit')")
    @Log(title = "险种管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JonlinkInsuranceType jonlinkInsuranceType)
    {
        return toAjax(jonlinkInsuranceTypeService.updateJonlinkInsuranceType(jonlinkInsuranceType));
    }

    /**
     * 删除险种管理
     */
    @PreAuthorize("@ss.hasPermi('ledger:insuranceType:remove')")
    @Log(title = "险种管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(jonlinkInsuranceTypeService.deleteJonlinkInsuranceTypeByIds(ids));
    }
}
