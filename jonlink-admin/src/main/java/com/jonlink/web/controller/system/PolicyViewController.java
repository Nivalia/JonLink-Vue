package com.jonlink.web.controller.system;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.jonlink.common.core.controller.BaseController;
import com.jonlink.common.core.domain.AjaxResult;
import com.jonlink.system.domain.PolicyArticle;
import com.jonlink.system.domain.PolicyArticleVersion;
import com.jonlink.system.service.IPolicyArticleService;

/**
 * 政策展示端接口(员工访问)
 * 权限:policy:view:list(给业务岗 + admin)
 *
 * @author jonlink
 * @date 2026-08-22
 */
@RestController
@RequestMapping("/policy/view")
public class PolicyViewController extends BaseController
{
    @Autowired
    private IPolicyArticleService articleService;

    /** 按 2 级分类查当前版(展示端主页) */
    @PreAuthorize("@ss.hasPermi('policy:view:list')")
    @GetMapping("/current/{categoryId}")
    public AjaxResult current(@PathVariable Long categoryId) {
        PolicyArticle current = articleService.selectCurrentByCategoryId(categoryId);
        return success(current);
    }

    /** 历史版本列表 */
    @PreAuthorize("@ss.hasPermi('policy:view:list')")
    @GetMapping("/history/{articleId}")
    public AjaxResult history(@PathVariable Long articleId) {
        List<PolicyArticleVersion> list = articleService.selectVersionListByArticleId(articleId);
        return success(list);
    }

    /** 单条历史版本详情 */
    @PreAuthorize("@ss.hasPermi('policy:view:list')")
    @GetMapping("/historyDetail/{versionId}")
    public AjaxResult historyDetail(@PathVariable Long versionId) {
        PolicyArticleVersion v = articleService.selectVersionById(versionId);
        return success(v);
    }

    /**
     * 版本对比(展示端可调用,返回两条版本号用来并排展示)
     * GET /policy/view/compare?articleId={id}&base={v1}&target={v2}
     */
    @PreAuthorize("@ss.hasPermi('policy:view:list')")
    @GetMapping("/compare")
    public AjaxResult compare(@RequestParam Long articleId,
                              @RequestParam Integer base,
                              @RequestParam Integer target) {
        // 简化实现:取当前版 + 指定版本号(simple compare shape)
        PolicyArticle current = articleService.selectPolicyArticleById(articleId);
        List<PolicyArticleVersion> history = articleService.selectVersionListByArticleId(articleId);
        PolicyArticleVersion baseV = history.stream()
            .filter(v -> base.equals(v.getVersionNo())).findFirst().orElse(null);
        PolicyArticleVersion targetV = history.stream()
            .filter(v -> target.equals(v.getVersionNo())).findFirst().orElse(null);
        return success(java.util.Map.of(
            "current", current,
            "base", baseV,
            "target", targetV));
    }
}
