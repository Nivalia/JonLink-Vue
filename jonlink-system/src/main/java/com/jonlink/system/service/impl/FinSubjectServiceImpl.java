package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.FinSubjectMapper;
import com.jonlink.system.domain.FinSubject;
import com.jonlink.system.service.IFinSubjectService;

/**
 * 会计科目Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@Service
public class FinSubjectServiceImpl implements IFinSubjectService 
{
    @Autowired
    private FinSubjectMapper finSubjectMapper;

    /**
     * 查询会计科目
     * 
     * @param id 会计科目主键
     * @return 会计科目
     */
    @Override
    public FinSubject selectFinSubjectById(Long id)
    {
        return finSubjectMapper.selectFinSubjectById(id);
    }

    /**
     * 查询会计科目列表
     * 
     * @param finSubject 会计科目
     * @return 会计科目
     */
    @Override
    public List<FinSubject> selectFinSubjectList(FinSubject finSubject)
    {
        return finSubjectMapper.selectFinSubjectList(finSubject);
    }

    /**
     * 新增会计科目
     * 
     * @param finSubject 会计科目
     * @return 结果
     */
    @Override
    public int insertFinSubject(FinSubject finSubject)
    {
        finSubject.setCreateTime(DateUtils.getNowDate());
        return finSubjectMapper.insertFinSubject(finSubject);
    }

    /**
     * 修改会计科目
     * 
     * @param finSubject 会计科目
     * @return 结果
     */
    @Override
    public int updateFinSubject(FinSubject finSubject)
    {
        finSubject.setUpdateTime(DateUtils.getNowDate());
        return finSubjectMapper.updateFinSubject(finSubject);
    }

    /**
     * 批量删除会计科目
     * 
     * @param ids 需要删除的会计科目主键
     * @return 结果
     */
    @Override
    public int deleteFinSubjectByIds(Long[] ids)
    {
        return finSubjectMapper.deleteFinSubjectByIds(ids);
    }

    /**
     * 删除会计科目信息
     * 
     * @param id 会计科目主键
     * @return 结果
     */
    @Override
    public int deleteFinSubjectById(Long id)
    {
        return finSubjectMapper.deleteFinSubjectById(id);
    }
}
