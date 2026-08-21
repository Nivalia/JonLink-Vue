package com.jonlink.system.mapper;

import java.util.List;
import java.util.Map;
import com.jonlink.system.domain.PolicyArticle;
import com.jonlink.system.domain.PolicyArticleVersion;

/**
 * 政策文章 Mapper 接口
 *
 * @author jonlink
 * @date 2026-08-22
 */
public interface PolicyArticleMapper
{
    /** 查询一条(管理端) */
    public PolicyArticle selectPolicyArticleById(Long id);

    /** 按分类ID查当前版(展示端) */
    public PolicyArticle selectCurrentByCategoryId(Long categoryId);

    /** 条件分页 */
    public List<PolicyArticle> selectPolicyArticleList(PolicyArticle article);

    /** 浏览次数 +1 */
    public int incrementViewCount(Long id);

    /** 新增(自动 useGeneratedKeys,article_id 写回对象) */
    public int insertPolicyArticle(PolicyArticle article);

    /** 修改当前版 */
    public int updatePolicyArticle(PolicyArticle article);

    /** 删除当前版 */
    public int deletePolicyArticleById(Long id);

    /** 批删 */
    public int deletePolicyArticleByIds(Long[] ids);

    /** 按 article_id 列出历史版本(倒序) */
    public List<PolicyArticleVersion> selectVersionListByArticleId(Long articleId);

    /** 取一条历史版本 */
    public PolicyArticleVersion selectVersionById(Long id);

    /** 新增版本快照 */
    public int insertPolicyArticleVersion(PolicyArticleVersion version);

    /** 当前版的最多版本号(用于下次保存时 +1) */
    public Integer selectMaxVersionNoByArticleId(Long articleId);
}
