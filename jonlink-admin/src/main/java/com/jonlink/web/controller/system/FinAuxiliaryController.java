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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.jonlink.common.annotation.Log;
import com.jonlink.common.core.controller.BaseController;
import com.jonlink.common.core.domain.AjaxResult;
import com.jonlink.common.enums.BusinessType;
import com.jonlink.system.domain.FinAuxiliary;
import com.jonlink.system.service.IFinAuxiliaryService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 辅助核算Controller
 * 
 * @author jonlink
 * @date 2026-08-23
 */
@RestController
@RequestMapping("/finance/auxiliary")
public class FinAuxiliaryController extends BaseController
{
    @Autowired
    private IFinAuxiliaryService finAuxiliaryService;

    /**
     * 查询辅助核算列表
     */
    @PreAuthorize("@ss.hasPermi('finance:auxiliary:list')")
    @GetMapping("/list")
    public TableDataInfo list(FinAuxiliary finAuxiliary)
    {
        startPage();
        List<FinAuxiliary> list = finAuxiliaryService.selectFinAuxiliaryList(finAuxiliary);
        return getDataTable(list);
    }

    /**
     * 导出辅助核算列表
     */
    @PreAuthorize("@ss.hasPermi('finance:auxiliary:export')")
    @Log(title = "辅助核算", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinAuxiliary finAuxiliary)
    {
        List<FinAuxiliary> list = finAuxiliaryService.selectFinAuxiliaryList(finAuxiliary);
        ExcelUtil<FinAuxiliary> util = new ExcelUtil<FinAuxiliary>(FinAuxiliary.class);
        util.exportExcel(response, list, "辅助核算数据");
    }

    /**
     * 获取辅助核算详细信息
     */
    @PreAuthorize("@ss.hasPermi('finance:auxiliary:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(finAuxiliaryService.selectFinAuxiliaryById(id));
    }

    /**
     * 新增辅助核算
     */
    @PreAuthorize("@ss.hasPermi('finance:auxiliary:add')")
    @Log(title = "辅助核算", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FinAuxiliary finAuxiliary)
    {
        return toAjax(finAuxiliaryService.insertFinAuxiliary(finAuxiliary));
    }

    /**
     * 修改辅助核算
     */
    @PreAuthorize("@ss.hasPermi('finance:auxiliary:edit')")
    @Log(title = "辅助核算", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FinAuxiliary finAuxiliary)
    {
        return toAjax(finAuxiliaryService.updateFinAuxiliary(finAuxiliary));
    }

    /**
     * 删除辅助核算
     */
    @PreAuthorize("@ss.hasPermi('finance:auxiliary:remove')")
    @Log(title = "辅助核算", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(finAuxiliaryService.deleteFinAuxiliaryByIds(ids));
    }

    /**
     * 按类型查询辅助核算
     */
    @PreAuthorize("@ss.hasPermi('finance:auxiliary:list')")
    @GetMapping("/type")
    public AjaxResult listByType(@RequestParam String auxType)
    {
        List<FinAuxiliary> list = finAuxiliaryService.selectFinAuxiliaryByType(auxType);
        return AjaxResult.success(list);
    }
}
