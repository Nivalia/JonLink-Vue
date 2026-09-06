package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.FinAuxiliary;

/**
 * 辅助核算Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-23
 */
public interface FinAuxiliaryMapper 
{
    /**
     * 查询辅助核算
     * 
     * @param id 辅助核算主键
     * @return 辅助核算
     */
    public FinAuxiliary selectFinAuxiliaryById(Long id);

    /**
     * 查询辅助核算列表
     * 
     * @param finAuxiliary 辅助核算
     * @return 辅助核算集合
     */
    public List<FinAuxiliary> selectFinAuxiliaryList(FinAuxiliary finAuxiliary);

    /**
     * 新增辅助核算
     * 
     * @param finAuxiliary 辅助核算
     * @return 结果
     */
    public int insertFinAuxiliary(FinAuxiliary finAuxiliary);

    /**
     * 修改辅助核算
     * 
     * @param finAuxiliary 辅助核算
     * @return 结果
     */
    public int updateFinAuxiliary(FinAuxiliary finAuxiliary);

    /**
     * 删除辅助核算
     * 
     * @param id 辅助核算主键
     * @return 结果
     */
    public int deleteFinAuxiliaryById(Long id);

    /**
     * 批量删除辅助核算
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinAuxiliaryByIds(Long[] ids);

    /**
     * 按类型查询辅助核算
     * 
     * @param auxType 辅助核算类型
     * @return 辅助核算集合
     */
    public List<FinAuxiliary> selectFinAuxiliaryByType(String auxType);
}
