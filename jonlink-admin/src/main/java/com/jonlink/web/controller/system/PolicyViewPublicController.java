package com.jonlink.web.controller.system;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.jonlink.common.annotation.Anonymous;
import com.jonlink.common.core.controller.BaseController;
import com.jonlink.common.core.domain.AjaxResult;
import com.jonlink.common.core.domain.entity.SysUser;
import com.jonlink.system.domain.PolicyArticle;
import com.jonlink.system.domain.PolicyArticleVersion;
import com.jonlink.system.service.IPolicyArticleService;
import com.jonlink.system.service.IPolicyCategoryService;
import com.jonlink.system.mapper.SysUserMapper;

/**
 * 政策展示端 - public 接口(渠道部门登录 + 政策浏览)
 *
 * 路径前缀: /policy/view/public
 *
 * @author jonlink
 * @date 2026-08-22
 */
@Anonymous
@RestController
@RequestMapping("/policy/view/public")
public class PolicyViewPublicController extends BaseController
{
    @Autowired
    private IPolicyCategoryService categoryService;

    @Autowired
    private IPolicyArticleService articleService;

    @Autowired
    private SysUserMapper sysUserMapper;

    private static final Long CHANNEL_DEPT_ID = 200L;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 渠道用户登录
     * 仅允许 dept_id = 200 (渠道部门) 的用户登录
     */
    @Anonymous
    @PostMapping("/login")
    public AjaxResult login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return error("用户名和密码不能为空");
        }
        // 查询用户
        SysUser user = sysUserMapper.selectUserByUserName(username.trim());
        if (user == null) {
            return error("用户名或密码错误");
        }
        // 检查用户状态
        if ("1".equals(user.getStatus())) {
            return error("该用户已被停用");
        }
        if ("1".equals(user.getDelFlag())) {
            return error("该用户已被删除");
        }
        // 检查是否渠道部门
        if (user.getDeptId() == null || !CHANNEL_DEPT_ID.equals(user.getDeptId())) {
            return error("仅限渠道部门用户登录");
        }
        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return error("用户名或密码错误");
        }
        // 返回用户信息
        Map<String, Object> data = new HashMap<>();
        data.put("userId", user.getUserId());
        data.put("username", user.getUserName());
        data.put("nickName", user.getNickName());
        data.put("deptId", user.getDeptId());
        return success(data);
    }

    /** 2 级分类树(只返启用的) */
    @Anonymous
    @GetMapping("/tree")
    public AjaxResult tree() {
        // IPolicyCategoryService.selectPolicyCategoryTree 已支持 category 入参
        com.jonlink.system.domain.PolicyCategory probe = new com.jonlink.system.domain.PolicyCategory();
        probe.setStatus(null); // 不过滤status，返回所有分类
        List<?> tree = categoryService.selectPolicyCategoryTree(probe);
        return success(tree);
    }

    /** 按 2 级分类查当前版 */
    @Anonymous
    @GetMapping("/current/{categoryId}")
    public AjaxResult current(@PathVariable Long categoryId) {
        PolicyArticle a = articleService.selectCurrentByCategoryId(categoryId);
        return success(a);
    }

    /** 历史版本列表(按 articleId) */
    @Anonymous
    @GetMapping("/history/{articleId}")
    public AjaxResult history(@PathVariable Long articleId) {
        List<PolicyArticleVersion> list = articleService.selectVersionListByArticleId(articleId);
        return success(list);
    }

    /** 单条历史版本详情 */
    @Anonymous
    @GetMapping("/historyDetail/{versionId}")
    public AjaxResult historyDetail(@PathVariable Long versionId) {
        PolicyArticleVersion v = articleService.selectVersionById(versionId);
        return success(v);
    }

    /** 版本对比 (并排展示用) */
    @Anonymous
    @GetMapping("/compare")
    public AjaxResult compare(@RequestParam Long articleId,
                              @RequestParam Integer base,
                              @RequestParam Integer target) {
        PolicyArticle current = articleService.selectPolicyArticleById(articleId);
        List<PolicyArticleVersion> history = articleService.selectVersionListByArticleId(articleId);
        PolicyArticleVersion baseV = history.stream()
            .filter(v -> base.equals(v.getVersionNo())).findFirst().orElse(null);
        PolicyArticleVersion targetV = history.stream()
            .filter(v -> target.equals(v.getVersionNo())).findFirst().orElse(null);
        return success(java.util.Map.of(
            "current", current == null ? "" : current,
            "base",    baseV == null ? "" : baseV,
            "target",  targetV == null ? "" : targetV));
    }
}
