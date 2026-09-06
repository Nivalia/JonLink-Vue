package com.jonlink.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.PolicyCategoryMapper;
import com.jonlink.system.domain.PolicyCategory;
import com.jonlink.system.service.IPolicyCategoryService;

/**
 * 政策分类Service业务层处理
 *
 * @author jonlink
 */
@Service
public class PolicyCategoryServiceImpl implements IPolicyCategoryService
{
    @Autowired
    private PolicyCategoryMapper policyCategoryMapper;

    /**
     * 查询政策分类
     */
    @Override
    public PolicyCategory selectPolicyCategoryById(Long id)
    {
        return policyCategoryMapper.selectPolicyCategoryById(id);
    }

    /**
     * 查询政策分类列表
     */
    @Override
    public List<PolicyCategory> selectPolicyCategoryList(PolicyCategory policyCategory)
    {
        return policyCategoryMapper.selectPolicyCategoryList(policyCategory);
    }

    /**
     * 查询政策分类树
     */
    @Override
    public List<PolicyCategory> selectPolicyCategoryTree(PolicyCategory policyCategory)
    {
        List<PolicyCategory> list = policyCategoryMapper.selectPolicyCategoryTree(policyCategory);
        return buildTree(list);
    }

    /**
     * 构建分类树
     */
    private List<PolicyCategory> buildTree(List<PolicyCategory> list)
    {
        List<PolicyCategory> roots = new ArrayList<>();
        for (PolicyCategory node : list)
        {
            if (node.getParentId() == null || node.getParentId() == 0L)
            {
                roots.add(node);
            }
        }
        for (PolicyCategory root : roots)
        {
            root.setChildren(getChildren(root.getId(), list));
        }
        return roots;
    }

    /**
     * 获取子节点
     */
    private List<PolicyCategory> getChildren(Long parentId, List<PolicyCategory> list)
    {
        List<PolicyCategory> children = new ArrayList<>();
        for (PolicyCategory node : list)
        {
            if (parentId.equals(node.getParentId()))
            {
                children.add(node);
            }
        }
        for (PolicyCategory child : children)
        {
            child.setChildren(getChildren(child.getId(), list));
        }
        return children;
    }

    /**
     * 新增政策分类
     */
    @Override
    public int insertPolicyCategory(PolicyCategory policyCategory)
    {
        policyCategory.setCreateTime(DateUtils.getNowDate());
        return policyCategoryMapper.insertPolicyCategory(policyCategory);
    }

    /**
     * 修改政策分类
     */
    @Override
    public int updatePolicyCategory(PolicyCategory policyCategory)
    {
        policyCategory.setUpdateTime(DateUtils.getNowDate());
        return policyCategoryMapper.updatePolicyCategory(policyCategory);
    }

    /**
     * 批量删除政策分类
     */
    @Override
    public int deletePolicyCategoryByIds(Long[] ids)
    {
        return policyCategoryMapper.deletePolicyCategoryByIds(ids);
    }

    /**
     * 删除政策分类信息
     */
    @Override
    public int deletePolicyCategoryById(Long id)
    {
        return policyCategoryMapper.deletePolicyCategoryById(id);
    }

    /**
     * 校验分类名称唯一
     */
    @Override
    public boolean checkCategoryNameUnique(PolicyCategory policyCategory)
    {
        PolicyCategory info = policyCategoryMapper.checkCategoryNameUnique(policyCategory);
        if (info != null && !info.getId().equals(policyCategory.getId()))
        {
            return false;
        }
        return true;
    }
}