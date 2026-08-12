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
import com.jonlink.system.domain.JonlinkInsuranceLedger;
import com.jonlink.system.service.IJonlinkInsuranceLedgerService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 保险台账Controller
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@RestController
@RequestMapping("/ledger/ledger")
public class JonlinkInsuranceLedgerController extends BaseController
{
    @Autowired
    private IJonlinkInsuranceLedgerService jonlinkInsuranceLedgerService;

    /**
     * 查询保险台账列表
     */
    @PreAuthorize("@ss.hasPermi('ledger:ledger:list')")
    @GetMapping("/list")
    public TableDataInfo list(JonlinkInsuranceLedger jonlinkInsuranceLedger)
    {
        startPage();
        List<JonlinkInsuranceLedger> list = jonlinkInsuranceLedgerService.selectJonlinkInsuranceLedgerList(jonlinkInsuranceLedger);
        return getDataTable(list);
    }

    /**
     * 导出保险台账列表
     */
    @PreAuthorize("@ss.hasPermi('ledger:ledger:export')")
    @Log(title = "保险台账", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JonlinkInsuranceLedger jonlinkInsuranceLedger)
    {
        List<JonlinkInsuranceLedger> list = jonlinkInsuranceLedgerService.selectJonlinkInsuranceLedgerList(jonlinkInsuranceLedger);
        ExcelUtil<JonlinkInsuranceLedger> util = new ExcelUtil<JonlinkInsuranceLedger>(JonlinkInsuranceLedger.class);
        util.exportExcel(response, list, "保险台账数据");
    }

    /**
     * 获取保险台账详细信息
     */
    @PreAuthorize("@ss.hasPermi('ledger:ledger:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(jonlinkInsuranceLedgerService.selectJonlinkInsuranceLedgerById(id));
    }

    /**
     * 新增保险台账
     */
    @PreAuthorize("@ss.hasPermi('ledger:ledger:add')")
    @Log(title = "保险台账", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JonlinkInsuranceLedger jonlinkInsuranceLedger)
    {
        return toAjax(jonlinkInsuranceLedgerService.insertJonlinkInsuranceLedger(jonlinkInsuranceLedger));
    }

    /**
     * 修改保险台账
     */
    @PreAuthorize("@ss.hasPermi('ledger:ledger:edit')")
    @Log(title = "保险台账", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JonlinkInsuranceLedger jonlinkInsuranceLedger)
    {
        return toAjax(jonlinkInsuranceLedgerService.updateJonlinkInsuranceLedger(jonlinkInsuranceLedger));
    }

    /**
     * 删除保险台账
     */
    @PreAuthorize("@ss.hasPermi('ledger:ledger:remove')")
    @Log(title = "保险台账", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(jonlinkInsuranceLedgerService.deleteJonlinkInsuranceLedgerByIds(ids));
    }
}
