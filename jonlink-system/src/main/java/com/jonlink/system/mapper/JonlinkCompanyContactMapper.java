package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.JonlinkCompanyContact;

/**
 * 公司联系人Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public interface JonlinkCompanyContactMapper 
{
    /**
     * 查询公司联系人
     * 
     * @param id 公司联系人主键
     * @return 公司联系人
     */
    public JonlinkCompanyContact selectJonlinkCompanyContactById(Long id);

    /**
     * 查询公司联系人列表
     * 
     * @param jonlinkCompanyContact 公司联系人
     * @return 公司联系人集合
     */
    public List<JonlinkCompanyContact> selectJonlinkCompanyContactList(JonlinkCompanyContact jonlinkCompanyContact);

    /**
     * 新增公司联系人
     * 
     * @param jonlinkCompanyContact 公司联系人
     * @return 结果
     */
    public int insertJonlinkCompanyContact(JonlinkCompanyContact jonlinkCompanyContact);

    /**
     * 修改公司联系人
     * 
     * @param jonlinkCompanyContact 公司联系人
     * @return 结果
     */
    public int updateJonlinkCompanyContact(JonlinkCompanyContact jonlinkCompanyContact);

    /**
     * 删除公司联系人
     * 
     * @param id 公司联系人主键
     * @return 结果
     */
    public int deleteJonlinkCompanyContactById(Long id);

    /**
     * 批量删除公司联系人
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteJonlinkCompanyContactByIds(Long[] ids);
}
