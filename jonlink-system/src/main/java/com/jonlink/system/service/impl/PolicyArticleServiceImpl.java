package com.jonlink.system.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jonlink.system.mapper.PolicyArticleMapper;
import com.jonlink.system.domain.PolicyArticle;
import com.jonlink.system.domain.PolicyArticleVersion;
import com.jonlink.system.service.IPolicyArticleService;

/**
 * 政策文章Service业务层处理
 *
 * @author jonlink
 */
@Service
public class PolicyArticleServiceImpl implements IPolicyArticleService
{
    @Autowired
    private PolicyArticleMapper policyArticleMapper;

    /**
     * 查询政策文章
     */
    @Override
    public PolicyArticle selectPolicyArticleById(Long id)
    {
        return policyArticleMapper.selectPolicyArticleById(id);
    }

    /**
     * 查询政策文章列表
     */
    @Override
    public List<PolicyArticle> selectPolicyArticleList(PolicyArticle policyArticle)
    {
        return policyArticleMapper.selectPolicyArticleList(policyArticle);
    }

    /**
     * 查询分类下的当前政策
     */
    @Override
    public PolicyArticle selectCurrentByCategoryId(Long categoryId)
    {
        return policyArticleMapper.selectCurrentByCategoryId(categoryId);
    }

    /**
     * 新增政策文章
     */
    @Override
    public int insertPolicyArticle(PolicyArticle policyArticle)
    {
        Date now = DateUtils.getNowDate();
        policyArticle.setCreateTime(now);
        policyArticle.setVersionNo(1);
        policyArticle.setViewCount(0);
        // 标题为空时自动生成:yyyyMMddHHmmss (运营/客服根本不用想标题,直接上传图即可)
        if (policyArticle.getTitle() == null || policyArticle.getTitle().trim().isEmpty())
        {
            String autoTitle = new SimpleDateFormat("yyyyMMddHHmmss").format(now);
            policyArticle.setTitle(autoTitle);
        }
        int rows = policyArticleMapper.insertPolicyArticle(policyArticle);
        // 新增同时保存第一版本
        saveVersion(policyArticle);
        return rows;
    }

    /**
     * 修改政策文章（自动保存旧版本到历史）
     */
    @Override
    @Transactional
    public int updatePolicyArticle(PolicyArticle policyArticle)
    {
        // 获取当前文章
        PolicyArticle old = policyArticleMapper.selectPolicyArticleById(policyArticle.getId());
        if (old == null)
        {
            return 0;
        }
        // 保存旧版本到历史表
        PolicyArticleVersion version = new PolicyArticleVersion();
        version.setArticleId(old.getId());
        version.setVersionNo(old.getVersionNo());
        version.setTitle(old.getTitle());
        version.setPics(old.getPics());
        version.setCreateBy(old.getUpdateBy() != null ? old.getUpdateBy() : old.getCreateBy());
        version.setCreateTime(new Date());
        policyArticleMapper.insertPolicyArticleVersion(version);

        // 更新版本号+1
        policyArticle.setVersionNo(old.getVersionNo() + 1);
        policyArticle.setUpdateTime(DateUtils.getNowDate());
        return policyArticleMapper.updatePolicyArticle(policyArticle);
    }

    /**
     * 批量删除政策文章
     */
    @Override
    public int deletePolicyArticleByIds(Long[] ids)
    {
        return policyArticleMapper.deletePolicyArticleByIds(ids);
    }

    /**
     * 删除政策文章信息
     */
    @Override
    public int deletePolicyArticleById(Long id)
    {
        return policyArticleMapper.deletePolicyArticleById(id);
    }

    /**
     * 查询历史版本列表
     */
    @Override
    public List<PolicyArticleVersion> selectVersionListByArticleId(Long articleId)
    {
        return policyArticleMapper.selectVersionListByArticleId(articleId);
    }

    /**
     * 查询历史版本详情
     */
    @Override
    public PolicyArticleVersion selectVersionById(Long id)
    {
        return policyArticleMapper.selectVersionById(id);
    }

    /**
     * 恢复历史版本
     */
    @Override
    @Transactional
    public int restoreVersion(Long versionId)
    {
        PolicyArticleVersion version = policyArticleMapper.selectVersionById(versionId);
        if (version == null)
        {
            return 0;
        }
        PolicyArticle article = policyArticleMapper.selectPolicyArticleById(version.getArticleId());
        if (article == null)
        {
            return 0;
        }
        // 保存当前版本到历史
        PolicyArticleVersion oldVersion = new PolicyArticleVersion();
        oldVersion.setArticleId(article.getId());
        oldVersion.setVersionNo(article.getVersionNo());
        oldVersion.setTitle(article.getTitle());
        oldVersion.setPics(article.getPics());
        oldVersion.setCreateBy(DateUtils.getNowDate() != null ? "system" : "system");
        oldVersion.setCreateTime(new Date());
        policyArticleMapper.insertPolicyArticleVersion(oldVersion);

        // 用历史版本内容覆盖当前
        article.setTitle(version.getTitle());
        article.setPics(version.getPics());
        article.setVersionNo(article.getVersionNo() + 1);
        article.setUpdateTime(DateUtils.getNowDate());
        return policyArticleMapper.updatePolicyArticle(article);
    }

    /**
     * 保存初始版本
     */
    private void saveVersion(PolicyArticle article)
    {
        PolicyArticleVersion version = new PolicyArticleVersion();
        version.setArticleId(article.getId());
        version.setVersionNo(1);
        version.setTitle(article.getTitle());
        version.setPics(article.getPics());
        version.setCreateBy(article.getCreateBy());
        version.setCreateTime(new Date());
        policyArticleMapper.insertPolicyArticleVersion(version);
    }
}