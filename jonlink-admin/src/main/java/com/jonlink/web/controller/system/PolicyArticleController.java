package com.jonlink.web.controller.system;

import java.util.List;
import java.util.Map;
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
import com.jonlink.system.domain.PolicyArticle;
import com.jonlink.system.service.IPolicyArticleService;

/**
 * 政策文章 信息操作处理(管理端 CRUD)
 *
 * @author jonlink
 * @date 2026-08-22
 */
@RestController
@RequestMapping("/policy/article")
public class PolicyArticleController extends BaseController
{
    @Autowired
    private IPolicyArticleService articleService;

    @PreAuthorize("@ss.hasPermi('policy:article:list')")
    @GetMapping("/list")
    public TableDataInfo list(PolicyArticle article) {
        startPage();
        List<PolicyArticle> list = articleService.selectPolicyArticleList(article);
        return getDataTable(list);
    }

    @Log(title = "政策文章", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('policy:article:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, PolicyArticle article) {
        List<PolicyArticle> list = articleService.selectPolicyArticleList(article);
        ExcelUtil<PolicyArticle> util = new ExcelUtil<>(PolicyArticle.class);
        util.exportExcel(response, list, "政策文章数据");
    }

    @PreAuthorize("@ss.hasPermi('policy:article:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(articleService.selectPolicyArticleById(id));
    }

    @PreAuthorize("@ss.hasPermi('policy:article:add')")
    @Log(title = "政策文章", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody PolicyArticle article) {
        article.setCreateBy(getUsername());
        return toAjax(articleService.insertPolicyArticle(article));
    }

    @PreAuthorize("@ss.hasPermi('policy:article:edit')")
    @Log(title = "政策文章", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody PolicyArticle article) {
        article.setUpdateBy(getUsername());
        return toAjax(articleService.updatePolicyArticle(article));
    }

    @PreAuthorize("@ss.hasPermi('policy:article:remove')")
    @Log(title = "政策文章", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(articleService.deletePolicyArticleByIds(ids));
    }

    /**
     * 发布/下架 状态切换
     * 前端可传 PolicyArticle{id, status:'0'/'1'}
     */
    @PreAuthorize("@ss.hasPermi('policy:article:edit')")
    @Log(title = "政策文章", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}/status")
    public AjaxResult changeStatus(@PathVariable Long id, @RequestBody PolicyArticle body) {
        PolicyArticle a = new PolicyArticle();
        a.setId(id);
        a.setStatus(body.getStatus());
        a.setUpdateBy(getUsername());
        return toAjax(articleService.updatePolicyArticle(a));
    }

    /**
     * 历史版本列表(沿用 article 域权限即可)
     */
    @PreAuthorize("@ss.hasPermi('policy:article:query')")
    @GetMapping("/{id}/versions")
    public AjaxResult versions(@PathVariable Long id) {
        return success(articleService.selectVersionListByArticleId(id));
    }

    /**
     * 恢复历史版本
     */
    @PreAuthorize("@ss.hasPermi('policy:article:edit')")
    @Log(title = "政策文章-恢复版本", businessType = BusinessType.UPDATE)
    @PutMapping("/version/{versionId}/restore")
    public AjaxResult restoreVersion(@PathVariable Long versionId) {
        return toAjax(articleService.restoreVersion(versionId));
    }
}
