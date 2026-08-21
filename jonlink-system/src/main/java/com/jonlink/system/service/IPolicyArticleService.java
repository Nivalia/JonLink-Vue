package com.jonlink.system.service;

import java.util.List;
import com.jonlink.system.domain.PolicyArticle;
import com.jonlink.system.domain.PolicyArticleVersion;

/**
 * 政策文章 Service 接口
 *
 * @author jonlink
 * @date 2026-08-22
 */
public interface IPolicyArticleService
{
    /** 条件分页 */
    public List<PolicyArticle> selectPolicyArticleList(PolicyArticle article);

    /** 单条 */
    public PolicyArticle selectPolicyArticleById(Long id);

    /** 按分类查当前版(展示端) */
    public PolicyArticle selectCurrentByCategoryId(Long categoryId);

    /** 浏览 +1 */
    public void incrementViewCount(Long id);

    /**
     * 新增政策
     * 自动设置 version_no=1
     */
    public int insertPolicyArticle(PolicyArticle article);

    /**
     * 修改政策
     * 如果 pics 或 title 变化,自动将"修改前的当前版"快照到 policy_article_version
     */
    public int updatePolicyArticle(PolicyArticle article);

    /** 单删 */
    public int deletePolicyArticleById(Long id);

    /** 批删 */
    public int deletePolicyArticleByIds(Long[] ids);

    /** 历史版本(倒序) */
    public List<PolicyArticleVersion> selectVersionListByArticleId(Long articleId);

    /** 取一条历史版本 */
    public PolicyArticleVersion selectVersionById(Long id);

    /**
     * 恢复到指定历史版本
     * 实现:把当前版快照,再用历史版本字段覆盖当前版,version_no 自增
     */
    public int restoreVersion(Long versionId);
}
