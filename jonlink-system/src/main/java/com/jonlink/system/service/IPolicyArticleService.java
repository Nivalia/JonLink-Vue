package com.jonlink.system.service;

import java.util.List;
import com.jonlink.system.domain.PolicyArticle;
import com.jonlink.system.domain.PolicyArticleVersion;

/**
 * 政策文章Service接口
 *
 * @author jonlink
 */
public interface IPolicyArticleService
{
    /**
     * 查询政策文章
     */
    public PolicyArticle selectPolicyArticleById(Long id);

    /**
     * 查询政策文章列表
     */
    public List<PolicyArticle> selectPolicyArticleList(PolicyArticle policyArticle);

    /**
     * 查询分类下的当前政策
     */
    public PolicyArticle selectCurrentByCategoryId(Long categoryId);

    /**
     * 新增政策文章
     */
    public int insertPolicyArticle(PolicyArticle policyArticle);

    /**
     * 修改政策文章（自动保存旧版本到历史）
     */
    public int updatePolicyArticle(PolicyArticle policyArticle);

    /**
     * 批量删除政策文章
     */
    public int deletePolicyArticleByIds(Long[] ids);

    /**
     * 删除政策文章信息
     */
    public int deletePolicyArticleById(Long id);

    /**
     * 查询历史版本列表
     */
    public List<PolicyArticleVersion> selectVersionListByArticleId(Long articleId);

    /**
     * 查询历史版本详情
     */
    public PolicyArticleVersion selectVersionById(Long id);

    /**
     * 恢复历史版本（用历史版本覆盖当前）
     */
    public int restoreVersion(Long versionId);
}