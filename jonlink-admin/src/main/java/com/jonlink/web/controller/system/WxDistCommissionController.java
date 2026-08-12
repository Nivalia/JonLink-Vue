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
import com.jonlink.system.domain.WxDistCommission;
import com.jonlink.system.service.IWxDistCommissionService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 佣金积分Controller
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@RestController
@RequestMapping("/wx/commission")
public class WxDistCommissionController extends BaseController
{
    @Autowired
    private IWxDistCommissionService wxDistCommissionService;

    /**
     * 查询佣金积分列表
     */
    @PreAuthorize("@ss.hasPermi('wx:dist:commission:list')")
    @GetMapping("/list")
    public TableDataInfo list(WxDistCommission wxDistCommission)
    {
        startPage();
        List<WxDistCommission> list = wxDistCommissionService.selectWxDistCommissionList(wxDistCommission);
        return getDataTable(list);
    }

    /**
     * 导出佣金积分列表
     */
    @PreAuthorize("@ss.hasPermi('wx:dist:commission:export')")
    @Log(title = "佣金积分", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WxDistCommission wxDistCommission)
    {
        List<WxDistCommission> list = wxDistCommissionService.selectWxDistCommissionList(wxDistCommission);
        ExcelUtil<WxDistCommission> util = new ExcelUtil<WxDistCommission>(WxDistCommission.class);
        util.exportExcel(response, list, "佣金积分数据");
    }

    /**
     * 获取佣金积分详细信息
     */
    @PreAuthorize("@ss.hasPermi('wx:dist:commission:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wxDistCommissionService.selectWxDistCommissionById(id));
    }

    /**
     * 新增佣金积分
     */
    @PreAuthorize("@ss.hasPermi('wx:dist:commission:add')")
    @Log(title = "佣金积分", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WxDistCommission wxDistCommission)
    {
        return toAjax(wxDistCommissionService.insertWxDistCommission(wxDistCommission));
    }

    /**
     * 修改佣金积分
     */
    @PreAuthorize("@ss.hasPermi('wx:dist:commission:edit')")
    @Log(title = "佣金积分", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WxDistCommission wxDistCommission)
    {
        return toAjax(wxDistCommissionService.updateWxDistCommission(wxDistCommission));
    }

    /**
     * 删除佣金积分
     */
    @PreAuthorize("@ss.hasPermi('wx:dist:commission:remove')")
    @Log(title = "佣金积分", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wxDistCommissionService.deleteWxDistCommissionByIds(ids));
    }
}
