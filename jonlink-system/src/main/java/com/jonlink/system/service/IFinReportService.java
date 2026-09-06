package com.jonlink.system.service;

/**
 * 报表引擎
 * - subjectBalance(科目余额表)
 * - trialBalance(试算平衡表 = 余额表的扩展,带期初/期末)
 * - incomeStatement(利润表)
 * - balanceSheet(资产负债表)
 * - ledgerPerformance(台账业绩)
 *
 * @author jonlink
 */
public interface IFinReportService
{
    /**
     * 科目余额表:期间内各科目期末借/贷余额
     */
    Object subjectBalance(String periodCode);

    /**
     * 利润表:按损益类科目 期间发生额
     */
    Object incomeStatement(String periodCode);

    /**
     * 资产负债表:按资产/负债/权益类科目期末余额
     * 返回 Map: assets / liabilities / equity + total
     */
    Object balanceSheet(String periodCode);

    /**
     * 台账业绩:按 channel_type / insurance_type / 月度聚合保费/佣金/利润
     * 返回 List<Map>: 每条 { channel, insuranceType, yearMonth, premium, upCommission, downCommission, profit, count }
     */
    Object ledgerPerformance(String startDate, String endDate);
}