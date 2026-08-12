package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.JonlinkInsuranceCompany;

/**
 * 保险公司Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public interface JonlinkInsuranceCompanyMapper 
{
    /**
     * 查询保险公司
     * 
     * @param id 保险公司主键
     * @return 保险公司
     */
    public JonlinkInsuranceCompany selectJonlinkInsuranceCompanyById(Long id);

    /**
     * 查询保险公司列表
     * 
     * @param jonlinkInsuranceCompany 保险公司
     * @return 保险公司集合
     */
    public List<JonlinkInsuranceCompany> selectJonlinkInsuranceCompanyList(JonlinkInsuranceCompany jonlinkInsuranceCompany);

    /**
     * 新增保险公司
     * 
     * @param jonlinkInsuranceCompany 保险公司
     * @return 结果
     */
    public int insertJonlinkInsuranceCompany(JonlinkInsuranceCompany jonlinkInsuranceCompany);

    /**
     * 修改保险公司
     * 
     * @param jonlinkInsuranceCompany 保险公司
     * @return 结果
     */
    public int updateJonlinkInsuranceCompany(JonlinkInsuranceCompany jonlinkInsuranceCompany);

    /**
     * 删除保险公司
     * 
     * @param id 保险公司主键
     * @return 结果
     */
    public int deleteJonlinkInsuranceCompanyById(Long id);

    /**
     * 批量删除保险公司
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteJonlinkInsuranceCompanyByIds(Long[] ids);
}
