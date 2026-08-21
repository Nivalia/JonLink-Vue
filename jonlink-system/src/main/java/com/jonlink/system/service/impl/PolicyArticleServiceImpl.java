package com.jonlink.system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jonlink.common.exception.ServiceException;
import com.jonlink.common.utils.DateUtils;
import com.jonlink.system.domain.PolicyArticle;
import com.jonlink.system.domain.PolicyArticleVersion;
import com.jonlink.system.mapper.PolicyArticleMapper;
import com.jonlink.system.service.IPolicyArticleService;

/**
 * 政策文章 Service 业务层实现
 *
 * @author jonlink
 * @date 2026-08-22
 */
@Service
public class PolicyArticleServiceImpl implements IPolicyArticleService
{
    @Autowired
    private PolicyArticleMapper articleMapper;

    @Override
    public List<PolicyArticle> selectPolicyArticleList(PolicyArticle article) {
        return articleMapper.selectPolicyArticleList(article);
    }

    @Override
    public PolicyArticle selectPolicyArticleById(Long id) {
        return articleMapper.selectPolicyArticleById(id);
    }

    @Override
    public PolicyArticle selectCurrentByCategoryId(Long categoryId) {
        PolicyArticle current = articleMapper.selectCurrentByCategoryId(categoryId);
        if (current != null) incrementViewCount(current.getId());
        return current;
    }

    @Override
    public void incrementViewCount(Long id) {
        articleMapper.incrementViewCount(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertPolicyArticle(PolicyArticle article) {
        if (article.getStatus() == null || article.getStatus().isEmpty()) article.setStatus("1");
        article.setVersionNo(1);
        article.setViewCount(0);
        article.setCreateTime(DateUtils.getNowDate());
        int n = articleMapper.insertPolicyArticle(article);
        // 同时写一条版本快照(version_no=1)
        PolicyArticleVersion v = new PolicyArticleVersion();
        v.setArticleId(article.getId());
        v.setVersionNo(1);
        v.setTitle(article.getTitle());
        v.setPics(article.getPics());
        v.setCreateBy(article.getCreateBy());
        v.setCreateTime(article.getCreateTime());
        articleMapper.insertPolicyArticleVersion(v);
        return n;
    }

    /**
     * 修改:
     * - 如果 title 或 pics 变化,先把当前版快照到 version 表,version_no = 当前 maxVersionNo+1
     * - 更新当前版,version_no 在当前 maxVersionNo+1(对应 version 表中的同号)
     * - 若 title/pics 没变,只更新 update_time,不写版本快照(避免空版本)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePolicyArticle(PolicyArticle article) {
        PolicyArticle before = articleMapper.selectPolicyArticleById(article.getId());
        if (before == null) throw new ServiceException("政策不存在 id=" + article.getId());

        boolean changed = false;
        if (article.getTitle() != null && !article.getTitle().equals(before.getTitle())) changed = true;
        if (article.getPics() != null && !article.getPics().equals(before.getPics())) changed = true;

        Date now = DateUtils.getNowDate();
        article.setUpdateTime(now);

        if (changed) {
            // 当前版入历史表
            PolicyArticleVersion v = new PolicyArticleVersion();
            v.setArticleId(before.getId());
            v.setVersionNo(before.getVersionNo());
            v.setTitle(before.getTitle());
            v.setPics(before.getPics());
            v.setCreateBy(article.getUpdateBy() != null ? article.getUpdateBy() : "restore");
            v.setCreateTime(now);
            articleMapper.insertPolicyArticleVersion(v);

            // 当前版 version_no +1
            Integer maxInHistory = articleMapper.selectMaxVersionNoByArticleId(before.getId());
            int next = (maxInHistory == null ? 0 : maxInHistory) + 1;
            article.setVersionNo(next);
        }
        return articleMapper.updatePolicyArticle(article);
    }

    @Override
    public int deletePolicyArticleById(Long id) {
        // 注意:删除当前版不自动删历史版本(供审计)
        return articleMapper.deletePolicyArticleById(id);
    }

    @Override
    public int deletePolicyArticleByIds(Long[] ids) {
        return articleMapper.deletePolicyArticleByIds(ids);
    }

    @Override
    public List<PolicyArticleVersion> selectVersionListByArticleId(Long articleId) {
        return articleMapper.selectVersionListByArticleId(articleId);
    }

    @Override
    public PolicyArticleVersion selectVersionById(Long id) {
        return articleMapper.selectVersionById(id);
    }

    /**
     * 恢复流程:
     * 1. 拿到目标历史版本(v)
     * 2. 把当前版快照进历史表(version_no = 当前 maxVersionNo+1)
     * 3. 用 v 的 title/pics 覆盖当前版,当前 version_no 同样 = maxVersionNo+1
     * 4. 旧的历史版本保留不动(不删除)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int restoreVersion(Long versionId) {
        PolicyArticleVersion v = articleMapper.selectVersionById(versionId);
        if (v == null) throw new ServiceException("历史版本不存在 id=" + versionId);

        PolicyArticle before = articleMapper.selectPolicyArticleById(v.getArticleId());
        if (before == null) throw new ServiceException("对应文章不存在 id=" + v.getArticleId());

        Integer maxInHistory = articleMapper.selectMaxVersionNoByArticleId(v.getArticleId());
        int next = (maxInHistory == null ? 0 : maxInHistory) + 1;
        Date now = DateUtils.getNowDate();

        // 当前版入历史
        PolicyArticleVersion snap = new PolicyArticleVersion();
        snap.setArticleId(before.getId());
        snap.setVersionNo(before.getVersionNo());
        snap.setTitle(before.getTitle());
        snap.setPics(before.getPics());
        snap.setCreateBy("restore-before");
        snap.setCreateTime(now);
        articleMapper.insertPolicyArticleVersion(snap);

        // 覆盖当前版
        PolicyArticle upd = new PolicyArticle();
        upd.setId(before.getId());
        upd.setTitle(v.getTitle());
        upd.setPics(v.getPics());
        upd.setVersionNo(next);
        upd.setUpdateTime(now);
        upd.setUpdateBy("restore");
        return articleMapper.updatePolicyArticle(upd);
    }
}
