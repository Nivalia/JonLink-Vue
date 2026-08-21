package com.jonlink.system.service;

import java.util.List;
import com.jonlink.system.domain.PolicyCategory;

/**
 * 政策分类 Service 接口
 *
 * @author jonlink
 * @date 2026-08-22
 */
public interface IPolicyCategoryService
{
    /** 条件分页 */
    public List<PolicyCategory> selectPolicyCategoryList(PolicyCategory category);

    /** 树列表(展示端用) */
    public List<PolicyCategory> selectPolicyCategoryTree(PolicyCategory category);

    /** 单条 */
    public PolicyCategory selectPolicyCategoryById(Long id);

    /** 同级下名称唯一 */
    public boolean checkCategoryNameUnique(PolicyCategory category);

    /** 新增 */
    public int insertPolicyCategory(PolicyCategory category);

    /** 修改 */
    public int updatePolicyCategory(PolicyCategory category);

    /** 单删 */
    public int deletePolicyCategoryById(Long id);

    /** 批删 */
    public int deletePolicyCategoryByIds(Long[] ids);

    /** 构建前端树(无 ParentId=0 包裹) */
    public List<PolicyCategory> buildTree(List<PolicyCategory> all);
}
