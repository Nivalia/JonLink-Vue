package com.jonlink.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.common.exception.ServiceException;
import com.jonlink.common.utils.DateUtils;
import com.jonlink.system.domain.PolicyCategory;
import com.jonlink.system.mapper.PolicyCategoryMapper;
import com.jonlink.system.service.IPolicyCategoryService;

/**
 * 政策分类 Service 业务层实现
 *
 * @author jonlink
 * @date 2026-08-22
 */
@Service
public class PolicyCategoryServiceImpl implements IPolicyCategoryService
{
    @Autowired
    private PolicyCategoryMapper categoryMapper;

    @Override
    public List<PolicyCategory> selectPolicyCategoryList(PolicyCategory category) {
        return categoryMapper.selectPolicyCategoryList(category);
    }

    @Override
    public List<PolicyCategory> selectPolicyCategoryTree(PolicyCategory category) {
        List<PolicyCategory> all = categoryMapper.selectPolicyCategoryTree(category);
        return buildTree(all);
    }

    @Override
    public PolicyCategory selectPolicyCategoryById(Long id) {
        return categoryMapper.selectPolicyCategoryById(id);
    }

    @Override
    public boolean checkCategoryNameUnique(PolicyCategory category) {
        Long id = category.getId();
        PolicyCategory exist = categoryMapper.checkCategoryNameUnique(category);
        if (exist == null) return true;
        // 命中自身 id 视为唯一
        return id != null && id.equals(exist.getId());
    }

    @Override
    public int insertPolicyCategory(PolicyCategory category) {
        if (!checkCategoryNameUnique(category)) {
            throw new ServiceException("同级分类名称 '" + category.getCategoryName() + "' 已存在");
        }
        if (category.getSort() == null) category.setSort(0);
        if (category.getStatus() == null || category.getStatus().isEmpty()) category.setStatus("1");
        category.setCreateTime(DateUtils.getNowDate());
        return categoryMapper.insertPolicyCategory(category);
    }

    @Override
    public int updatePolicyCategory(PolicyCategory category) {
        if (!checkCategoryNameUnique(category)) {
            throw new ServiceException("同级分类名称 '" + category.getCategoryName() + "' 已存在");
        }
        category.setUpdateTime(DateUtils.getNowDate());
        return categoryMapper.updatePolicyCategory(category);
    }

    @Override
    public int deletePolicyCategoryById(Long id) {
        // 有子分类则不能删
        PolicyCategory probe = new PolicyCategory();
        probe.setParentId(id);
        List<PolicyCategory> children = categoryMapper.selectPolicyCategoryList(probe);
        if (!children.isEmpty()) {
            throw new ServiceException("存在子分类,不允许删除");
        }
        return categoryMapper.deletePolicyCategoryById(id);
    }

    @Override
    public int deletePolicyCategoryByIds(Long[] ids) {
        // 任一 id 有子分类都不允许删
        for (Long id : ids) {
            PolicyCategory probe = new PolicyCategory();
            probe.setParentId(id);
            List<PolicyCategory> children = categoryMapper.selectPolicyCategoryList(probe);
            if (!children.isEmpty()) {
                throw new ServiceException("分类 id=" + id + " 存在子分类,不允许删除");
            }
        }
        return categoryMapper.deletePolicyCategoryByIds(ids);
    }

    @Override
    public List<PolicyCategory> buildTree(List<PolicyCategory> all) {
        if (all == null || all.isEmpty()) return new ArrayList<>();
        // 索引一级
        List<PolicyCategory> roots = new ArrayList<>();
        for (PolicyCategory c : all) {
            if (c.getParentId() == null || c.getParentId() == 0L) {
                c.setChildren(new ArrayList<>());
                roots.add(c);
            }
        }
        // 二级挂一级
        for (PolicyCategory c : all) {
            Long pid = c.getParentId();
            if (pid == null || pid == 0L) continue;
            for (PolicyCategory r : roots) {
                if (pid.equals(r.getId())) {
                    if (r.getChildren() == null) r.setChildren(new ArrayList<>());
                    r.getChildren().add(c);
                    break;
                }
            }
        }
        return roots;
    }
}
