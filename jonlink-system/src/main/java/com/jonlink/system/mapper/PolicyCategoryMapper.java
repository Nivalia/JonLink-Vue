package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.PolicyCategory;

/**
 * 政策分类 Mapper 接口
 *
 * @author jonlink
 * @date 2026-08-22
 */
public interface PolicyCategoryMapper
{
    /** 查询一条 */
    public PolicyCategory selectPolicyCategoryById(Long id);

    /** 条件查询 */
    public List<PolicyCategory> selectPolicyCategoryList(PolicyCategory category);

    /** 树查询(1 级+2 级全展开) */
    public List<PolicyCategory> selectPolicyCategoryTree(PolicyCategory category);

    /** 校验同级下分类名唯一 */
    public PolicyCategory checkCategoryNameUnique(PolicyCategory category);

    /** 新增 */
    public int insertPolicyCategory(PolicyCategory category);

    /** 修改 */
    public int updatePolicyCategory(PolicyCategory category);

    /** 单删 */
    public int deletePolicyCategoryById(Long id);

    /** 批删 */
    public int deletePolicyCategoryByIds(Long[] ids);
}
