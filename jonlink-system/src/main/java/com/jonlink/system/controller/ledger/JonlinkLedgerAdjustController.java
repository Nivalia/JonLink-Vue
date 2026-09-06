package com.jonlink.system.controller.ledger;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jonlink.common.annotation.Log;
import com.jonlink.common.core.controller.BaseController;
import com.jonlink.common.core.domain.AjaxResult;
import com.jonlink.common.core.page.TableDataInfo;
import com.jonlink.common.enums.BusinessType;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.system.domain.JonlinkLedgerAdjust;
import com.jonlink.system.service.IJonlinkLedgerAdjustService;

/**
 * 台账批增退记录Controller
 * 
 * @author jonlink
 * @date 2026-09-05
 */
@RestController
@RequestMapping("/ledger/adjust")
public class JonlinkLedgerAdjustController extends BaseController
{
    @Autowired
    private IJonlinkLedgerAdjustService jonlinkLedgerAdjustService;

    /**
     * 查询台账批增退记录列表
     */
    @PreAuthorize("@ss.hasPermi('ledger:adjust:list')")
    @GetMapping("/list")
    public TableDataInfo list(JonlinkLedgerAdjust jonlinkLedgerAdjust)
    {
        startPage();
        List<JonlinkLedgerAdjust> list = jonlinkLedgerAdjustService.selectJonlinkLedgerAdjustList(jonlinkLedgerAdjust);
        return getDataTable(list);
    }

    /**
     * 导出台账批增退记录列表
     */
    @PreAuthorize("@ss.hasPermi('ledger:adjust:export')")
    @Log(title = "台账批增退记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JonlinkLedgerAdjust jonlinkLedgerAdjust)
    {
        List<JonlinkLedgerAdjust> list = jonlinkLedgerAdjustService.selectJonlinkLedgerAdjustList(jonlinkLedgerAdjust);
        ExcelUtil<JonlinkLedgerAdjust> util = new ExcelUtil<JonlinkLedgerAdjust>(JonlinkLedgerAdjust.class);
        util.exportExcel(response, list, "台账批增退记录数据");
    }

    /**
     * 获取台账批增退记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('ledger:adjust:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(jonlinkLedgerAdjustService.selectJonlinkLedgerAdjustById(id));
    }

    /**
     * 新增台账批增退记录
     */
    @PreAuthorize("@ss.hasPermi('ledger:adjust:add')")
    @Log(title = "台账批增退记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JonlinkLedgerAdjust jonlinkLedgerAdjust)
    {
        return toAjax(jonlinkLedgerAdjustService.insertJonlinkLedgerAdjust(jonlinkLedgerAdjust));
    }

    /**
     * 修改台账批增退记录
     */
    @PreAuthorize("@ss.hasPermi('ledger:adjust:edit')")
    @Log(title = "台账批增退记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JonlinkLedgerAdjust jonlinkLedgerAdjust)
    {
        return toAjax(jonlinkLedgerAdjustService.updateJonlinkLedgerAdjust(jonlinkLedgerAdjust));
    }

    /**
     * 删除台账批增退记录
     */
    @PreAuthorize("@ss.hasPermi('ledger:adjust:remove')")
    @Log(title = "台账批增退记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(jonlinkLedgerAdjustService.deleteJonlinkLedgerAdjustByIds(ids));
    }

    /**
     * 执行批增/批退操作
     */
    @PreAuthorize("@ss.hasPermi('ledger:adjust:edit')")
    @Log(title = "台账批增退", businessType = BusinessType.UPDATE)
    @PostMapping("/execute")
    public AjaxResult execute(@RequestBody JonlinkLedgerAdjust adjust)
    {
        return toAjax(jonlinkLedgerAdjustService.executeAdjust(adjust));
    }
}
