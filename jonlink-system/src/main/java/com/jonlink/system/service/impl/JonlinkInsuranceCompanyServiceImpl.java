package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.JonlinkInsuranceCompanyMapper;
import com.jonlink.system.domain.JonlinkInsuranceCompany;
import com.jonlink.system.service.IJonlinkInsuranceCompanyService;

/**
 * 保险公司Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@Service
public class JonlinkInsuranceCompanyServiceImpl implements IJonlinkInsuranceCompanyService 
{
    @Autowired
    private JonlinkInsuranceCompanyMapper jonlinkInsuranceCompanyMapper;

    /**
     * 查询保险公司
     * 
     * @param id 保险公司主键
     * @return 保险公司
     */
    @Override
    public JonlinkInsuranceCompany selectJonlinkInsuranceCompanyById(Long id)
    {
        return jonlinkInsuranceCompanyMapper.selectJonlinkInsuranceCompanyById(id);
    }

    /**
     * 查询保险公司列表
     * 
     * @param jonlinkInsuranceCompany 保险公司
     * @return 保险公司
     */
    @Override
    public List<JonlinkInsuranceCompany> selectJonlinkInsuranceCompanyList(JonlinkInsuranceCompany jonlinkInsuranceCompany)
    {
        return jonlinkInsuranceCompanyMapper.selectJonlinkInsuranceCompanyList(jonlinkInsuranceCompany);
    }

    /**
     * 新增保险公司
     * 
     * @param jonlinkInsuranceCompany 保险公司
     * @return 结果
     */
    @Override
    public int insertJonlinkInsuranceCompany(JonlinkInsuranceCompany jonlinkInsuranceCompany)
    {
        jonlinkInsuranceCompany.setCreateTime(DateUtils.getNowDate());
        return jonlinkInsuranceCompanyMapper.insertJonlinkInsuranceCompany(jonlinkInsuranceCompany);
    }

    /**
     * 修改保险公司
     * 
     * @param jonlinkInsuranceCompany 保险公司
     * @return 结果
     */
    @Override
    public int updateJonlinkInsuranceCompany(JonlinkInsuranceCompany jonlinkInsuranceCompany)
    {
        jonlinkInsuranceCompany.setUpdateTime(DateUtils.getNowDate());
        return jonlinkInsuranceCompanyMapper.updateJonlinkInsuranceCompany(jonlinkInsuranceCompany);
    }

    /**
     * 批量删除保险公司
     * 
     * @param ids 需要删除的保险公司主键
     * @return 结果
     */
    @Override
    public int deleteJonlinkInsuranceCompanyByIds(Long[] ids)
    {
        return jonlinkInsuranceCompanyMapper.deleteJonlinkInsuranceCompanyByIds(ids);
    }

    /**
     * 删除保险公司信息
     * 
     * @param id 保险公司主键
     * @return 结果
     */
    @Override
    public int deleteJonlinkInsuranceCompanyById(Long id)
    {
        return jonlinkInsuranceCompanyMapper.deleteJonlinkInsuranceCompanyById(id);
    }
}
