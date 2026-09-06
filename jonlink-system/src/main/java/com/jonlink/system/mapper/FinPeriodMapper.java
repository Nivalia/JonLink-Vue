package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.FinPeriod;

/**
 * 会计期间Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public interface FinPeriodMapper 
{
    /**
     * 查询会计期间
     * 
     * @param id 会计期间主键
     * @return 会计期间
     */
    public FinPeriod selectFinPeriodById(Long id);

    /**
     * 查询会计期间列表
     * 
     * @param finPeriod 会计期间
     * @return 会计期间集合
     */
    public List<FinPeriod> selectFinPeriodList(FinPeriod finPeriod);

    /**
     * 新增会计期间
     * 
     * @param finPeriod 会计期间
     * @return 结果
     */
    public int insertFinPeriod(FinPeriod finPeriod);

    /**
     * 修改会计期间
     * 
     * @param finPeriod 会计期间
     * @return 结果
     */
    public int updateFinPeriod(FinPeriod finPeriod);

    /**
     * 删除会计期间
     * 
     * @param id 会计期间主键
     * @return 结果
     */
    public int deleteFinPeriodById(Long id);

    /**
     * 批量删除会计期间
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinPeriodByIds(Long[] ids);

    /** 按 period_code 查(凭证录入时用) */
    public FinPeriod selectFinPeriodByCode(String periodCode);
}
