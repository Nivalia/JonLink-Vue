package com.jonlink.system.mapper;

import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * 保单收入确认Mapper接口
 *
 * @author jonlink
 * @date 2026-08-23
 */
public interface FinRevenueMapper
{
    /**
     * 查询保单用于收入确认
     *
     * @param policyId 保单ID
     * @return 保单数据
     */
    public Map<String, Object> getPolicyForRevenue(@Param("policyId") Long policyId);

    /**
     * 更新保单收入确认状态
     *
     * @param policyId       保单ID
     * @param confirmed      确认状态(0/1)
     * @param voucherId      关联凭证ID(确认时填,反确认时清空)
     */
    public void updateRevenueStatus(@Param("policyId") Long policyId,
                                    @Param("confirmed") String confirmed,
                                    @Param("voucherId") Long voucherId);

    /**
     * 查询收入汇总
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 汇总数据
     */
    public Map<String, Object> summary(@Param("startDate") String startDate,
                                       @Param("endDate") String endDate);
}
