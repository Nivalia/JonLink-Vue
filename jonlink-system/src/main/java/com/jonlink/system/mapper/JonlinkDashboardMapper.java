package com.jonlink.system.mapper;

import java.util.List;
import java.util.Map;

/**
 * 业务看板 Mapper（纯 SQL 聚合，不修改任何业务表）
 */
public interface JonlinkDashboardMapper
{
    /** 产品 KPI */
    Map<String, Object> kpiProduct();

    /** 渠道 KPI */
    Map<String, Object> kpiChannel();

    /** 业务员 KPI */
    Map<String, Object> kpiSalesman();

    /** 台账 KPI */
    Map<String, Object> kpiLedger();

    /** 保费/利润趋势 */
    List<Map<String, Object>> premiumTrend(Integer days);

    /** 保险公司占比 */
    List<Map<String, Object>> companyShare();

    /** 渠道 TOP N */
    List<Map<String, Object>> channelTop(Integer limit);

    /** 产品上下架分布 */
    List<Map<String, Object>> productShelf();

    /** 产品政策类型分布 */
    List<Map<String, Object>> productPolicyType();

    /** 产品扣税分布 */
    List<Map<String, Object>> productTax();

    /** 产品按保险公司分布 */
    List<Map<String, Object>> productCompany();

    /** 业务员 TOP N（按保费） */
    List<Map<String, Object>> salesmanTop(Integer limit);

    /** 最近台账明细 */
    List<Map<String, Object>> recentLedger(Integer limit);

    /** 佣金汇总(本月) */
    Map<String, Object> commissionSummary();

    /** 佣金月度趋势(近12个月) */
    List<Map<String, Object>> commissionTrend();

    /** 税费分布 */
    List<Map<String, Object>> taxBreakdown();

    /** 发票统计 */
    Map<String, Object> invoiceStats();

    /** 科目余额 TOP10 */
    List<Map<String, Object>> subjectBalanceTop10(Integer limit);

    /** 退保率统计 */
    Map<String, Object> surrenderRate();

    /** 公众号运营指标 */
    Map<String, Object> mpOperationStats();

    /** 险种分布（按台账保费） */
    List<Map<String, Object>> insuranceTypeDist();

    /** 续保率统计 */
    Map<String, Object> renewalRate();

    /** 活跃度统计（本月有保单的产品占比） */
    Map<String, Object> activityRate();

    /** 按险别分布（产品） */
    List<Map<String, Object>> productByType();
}