package com.jonlink.system.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.common.core.domain.entity.SysRole;
import com.jonlink.common.core.domain.entity.SysUser;
import com.jonlink.system.domain.SysRoleDept;
import com.jonlink.system.mapper.SysUserMapper;
import com.jonlink.system.mapper.SysRoleMapper;
import com.jonlink.system.mapper.SysRoleDeptMapper;
import com.jonlink.system.service.IFinDataScopeService;

/**
 * 数据权限Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-23
 */
@Service
public class FinDataScopeServiceImpl implements IFinDataScopeService
{
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleDeptMapper sysRoleDeptMapper;

    /**
     * 获取用户可访问的部门ID列表
     * 
     * @param username 用户名
     * @return 部门ID集合
     */
    @Override
    public Set<Long> getVisibleDeptIds(String username)
    {
        Set<Long> deptIds = new HashSet<>();
        
        // 获取用户信息
        SysUser user = sysUserMapper.selectUserByUserName(username);
        if (user == null) {
            return deptIds;
        }
        
        // 如果是管理员，返回所有部门
        if (user.isAdmin()) {
            deptIds.add(0L); // 0表示所有部门
            return deptIds;
        }
        
        // 获取用户角色
        List<SysRole> roles = sysRoleMapper.selectRolePermissionByUserId(user.getUserId());
        if (roles == null || roles.isEmpty()) {
            return deptIds;
        }
        
        // 遍历角色，收集部门权限
        for (SysRole role : roles) {
            // 如果角色是管理员或拥有全部数据权限
            if (role.isAdmin() || "1".equals(role.getDataScope())) {
                deptIds.clear();
                deptIds.add(0L); // 0表示所有部门
                return deptIds;
            }
            
            // 自定义数据权限：查询角色关联的部门
            if ("2".equals(role.getDataScope())) {
                List<SysRoleDept> roleDepts = sysRoleDeptMapper.selectRoleDeptByRoleId(role.getRoleId());
                if (roleDepts != null) {
                    for (SysRoleDept rd : roleDepts) {
                        deptIds.add(rd.getDeptId());
                    }
                }
            }
            // 本部门数据权限
            else if ("3".equals(role.getDataScope())) {
                deptIds.add(user.getDeptId());
            }
            // 本部门及以下数据权限
            else if ("4".equals(role.getDataScope())) {
                deptIds.add(user.getDeptId());
                // 这里可以递归查询子部门，简化处理直接返回本部门
            }
            // 仅本人数据权限
            else if ("5".equals(role.getDataScope())) {
                // 仅本人数据权限，返回空集合，后续通过用户ID过滤
                deptIds.add(-1L); // 特殊标记
            }
        }
        
        return deptIds;
    }

    /**
     * 检查用户是否有权限访问指定数据
     * 
     * @param username 用户名
     * @param dataDeptId 数据所属部门ID
     * @return 是否有权限
     */
    @Override
    public boolean hasAccess(String username, Long dataDeptId)
    {
        if (dataDeptId == null) {
            return true;
        }
        
        Set<Long> visibleDeptIds = getVisibleDeptIds(username);
        
        // 如果包含0，表示有全部权限
        if (visibleDeptIds.contains(0L)) {
            return true;
        }
        
        // 如果包含-1，表示仅本人数据权限，需要额外判断
        if (visibleDeptIds.contains(-1L)) {
            return false; // 调用方需要额外通过用户ID过滤
        }
        
        return visibleDeptIds.contains(dataDeptId);
    }
}
