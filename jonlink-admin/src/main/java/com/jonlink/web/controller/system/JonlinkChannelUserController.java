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
import com.jonlink.system.domain.JonlinkChannelUser;
import com.jonlink.system.service.IJonlinkChannelUserService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 渠道/业务员档案Controller
 *
 * @author jonlink
 * @date 2026-08-13
 */
@RestController
@RequestMapping("/ledger/channelUser")
public class JonlinkChannelUserController extends BaseController
{
    @Autowired
    private IJonlinkChannelUserService jonlinkChannelUserService;

    /**
     * 查询渠道/业务员列表
     */
    @PreAuthorize("@ss.hasPermi('ledger:channelUser:list')")
    @GetMapping("/list")
    public TableDataInfo list(JonlinkChannelUser jonlinkChannelUser)
    {
        startPage();
        List<JonlinkChannelUser> list = jonlinkChannelUserService.selectJonlinkChannelUserList(jonlinkChannelUser);
        return getDataTable(list);
    }

    /**
     * 导出渠道/业务员列表
     */
    @PreAuthorize("@ss.hasPermi('ledger:channelUser:export')")
    @Log(title = "渠道/业务员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JonlinkChannelUser jonlinkChannelUser)
    {
        List<JonlinkChannelUser> list = jonlinkChannelUserService.selectJonlinkChannelUserList(jonlinkChannelUser);
        ExcelUtil<JonlinkChannelUser> util = new ExcelUtil<JonlinkChannelUser>(JonlinkChannelUser.class);
        util.exportExcel(response, list, "渠道/业务员数据");
    }

    /**
     * 获取渠道/业务员详细信息
     */
    @PreAuthorize("@ss.hasPermi('ledger:channelUser:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(jonlinkChannelUserService.selectJonlinkChannelUserById(id));
    }

    /**
     * 查询启用的渠道/业务员（下拉用，台账渠道类型=2 数据源）
     */
    @GetMapping("/options")
    public AjaxResult options()
    {
        return success(jonlinkChannelUserService.selectJonlinkChannelUserOptions());
    }

    /**
     * 新增渠道/业务员
     */
    @PreAuthorize("@ss.hasPermi('ledger:channelUser:add')")
    @Log(title = "渠道/业务员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JonlinkChannelUser jonlinkChannelUser)
    {
        return toAjax(jonlinkChannelUserService.insertJonlinkChannelUser(jonlinkChannelUser));
    }

    /**
     * 修改渠道/业务员
     */
    @PreAuthorize("@ss.hasPermi('ledger:channelUser:edit')")
    @Log(title = "渠道/业务员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JonlinkChannelUser jonlinkChannelUser)
    {
        return toAjax(jonlinkChannelUserService.updateJonlinkChannelUser(jonlinkChannelUser));
    }

    /**
     * 删除渠道/业务员
     */
    @PreAuthorize("@ss.hasPermi('ledger:channelUser:remove')")
    @Log(title = "渠道/业务员", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(jonlinkChannelUserService.deleteJonlinkChannelUserByIds(ids));
    }
}
