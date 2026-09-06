package com.jonlink.system.service;

import java.util.List;
import com.jonlink.system.domain.FinSubject;

/**
 * 会计科目Service接口
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public interface IFinSubjectService 
{
    /**
     * 查询会计科目
     * 
     * @param id 会计科目主键
     * @return 会计科目
     */
    public FinSubject selectFinSubjectById(Long id);

    /**
     * 查询会计科目列表
     * 
     * @param finSubject 会计科目
     * @return 会计科目集合
     */
    public List<FinSubject> selectFinSubjectList(FinSubject finSubject);

    /**
     * 新增会计科目
     * 
     * @param finSubject 会计科目
     * @return 结果
     */
    public int insertFinSubject(FinSubject finSubject);

    /**
     * 修改会计科目
     * 
     * @param finSubject 会计科目
     * @return 结果
     */
    public int updateFinSubject(FinSubject finSubject);

    /**
     * 批量删除会计科目
     * 
     * @param ids 需要删除的会计科目主键集合
     * @return 结果
     */
    public int deleteFinSubjectByIds(Long[] ids);

    /**
     * 删除会计科目信息
     * 
     * @param id 会计科目主键
     * @return 结果
     */
    public int deleteFinSubjectById(Long id);
}
