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
import com.jonlink.system.domain.JonlinkSettleRecord;
import com.jonlink.system.service.IJonlinkSettleRecordService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 结算记录Controller
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@RestController
@RequestMapping("/ledger/settleRecord")
public class JonlinkSettleRecordController extends BaseController
{
    @Autowired
    private IJonlinkSettleRecordService jonlinkSettleRecordService;

    /**
     * 查询结算记录列表
     */
    @PreAuthorize("@ss.hasPermi('ledger:settleRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(JonlinkSettleRecord jonlinkSettleRecord)
    {
        startPage();
        List<JonlinkSettleRecord> list = jonlinkSettleRecordService.selectJonlinkSettleRecordList(jonlinkSettleRecord);
        return getDataTable(list);
    }

    /**
     * 导出结算记录列表
     */
    @PreAuthorize("@ss.hasPermi('ledger:settleRecord:export')")
    @Log(title = "结算记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JonlinkSettleRecord jonlinkSettleRecord)
    {
        List<JonlinkSettleRecord> list = jonlinkSettleRecordService.selectJonlinkSettleRecordList(jonlinkSettleRecord);
        ExcelUtil<JonlinkSettleRecord> util = new ExcelUtil<JonlinkSettleRecord>(JonlinkSettleRecord.class);
        util.exportExcel(response, list, "结算记录数据");
    }

    /**
     * 获取结算记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('ledger:settleRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(jonlinkSettleRecordService.selectJonlinkSettleRecordById(id));
    }

    /**
     * 新增结算记录
     */
    @PreAuthorize("@ss.hasPermi('ledger:settleRecord:add')")
    @Log(title = "结算记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JonlinkSettleRecord jonlinkSettleRecord)
    {
        return toAjax(jonlinkSettleRecordService.insertJonlinkSettleRecord(jonlinkSettleRecord));
    }

    /**
     * 修改结算记录
     */
    @PreAuthorize("@ss.hasPermi('ledger:settleRecord:edit')")
    @Log(title = "结算记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JonlinkSettleRecord jonlinkSettleRecord)
    {
        return toAjax(jonlinkSettleRecordService.updateJonlinkSettleRecord(jonlinkSettleRecord));
    }

    /**
     * 删除结算记录
     */
    @PreAuthorize("@ss.hasPermi('ledger:settleRecord:remove')")
    @Log(title = "结算记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(jonlinkSettleRecordService.deleteJonlinkSettleRecordByIds(ids));
    }
}
