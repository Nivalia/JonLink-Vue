package com.jonlink.system.service;

import java.util.List;
import com.jonlink.system.domain.FinPeriod;

/**
 * 会计期间Service接口
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public interface IFinPeriodService 
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
     * 批量删除会计期间
     * 
     * @param ids 需要删除的会计期间主键集合
     * @return 结果
     */
    public int deleteFinPeriodByIds(Long[] ids);

    /**
     * 删除会计期间信息
     * 
     * @param id 会计期间主键
     * @return 结果
     */
    public int deleteFinPeriodById(Long id);

    // ===== M1 =====

    /**
     * 期末结账(0未结账 → 1已结账)
     * 校验: 该期间所有凭证 status 必须为 2(已过账);否则拒绝
     */
    public void closePeriod(Long id, String operator);

    /**
     * 反结账(1已结账 → 0未结账)
     * 不校验过账状态(已结账 → 反结账 → 用户自己负责修正)
     */
    public void reopenPeriod(Long id, String operator);
}
