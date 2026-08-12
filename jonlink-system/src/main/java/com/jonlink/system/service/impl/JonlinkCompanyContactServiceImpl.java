package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.JonlinkCompanyContactMapper;
import com.jonlink.system.domain.JonlinkCompanyContact;
import com.jonlink.system.service.IJonlinkCompanyContactService;

/**
 * 公司联系人Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@Service
public class JonlinkCompanyContactServiceImpl implements IJonlinkCompanyContactService 
{
    @Autowired
    private JonlinkCompanyContactMapper jonlinkCompanyContactMapper;

    /**
     * 查询公司联系人
     * 
     * @param id 公司联系人主键
     * @return 公司联系人
     */
    @Override
    public JonlinkCompanyContact selectJonlinkCompanyContactById(Long id)
    {
        return jonlinkCompanyContactMapper.selectJonlinkCompanyContactById(id);
    }

    /**
     * 查询公司联系人列表
     * 
     * @param jonlinkCompanyContact 公司联系人
     * @return 公司联系人
     */
    @Override
    public List<JonlinkCompanyContact> selectJonlinkCompanyContactList(JonlinkCompanyContact jonlinkCompanyContact)
    {
        return jonlinkCompanyContactMapper.selectJonlinkCompanyContactList(jonlinkCompanyContact);
    }

    /**
     * 新增公司联系人
     * 
     * @param jonlinkCompanyContact 公司联系人
     * @return 结果
     */
    @Override
    public int insertJonlinkCompanyContact(JonlinkCompanyContact jonlinkCompanyContact)
    {
        jonlinkCompanyContact.setCreateTime(DateUtils.getNowDate());
        return jonlinkCompanyContactMapper.insertJonlinkCompanyContact(jonlinkCompanyContact);
    }

    /**
     * 修改公司联系人
     * 
     * @param jonlinkCompanyContact 公司联系人
     * @return 结果
     */
    @Override
    public int updateJonlinkCompanyContact(JonlinkCompanyContact jonlinkCompanyContact)
    {
        return jonlinkCompanyContactMapper.updateJonlinkCompanyContact(jonlinkCompanyContact);
    }

    /**
     * 批量删除公司联系人
     * 
     * @param ids 需要删除的公司联系人主键
     * @return 结果
     */
    @Override
    public int deleteJonlinkCompanyContactByIds(Long[] ids)
    {
        return jonlinkCompanyContactMapper.deleteJonlinkCompanyContactByIds(ids);
    }

    /**
     * 删除公司联系人信息
     * 
     * @param id 公司联系人主键
     * @return 结果
     */
    @Override
    public int deleteJonlinkCompanyContactById(Long id)
    {
        return jonlinkCompanyContactMapper.deleteJonlinkCompanyContactById(id);
    }
}
