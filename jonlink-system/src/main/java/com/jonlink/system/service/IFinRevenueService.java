package com.jonlink.system.service;

import java.util.Map;

/**
 * 保单收入确认Service接口
 *
 * @author jonlink
 * @date 2026-08-23
 */
public interface IFinRevenueService
{
    /**
     * 确认保单收入
     *
     * @param policyId 保单ID(jonlink_insurance_ledger.id)
     * @param operator 操作人
     */
    public void confirmRevenue(Long policyId, String operator);

    /**
     * 反确认(删除已生成凭证)
     *
     * @param policyId 保单ID
     * @param operator 操作人
     */
    public void reverseRevenue(Long policyId, String operator);

    /**
     * 查询收入汇总
     *
     * @param startDate 开始日期(yyyy-MM-dd)
     * @param endDate   结束日期(yyyy-MM-dd)
     * @return 汇总数据
     */
    public Map<String, Object> summary(String startDate, String endDate);
}
