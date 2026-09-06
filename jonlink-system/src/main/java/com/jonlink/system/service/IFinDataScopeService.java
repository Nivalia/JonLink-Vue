package com.jonlink.system.service;

import java.util.Set;

/**
 * 数据权限Service接口
 * 
 * @author jonlink
 * @date 2026-08-23
 */
public interface IFinDataScopeService 
{
    /**
     * 获取用户可访问的部门ID列表
     * 
     * @param username 用户名
     * @return 部门ID集合
     */
    public Set<Long> getVisibleDeptIds(String username);

    /**
     * 检查用户是否有权限访问指定数据
     * 
     * @param username 用户名
     * @param dataDeptId 数据所属部门ID
     * @return 是否有权限
     */
    public boolean hasAccess(String username, Long dataDeptId);
}
