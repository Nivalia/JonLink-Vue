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
import com.jonlink.system.domain.JonlinkChannel;
import com.jonlink.system.service.IJonlinkChannelService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 上游渠道商Controller
 *
 * @author jonlink
 * @date 2026-08-13
 */
@RestController
@RequestMapping("/ledger/channel")
public class JonlinkChannelController extends BaseController
{
    @Autowired
    private IJonlinkChannelService jonlinkChannelService;

    /**
     * 查询上游渠道商列表
     */
    @PreAuthorize("@ss.hasPermi('ledger:channel:list')")
    @GetMapping("/list")
    public TableDataInfo list(JonlinkChannel jonlinkChannel)
    {
        startPage();
        List<JonlinkChannel> list = jonlinkChannelService.selectJonlinkChannelList(jonlinkChannel);
        return getDataTable(list);
    }

    /**
     * 导出上游渠道商列表
     */
    @PreAuthorize("@ss.hasPermi('ledger:channel:export')")
    @Log(title = "上游渠道商", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JonlinkChannel jonlinkChannel)
    {
        List<JonlinkChannel> list = jonlinkChannelService.selectJonlinkChannelList(jonlinkChannel);
        ExcelUtil<JonlinkChannel> util = new ExcelUtil<JonlinkChannel>(JonlinkChannel.class);
        util.exportExcel(response, list, "上游渠道商数据");
    }

    /**
     * 获取上游渠道商详细信息
     */
    @PreAuthorize("@ss.hasPermi('ledger:channel:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(jonlinkChannelService.selectJonlinkChannelById(id));
    }

    /**
     * 查询启用的渠道商（下拉用）
     */
    @GetMapping("/options")
    public AjaxResult options()
    {
        return success(jonlinkChannelService.selectJonlinkChannelOptions());
    }

    /**
     * 新增上游渠道商
     */
    @PreAuthorize("@ss.hasPermi('ledger:channel:add')")
    @Log(title = "上游渠道商", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JonlinkChannel jonlinkChannel)
    {
        return toAjax(jonlinkChannelService.insertJonlinkChannel(jonlinkChannel));
    }

    /**
     * 修改上游渠道商
     */
    @PreAuthorize("@ss.hasPermi('ledger:channel:edit')")
    @Log(title = "上游渠道商", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JonlinkChannel jonlinkChannel)
    {
        return toAjax(jonlinkChannelService.updateJonlinkChannel(jonlinkChannel));
    }

    /**
     * 删除上游渠道商
     */
    @PreAuthorize("@ss.hasPermi('ledger:channel:remove')")
    @Log(title = "上游渠道商", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(jonlinkChannelService.deleteJonlinkChannelByIds(ids));
    }
}
