package com.jonlink.system.service;

import java.util.List;
import com.jonlink.system.domain.PolicyCategory;

/**
 * 政策分类Service接口
 *
 * @author jonlink
 */
public interface IPolicyCategoryService
{
    /**
     * 查询政策分类
     */
    public PolicyCategory selectPolicyCategoryById(Long id);

    /**
     * 查询政策分类列表
     */
    public List<PolicyCategory> selectPolicyCategoryList(PolicyCategory policyCategory);

    /**
     * 查询政策分类树
     */
    public List<PolicyCategory> selectPolicyCategoryTree(PolicyCategory policyCategory);

    /**
     * 新增政策分类
     */
    public int insertPolicyCategory(PolicyCategory policyCategory);

    /**
     * 修改政策分类
     */
    public int updatePolicyCategory(PolicyCategory policyCategory);

    /**
     * 批量删除政策分类
     */
    public int deletePolicyCategoryByIds(Long[] ids);

    /**
     * 删除政策分类信息
     */
    public int deletePolicyCategoryById(Long id);

    /**
     * 校验分类名称唯一
     */
    public boolean checkCategoryNameUnique(PolicyCategory policyCategory);
}