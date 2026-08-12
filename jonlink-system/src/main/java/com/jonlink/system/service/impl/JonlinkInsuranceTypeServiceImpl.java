package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.JonlinkInsuranceTypeMapper;
import com.jonlink.system.domain.JonlinkInsuranceType;
import com.jonlink.system.service.IJonlinkInsuranceTypeService;

/**
 * 险种管理Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@Service
public class JonlinkInsuranceTypeServiceImpl implements IJonlinkInsuranceTypeService 
{
    @Autowired
    private JonlinkInsuranceTypeMapper jonlinkInsuranceTypeMapper;

    /**
     * 查询险种管理
     * 
     * @param id 险种管理主键
     * @return 险种管理
     */
    @Override
    public JonlinkInsuranceType selectJonlinkInsuranceTypeById(Long id)
    {
        return jonlinkInsuranceTypeMapper.selectJonlinkInsuranceTypeById(id);
    }

    /**
     * 查询险种管理列表
     * 
     * @param jonlinkInsuranceType 险种管理
     * @return 险种管理
     */
    @Override
    public List<JonlinkInsuranceType> selectJonlinkInsuranceTypeList(JonlinkInsuranceType jonlinkInsuranceType)
    {
        return jonlinkInsuranceTypeMapper.selectJonlinkInsuranceTypeList(jonlinkInsuranceType);
    }

    /**
     * 新增险种管理
     * 
     * @param jonlinkInsuranceType 险种管理
     * @return 结果
     */
    @Override
    public int insertJonlinkInsuranceType(JonlinkInsuranceType jonlinkInsuranceType)
    {
        jonlinkInsuranceType.setCreateTime(DateUtils.getNowDate());
        return jonlinkInsuranceTypeMapper.insertJonlinkInsuranceType(jonlinkInsuranceType);
    }

    /**
     * 修改险种管理
     * 
     * @param jonlinkInsuranceType 险种管理
     * @return 结果
     */
    @Override
    public int updateJonlinkInsuranceType(JonlinkInsuranceType jonlinkInsuranceType)
    {
        jonlinkInsuranceType.setUpdateTime(DateUtils.getNowDate());
        return jonlinkInsuranceTypeMapper.updateJonlinkInsuranceType(jonlinkInsuranceType);
    }

    /**
     * 批量删除险种管理
     * 
     * @param ids 需要删除的险种管理主键
     * @return 结果
     */
    @Override
    public int deleteJonlinkInsuranceTypeByIds(Long[] ids)
    {
        return jonlinkInsuranceTypeMapper.deleteJonlinkInsuranceTypeByIds(ids);
    }

    /**
     * 删除险种管理信息
     * 
     * @param id 险种管理主键
     * @return 结果
     */
    @Override
    public int deleteJonlinkInsuranceTypeById(Long id)
    {
        return jonlinkInsuranceTypeMapper.deleteJonlinkInsuranceTypeById(id);
    }
}
