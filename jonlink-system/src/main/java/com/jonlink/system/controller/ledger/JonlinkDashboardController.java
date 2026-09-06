package com.jonlink.system.controller.ledger;

import com.jonlink.common.core.controller.BaseController;
import com.jonlink.common.core.domain.AjaxResult;
import com.jonlink.system.mapper.JonlinkDashboardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务看板 Controller（保险台账 KPI）
 *
 * @author jonlink
 */
@RestController
@RequestMapping("/ledger/dashboard")
// [AUDIT-2026-09-04] 业务看板涉及 KPI/未结保费/佣金/公司份额等敏感数据,加权限校验
// 真生产必须先把 ledger:dashboard:list 权限授权给 super-admin/有看板权限的角色
// 可在 sys_menu.perms 写入 'ledger:dashboard:list',通过 sys_role_menu 关联
@PreAuthorize("@ss.hasPermi('ledger:dashboard:list')")
public class JonlinkDashboardController extends BaseController
{
    @Autowired
    private JonlinkDashboardMapper jonlinkDashboardMapper;

    /** 顶部 6 个 KPI 卡片 */
    @GetMapping("/kpi")
    public AjaxResult kpi()
    {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> product = jonlinkDashboardMapper.kpiProduct();
        Map<String, Object> channel = jonlinkDashboardMapper.kpiChannel();
        Map<String, Object> salesman = jonlinkDashboardMapper.kpiSalesman();
        Map<String, Object> ledger = jonlinkDashboardMapper.kpiLedger();
        data.put("productCount", product.get("product_count"));
        data.put("productActive", product.get("product_active"));
        data.put("channelCount", channel.get("channel_count"));
        data.put("channelActive", channel.get("channel_active"));
        data.put("salesmanCount", salesman.get("salesman_count"));
        data.put("salesmanActive", salesman.get("salesman_active"));
        data.put("totalPremium", ledger.get("total_premium"));
        data.put("totalProfit", ledger.get("total_profit"));
        data.put("totalPolicy", ledger.get("total_policy"));
        data.put("unsettledPremium", ledger.get("unsettled_premium"));
        data.put("unsettledUpPremium", ledger.get("unsettled_up_premium"));
        data.put("unsettledDownPremium", ledger.get("unsettled_down_premium"));
        return success(data);
    }

    /** 保费/利润趋势（近 N 天） */
    @GetMapping("/premiumTrend")
    public AjaxResult premiumTrend(@RequestParam(defaultValue = "30") Integer days)
    {
        List<Map<String, Object>> rows = jonlinkDashboardMapper.premiumTrend(days);
        return success(rows);
    }

    /** 各保险公司保费占比 */
    @GetMapping("/companyShare")
    public AjaxResult companyShare()
    {
        List<Map<String, Object>> rows = jonlinkDashboardMapper.companyShare();
        return success(rows);
    }

    /** 渠道贡献 TOP N */
    @GetMapping("/channelTop")
    public AjaxResult channelTop(@RequestParam(defaultValue = "10") Integer limit)
    {
        List<Map<String, Object>> rows = jonlinkDashboardMapper.channelTop(limit);
        return success(rows);
    }

    /** 产品状态分布 */
    @GetMapping("/productDistribution")
    public AjaxResult productDistribution()
    {
        Map<String, Object> data = new HashMap<>();
        List<Map<String, Object>> shelf = jonlinkDashboardMapper.productShelf();
        List<Map<String, Object>> policy = jonlinkDashboardMapper.productPolicyType();
        List<Map<String, Object>> tax = jonlinkDashboardMapper.productTax();
        List<Map<String, Object>> company = jonlinkDashboardMapper.productCompany();
        data.put("shelf", shelf);
        data.put("policy", policy);
        data.put("tax", tax);
        data.put("company", company);
        return success(data);
    }

    /** 业务员 TOP N（按保费） */
    @GetMapping("/salesmanTop")
    public AjaxResult salesmanTop(@RequestParam(defaultValue = "10") Integer limit)
    {
        List<Map<String, Object>> rows = jonlinkDashboardMapper.salesmanTop(limit);
        return success(rows);
    }

    /** 最近 N 条台账明细 */
    @GetMapping("/recentLedger")
    public AjaxResult recentLedger(@RequestParam(defaultValue = "5") Integer limit)
    {
        List<Map<String, Object>> rows = jonlinkDashboardMapper.recentLedger(limit);
        return success(rows);
    }

    /** 佣金汇总(本月) */
    @GetMapping("/commissionSummary")
    public AjaxResult commissionSummary()
    {
        return success(jonlinkDashboardMapper.commissionSummary());
    }

    /** 佣金月度趋势 */
    @GetMapping("/commissionTrend")
    public AjaxResult commissionTrend()
    {
        return success(jonlinkDashboardMapper.commissionTrend());
    }

    /** 税费分布 */
    @GetMapping("/taxBreakdown")
    public AjaxResult taxBreakdown()
    {
        return success(jonlinkDashboardMapper.taxBreakdown());
    }

    /** 发票统计 */
    @GetMapping("/invoiceStats")
    public AjaxResult invoiceStats()
    {
        return success(jonlinkDashboardMapper.invoiceStats());
    }

    /** 科目余额 TOP10 */
    @GetMapping("/subjectBalanceTop10")
    public AjaxResult subjectBalanceTop10(@RequestParam(defaultValue = "10") Integer limit)
    {
        return success(jonlinkDashboardMapper.subjectBalanceTop10(limit));
    }

    /** 退保率统计 */
    @GetMapping("/surrenderRate")
    public AjaxResult surrenderRate()
    {
        return success(jonlinkDashboardMapper.surrenderRate());
    }

    /** 公众号运营指标 */
    @GetMapping("/mpOperationStats")
    public AjaxResult mpOperationStats()
    {
        return success(jonlinkDashboardMapper.mpOperationStats());
    }

    /** 险种分布（按台账保费） */
    @GetMapping("/insuranceTypeDist")
    public AjaxResult insuranceTypeDist()
    {
        return success(jonlinkDashboardMapper.insuranceTypeDist());
    }

    /** 续保率统计 */
    @GetMapping("/renewalRate")
    public AjaxResult renewalRate()
    {
        return success(jonlinkDashboardMapper.renewalRate());
    }

    /** 活跃度统计 */
    @GetMapping("/activityRate")
    public AjaxResult activityRate()
    {
        return success(jonlinkDashboardMapper.activityRate());
    }

    /** 按险别分布（产品） */
    @GetMapping("/productByType")
    public AjaxResult productByType()
    {
        return success(jonlinkDashboardMapper.productByType());
    }
}