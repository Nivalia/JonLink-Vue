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
import com.jonlink.system.domain.PolicyCategory;
import com.jonlink.system.service.IPolicyCategoryService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 政策分类Controller
 *
 * @author jonlink
 */
@RestController
@RequestMapping("/policy/category")
public class PolicyCategoryController extends BaseController
{
    @Autowired
    private IPolicyCategoryService policyCategoryService;

    /**
     * 查询政策分类列表
     */
    @PreAuthorize("@ss.hasPermi('policy:category:list')")
    @GetMapping("/list")
    public TableDataInfo list(PolicyCategory policyCategory)
    {
        startPage();
        List<PolicyCategory> list = policyCategoryService.selectPolicyCategoryList(policyCategory);
        return getDataTable(list);
    }

    /**
     * 查询政策分类树
     */
    @PreAuthorize("@ss.hasPermi('policy:category:list')")
    @GetMapping("/tree")
    public AjaxResult tree(PolicyCategory policyCategory)
    {
        policyCategory.setStatus(null);
        return success(policyCategoryService.selectPolicyCategoryTree(policyCategory));
    }

    /**
     * 导出政策分类列表
     */
    @PreAuthorize("@ss.hasPermi('policy:category:export')")
    @Log(title = "政策分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PolicyCategory policyCategory)
    {
        List<PolicyCategory> list = policyCategoryService.selectPolicyCategoryList(policyCategory);
        ExcelUtil<PolicyCategory> util = new ExcelUtil<PolicyCategory>(PolicyCategory.class);
        util.exportExcel(response, list, "政策分类数据");
    }

    /**
     * 获取政策分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('policy:category:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(policyCategoryService.selectPolicyCategoryById(id));
    }

    /**
     * 新增政策分类
     */
    @PreAuthorize("@ss.hasPermi('policy:category:add')")
    @Log(title = "政策分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PolicyCategory policyCategory)
    {
        if (!policyCategoryService.checkCategoryNameUnique(policyCategory))
        {
            return error("新增政策分类'" + policyCategory.getCategoryName() + "'失败，分类名称已存在");
        }
        return toAjax(policyCategoryService.insertPolicyCategory(policyCategory));
    }

    /**
     * 修改政策分类
     */
    @PreAuthorize("@ss.hasPermi('policy:category:edit')")
    @Log(title = "政策分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PolicyCategory policyCategory)
    {
        if (!policyCategoryService.checkCategoryNameUnique(policyCategory))
        {
            return error("修改政策分类'" + policyCategory.getCategoryName() + "'失败，分类名称已存在");
        }
        return toAjax(policyCategoryService.updatePolicyCategory(policyCategory));
    }

    /**
     * 删除政策分类
     */
    @PreAuthorize("@ss.hasPermi('policy:category:remove')")
    @Log(title = "政策分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(policyCategoryService.deletePolicyCategoryByIds(ids));
    }
}