package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.JonlinkContactPersonMapper;
import com.jonlink.system.domain.JonlinkContactPerson;
import com.jonlink.system.service.IJonlinkContactPersonService;

/**
 * 联系人管理Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@Service
public class JonlinkContactPersonServiceImpl implements IJonlinkContactPersonService 
{
    @Autowired
    private JonlinkContactPersonMapper jonlinkContactPersonMapper;

    /**
     * 查询联系人管理
     * 
     * @param id 联系人管理主键
     * @return 联系人管理
     */
    @Override
    public JonlinkContactPerson selectJonlinkContactPersonById(Long id)
    {
        return jonlinkContactPersonMapper.selectJonlinkContactPersonById(id);
    }

    /**
     * 查询联系人管理列表
     * 
     * @param jonlinkContactPerson 联系人管理
     * @return 联系人管理
     */
    @Override
    public List<JonlinkContactPerson> selectJonlinkContactPersonList(JonlinkContactPerson jonlinkContactPerson)
    {
        return jonlinkContactPersonMapper.selectJonlinkContactPersonList(jonlinkContactPerson);
    }

    /**
     * 新增联系人管理
     * 
     * @param jonlinkContactPerson 联系人管理
     * @return 结果
     */
    @Override
    public int insertJonlinkContactPerson(JonlinkContactPerson jonlinkContactPerson)
    {
        jonlinkContactPerson.setCreateTime(DateUtils.getNowDate());
        return jonlinkContactPersonMapper.insertJonlinkContactPerson(jonlinkContactPerson);
    }

    /**
     * 修改联系人管理
     * 
     * @param jonlinkContactPerson 联系人管理
     * @return 结果
     */
    @Override
    public int updateJonlinkContactPerson(JonlinkContactPerson jonlinkContactPerson)
    {
        jonlinkContactPerson.setUpdateTime(DateUtils.getNowDate());
        return jonlinkContactPersonMapper.updateJonlinkContactPerson(jonlinkContactPerson);
    }

    /**
     * 批量删除联系人管理
     * 
     * @param ids 需要删除的联系人管理主键
     * @return 结果
     */
    @Override
    public int deleteJonlinkContactPersonByIds(Long[] ids)
    {
        return jonlinkContactPersonMapper.deleteJonlinkContactPersonByIds(ids);
    }

    /**
     * 删除联系人管理信息
     * 
     * @param id 联系人管理主键
     * @return 结果
     */
    @Override
    public int deleteJonlinkContactPersonById(Long id)
    {
        return jonlinkContactPersonMapper.deleteJonlinkContactPersonById(id);
    }
}
