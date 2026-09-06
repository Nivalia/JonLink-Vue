package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.FinSubject;

/**
 * 会计科目Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public interface FinSubjectMapper 
{
    /**
     * 查询会计科目
     * 
     * @param id 会计科目主键
     * @return 会计科目
     */
    public FinSubject selectFinSubjectById(Long id);

    /** 按科目编码查 ID(末级校验在调用方) */
    public Long selectIdByCode(String subjectCode);

    /** 末级科目(用于余额表) */
    public java.util.List<java.util.Map<String, Object>> listLeafSubjects();

    /** 按 subject_type 取(资产=1 负债=2 权益=3 成本=4 损益=5) */
    public java.util.List<java.util.Map<String, Object>> listBySubjectType(String subjectType);

    /** 指定类型的末级科目列表 */
    public List<FinSubject> listLeafSubjectsByType(String subjectType);

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
     * 删除会计科目
     * 
     * @param id 会计科目主键
     * @return 结果
     */
    public int deleteFinSubjectById(Long id);

    /**
     * 批量删除会计科目
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinSubjectByIds(Long[] ids);
}
