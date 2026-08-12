package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.JonlinkInsuranceType;

/**
 * 险种管理Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public interface JonlinkInsuranceTypeMapper 
{
    /**
     * 查询险种管理
     * 
     * @param id 险种管理主键
     * @return 险种管理
     */
    public JonlinkInsuranceType selectJonlinkInsuranceTypeById(Long id);

    /**
     * 查询险种管理列表
     * 
     * @param jonlinkInsuranceType 险种管理
     * @return 险种管理集合
     */
    public List<JonlinkInsuranceType> selectJonlinkInsuranceTypeList(JonlinkInsuranceType jonlinkInsuranceType);

    /**
     * 新增险种管理
     * 
     * @param jonlinkInsuranceType 险种管理
     * @return 结果
     */
    public int insertJonlinkInsuranceType(JonlinkInsuranceType jonlinkInsuranceType);

    /**
     * 修改险种管理
     * 
     * @param jonlinkInsuranceType 险种管理
     * @return 结果
     */
    public int updateJonlinkInsuranceType(JonlinkInsuranceType jonlinkInsuranceType);

    /**
     * 删除险种管理
     * 
     * @param id 险种管理主键
     * @return 结果
     */
    public int deleteJonlinkInsuranceTypeById(Long id);

    /**
     * 批量删除险种管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteJonlinkInsuranceTypeByIds(Long[] ids);
}
