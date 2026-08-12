package com.jonlink.system.service;

import java.util.List;
import com.jonlink.system.domain.JonlinkContactPerson;

/**
 * 联系人管理Service接口
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public interface IJonlinkContactPersonService 
{
    /**
     * 查询联系人管理
     * 
     * @param id 联系人管理主键
     * @return 联系人管理
     */
    public JonlinkContactPerson selectJonlinkContactPersonById(Long id);

    /**
     * 查询联系人管理列表
     * 
     * @param jonlinkContactPerson 联系人管理
     * @return 联系人管理集合
     */
    public List<JonlinkContactPerson> selectJonlinkContactPersonList(JonlinkContactPerson jonlinkContactPerson);

    /**
     * 新增联系人管理
     * 
     * @param jonlinkContactPerson 联系人管理
     * @return 结果
     */
    public int insertJonlinkContactPerson(JonlinkContactPerson jonlinkContactPerson);

    /**
     * 修改联系人管理
     * 
     * @param jonlinkContactPerson 联系人管理
     * @return 结果
     */
    public int updateJonlinkContactPerson(JonlinkContactPerson jonlinkContactPerson);

    /**
     * 批量删除联系人管理
     * 
     * @param ids 需要删除的联系人管理主键集合
     * @return 结果
     */
    public int deleteJonlinkContactPersonByIds(Long[] ids);

    /**
     * 删除联系人管理信息
     * 
     * @param id 联系人管理主键
     * @return 结果
     */
    public int deleteJonlinkContactPersonById(Long id);
}
