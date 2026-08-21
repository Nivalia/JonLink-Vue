package com.jonlink.web.controller.system;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.jonlink.common.annotation.Log;
import com.jonlink.common.core.controller.BaseController;
import com.jonlink.common.core.domain.AjaxResult;
import com.jonlink.common.core.page.TableDataInfo;
import com.jonlink.common.enums.BusinessType;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.system.domain.PolicyCategory;
import com.jonlink.system.service.IPolicyCategoryService;

/**
 * 政策分类 信息操作处理
 *
 * @author jonlink
 * @date 2026-08-22
 */
@RestController
@RequestMapping("/policy/category")
public class PolicyCategoryController extends BaseController
{
    @Autowired
    private IPolicyCategoryService categoryService;

    /** 后台分页列表 */
    @PreAuthorize("@ss.hasPermi('policy:category:list')")
    @GetMapping("/list")
    public TableDataInfo list(PolicyCategory category) {
        startPage();
        List<PolicyCategory> list = categoryService.selectPolicyCategoryList(category);
        return getDataTable(list);
    }

    /** 树列表(展示端调用) */
    @PreAuthorize("@ss.hasPermi('policy:view:list')")
    @GetMapping("/tree")
    public AjaxResult tree(PolicyCategory category) {
        // 展示端只显示启用中的分类
        category.setStatus("1");
        List<PolicyCategory> tree = categoryService.selectPolicyCategoryTree(category);
        return success(tree);
    }

    @Log(title = "政策分类", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('policy:category:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, PolicyCategory category) {
        List<PolicyCategory> list = categoryService.selectPolicyCategoryList(category);
        ExcelUtil<PolicyCategory> util = new ExcelUtil<>(PolicyCategory.class);
        util.exportExcel(response, list, "政策分类数据");
    }

    @PreAuthorize("@ss.hasPermi('policy:category:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(categoryService.selectPolicyCategoryById(id));
    }

    @PreAuthorize("@ss.hasPermi('policy:category:add')")
    @Log(title = "政策分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody PolicyCategory category) {
        category.setCreateBy(getUsername());
        return toAjax(categoryService.insertPolicyCategory(category));
    }

    @PreAuthorize("@ss.hasPermi('policy:category:edit')")
    @Log(title = "政策分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody PolicyCategory category) {
        category.setUpdateBy(getUsername());
        return toAjax(categoryService.updatePolicyCategory(category));
    }

    @PreAuthorize("@ss.hasPermi('policy:category:remove')")
    @Log(title = "政策分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(categoryService.deletePolicyCategoryByIds(ids));
    }
}
