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
import com.jonlink.system.domain.PolicyArticle;
import com.jonlink.system.domain.PolicyArticleVersion;
import com.jonlink.system.service.IPolicyArticleService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 政策文章Controller
 *
 * @author jonlink
 */
@RestController
@RequestMapping("/policy/article")
public class PolicyArticleController extends BaseController
{
    @Autowired
    private IPolicyArticleService policyArticleService;

    /**
     * 查询政策文章列表
     */
    @PreAuthorize("@ss.hasPermi('policy:article:list')")
    @GetMapping("/list")
    public TableDataInfo list(PolicyArticle policyArticle)
    {
        startPage();
        List<PolicyArticle> list = policyArticleService.selectPolicyArticleList(policyArticle);
        return getDataTable(list);
    }

    /**
     * 导出政策文章列表
     */
    @PreAuthorize("@ss.hasPermi('policy:article:export')")
    @Log(title = "政策文章", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PolicyArticle policyArticle)
    {
        List<PolicyArticle> list = policyArticleService.selectPolicyArticleList(policyArticle);
        ExcelUtil<PolicyArticle> util = new ExcelUtil<PolicyArticle>(PolicyArticle.class);
        util.exportExcel(response, list, "政策文章数据");
    }

    /**
     * 获取政策文章详细信息
     */
    @PreAuthorize("@ss.hasPermi('policy:article:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(policyArticleService.selectPolicyArticleById(id));
    }

    /**
     * 查询分类下的当前政策
     */
    @PreAuthorize("@ss.hasPermi('policy:article:query')")
    @GetMapping(value = "/current/{categoryId}")
    public AjaxResult getCurrentByCategoryId(@PathVariable("categoryId") Long categoryId)
    {
        return success(policyArticleService.selectCurrentByCategoryId(categoryId));
    }

    /**
     * 新增政策文章
     */
    @PreAuthorize("@ss.hasPermi('policy:article:add')")
    @Log(title = "政策文章", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PolicyArticle policyArticle)
    {
        return toAjax(policyArticleService.insertPolicyArticle(policyArticle));
    }

    /**
     * 修改政策文章（自动保存旧版本）
     */
    @PreAuthorize("@ss.hasPermi('policy:article:edit')")
    @Log(title = "政策文章", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PolicyArticle policyArticle)
    {
        return toAjax(policyArticleService.updatePolicyArticle(policyArticle));
    }

    /**
     * 删除政策文章
     */
    @PreAuthorize("@ss.hasPermi('policy:article:remove')")
    @Log(title = "政策文章", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(policyArticleService.deletePolicyArticleByIds(ids));
    }

    /**
     * 查询历史版本列表
     */
    @PreAuthorize("@ss.hasPermi('policy:article:query')")
    @GetMapping(value = "/version/{articleId}")
    public AjaxResult versionList(@PathVariable("articleId") Long articleId)
    {
        List<PolicyArticleVersion> list = policyArticleService.selectVersionListByArticleId(articleId);
        return success(list);
    }

    /**
     * 获取历史版本详情
     */
    @PreAuthorize("@ss.hasPermi('policy:article:query')")
    @GetMapping(value = "/version/detail/{id}")
    public AjaxResult versionDetail(@PathVariable("id") Long id)
    {
        return success(policyArticleService.selectVersionById(id));
    }

    /**
     * 恢复历史版本
     */
    @PreAuthorize("@ss.hasPermi('policy:article:edit')")
    @Log(title = "政策文章", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/version/restore/{versionId}")
    public AjaxResult restoreVersion(@PathVariable("versionId") Long versionId)
    {
        return toAjax(policyArticleService.restoreVersion(versionId));
    }
}