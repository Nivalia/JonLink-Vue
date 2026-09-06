package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.FinAuxiliaryMapper;
import com.jonlink.system.domain.FinAuxiliary;
import com.jonlink.system.service.IFinAuxiliaryService;

/**
 * 辅助核算Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-23
 */
@Service
public class FinAuxiliaryServiceImpl implements IFinAuxiliaryService
{
    @Autowired
    private FinAuxiliaryMapper finAuxiliaryMapper;

    /**
     * 查询辅助核算
     * 
     * @param id 辅助核算主键
     * @return 辅助核算
     */
    @Override
    public FinAuxiliary selectFinAuxiliaryById(Long id)
    {
        return finAuxiliaryMapper.selectFinAuxiliaryById(id);
    }

    /**
     * 查询辅助核算列表
     * 
     * @param finAuxiliary 辅助核算
     * @return 辅助核算
     */
    @Override
    public List<FinAuxiliary> selectFinAuxiliaryList(FinAuxiliary finAuxiliary)
    {
        return finAuxiliaryMapper.selectFinAuxiliaryList(finAuxiliary);
    }

    /**
     * 新增辅助核算
     * 
     * @param finAuxiliary 辅助核算
     * @return 结果
     */
    @Override
    public int insertFinAuxiliary(FinAuxiliary finAuxiliary)
    {
        finAuxiliary.setCreateTime(DateUtils.getNowDate());
        return finAuxiliaryMapper.insertFinAuxiliary(finAuxiliary);
    }

    /**
     * 修改辅助核算
     * 
     * @param finAuxiliary 辅助核算
     * @return 结果
     */
    @Override
    public int updateFinAuxiliary(FinAuxiliary finAuxiliary)
    {
        finAuxiliary.setUpdateTime(DateUtils.getNowDate());
        return finAuxiliaryMapper.updateFinAuxiliary(finAuxiliary);
    }

    /**
     * 批量删除辅助核算
     * 
     * @param ids 需要删除的辅助核算主键
     * @return 结果
     */
    @Override
    public int deleteFinAuxiliaryByIds(Long[] ids)
    {
        return finAuxiliaryMapper.deleteFinAuxiliaryByIds(ids);
    }

    /**
     * 删除辅助核算信息
     * 
     * @param id 辅助核算主键
     * @return 结果
     */
    @Override
    public int deleteFinAuxiliaryById(Long id)
    {
        return finAuxiliaryMapper.deleteFinAuxiliaryById(id);
    }

    /**
     * 按类型查询辅助核算
     * 
     * @param auxType 辅助核算类型
     * @return 辅助核算集合
     */
    @Override
    public List<FinAuxiliary> selectFinAuxiliaryByType(String auxType)
    {
        return finAuxiliaryMapper.selectFinAuxiliaryByType(auxType);
    }
}
