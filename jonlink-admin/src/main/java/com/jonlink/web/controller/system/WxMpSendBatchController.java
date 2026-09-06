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
import com.jonlink.system.domain.WxMpSendBatch;
import com.jonlink.system.service.IWxMpSendBatchService;
import com.jonlink.system.wx.service.WxMsgCheckService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 发送批次Controller
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@RestController
@RequestMapping("/wx/batch")
public class WxMpSendBatchController extends BaseController
{
    @Autowired
    private IWxMpSendBatchService wxMpSendBatchService;
    @Autowired
    private WxMsgCheckService wxMsgCheckService;

    /**
     * 查询发送批次列表
     */
    @PreAuthorize("@ss.hasPermi('wx:batch:list')")
    @GetMapping("/list")
    public TableDataInfo list(WxMpSendBatch wxMpSendBatch)
    {
        startPage();
        List<WxMpSendBatch> list = wxMpSendBatchService.selectWxMpSendBatchList(wxMpSendBatch);
        return getDataTable(list);
    }

    /**
     * 导出发送批次列表
     */
    @PreAuthorize("@ss.hasPermi('wx:batch:export')")
    @Log(title = "发送批次", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WxMpSendBatch wxMpSendBatch)
    {
        List<WxMpSendBatch> list = wxMpSendBatchService.selectWxMpSendBatchList(wxMpSendBatch);
        ExcelUtil<WxMpSendBatch> util = new ExcelUtil<WxMpSendBatch>(WxMpSendBatch.class);
        util.exportExcel(response, list, "发送批次数据");
    }

    /**
     * 获取发送批次详细信息
     */
    @PreAuthorize("@ss.hasPermi('wx:batch:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wxMpSendBatchService.selectWxMpSendBatchById(id));
    }

    /**
     * 新增发送批次
     */
    @PreAuthorize("@ss.hasPermi('wx:batch:add')")
    @Log(title = "发送批次", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WxMpSendBatch wxMpSendBatch)
    {
        return toAjax(wxMpSendBatchService.insertWxMpSendBatch(wxMpSendBatch));
    }

    /**
     * 修改发送批次
     */
    @PreAuthorize("@ss.hasPermi('wx:batch:edit')")
    @Log(title = "发送批次", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WxMpSendBatch wxMpSendBatch)
    {
        return toAjax(wxMpSendBatchService.updateWxMpSendBatch(wxMpSendBatch));
    }

    /**
     * 删除发送批次
     */
    @PreAuthorize("@ss.hasPermi('wx:batch:remove')")
    @Log(title = "发送批次", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wxMpSendBatchService.deleteWxMpSendBatchByIds(ids));
    }

    /**
     * 推送核对: 仅对 Excel 批量导入的推送(source=1)做核对。幂等,重复调用结果相同。
     */
    @PreAuthorize("@ss.hasPermi('wx:batch:edit')")
    @Log(title = "发送批次", businessType = BusinessType.UPDATE)
    @GetMapping("/check")
    public AjaxResult check(@org.springframework.web.bind.annotation.RequestParam(required = true) String batchNo)
    {
        if (batchNo == null || batchNo.isEmpty())
        {
            return error("批次号不能为空");
        }
        WxMsgCheckService.CheckResult r = wxMsgCheckService.checkBatch(batchNo);
        return success("候选 " + r.total + " 通过 " + r.passed + " 失败 " + r.failed);
    }
}
