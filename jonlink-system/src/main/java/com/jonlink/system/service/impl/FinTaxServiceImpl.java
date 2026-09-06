package com.jonlink.system.service.impl;

import com.jonlink.common.exception.ServiceException;
import com.jonlink.system.mapper.FinTaxMapper;
import com.jonlink.system.service.IFinTaxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 税务管理服务实现
 *
 * @author jonlink
 */
@Service
public class FinTaxServiceImpl implements IFinTaxService
{
    private static final Logger log = LoggerFactory.getLogger(FinTaxServiceImpl.class);

    /** 企业所得税税率 25% */
    private static final BigDecimal TAX_RATE = new BigDecimal("0.25");

    @Autowired
    private FinTaxMapper taxMapper;

    @Override
    public Map<String, Object> calculateVAT(String periodCode)
    {
        if (periodCode == null || periodCode.length() < 6)
            throw new ServiceException("期间编码格式错误");

        Map<String, Object> inputTax = taxMapper.sumInputTaxByPeriod(periodCode);
        Map<String, Object> outputTax = taxMapper.sumOutputTaxByPeriod(periodCode);

        BigDecimal inputDebit = toBigDecimal(inputTax.get("totalDebit"));
        BigDecimal inputCredit = toBigDecimal(inputTax.get("totalCredit"));
        BigDecimal outputDebit = toBigDecimal(outputTax.get("totalDebit"));
        BigDecimal outputCredit = toBigDecimal(outputTax.get("totalCredit"));

        // 进项税额 = 借方合计 - 贷方合计 (正数表示进项)
        BigDecimal inputTaxAmount = inputDebit.subtract(inputCredit).max(BigDecimal.ZERO);
        // 销项税额 = 贷方合计 - 借方合计 (正数表示销项)
        BigDecimal outputTaxAmount = outputCredit.subtract(outputDebit).max(BigDecimal.ZERO);
        // 应纳税额 = 销项税额 - 进项税额
        BigDecimal taxPayable = outputTaxAmount.subtract(inputTaxAmount);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("periodCode", periodCode);
        result.put("inputTax", inputTaxAmount);
        result.put("outputTax", outputTaxAmount);
        result.put("taxPayable", taxPayable);

        log.info("VAT计算完成: period={}, input={}, output={}, payable={}",
                periodCode, inputTaxAmount, outputTaxAmount, taxPayable);
        return result;
    }

    @Override
    public BigDecimal calculateIncomeTax(String periodCode)
    {
        if (periodCode == null || periodCode.length() < 6)
            throw new ServiceException("期间编码格式错误");

        BigDecimal income = taxMapper.sumIncomeByPeriod(periodCode);
        BigDecimal expense = taxMapper.sumExpenseByPeriod(periodCode);

        if (income == null) income = BigDecimal.ZERO;
        if (expense == null) expense = BigDecimal.ZERO;

        // 利润总额 = 收入 - 费用
        BigDecimal profit = income.subtract(expense);

        // 应纳所得税 = 利润总额 * 税率 (负数不交税)
        BigDecimal incomeTax = BigDecimal.ZERO;
        if (profit.compareTo(BigDecimal.ZERO) > 0) {
            incomeTax = profit.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        }

        log.info("所得税计算完成: period={}, income={}, expense={}, profit={}, tax={}",
                periodCode, income, expense, profit, incomeTax);
        return incomeTax;
    }

    @Override
    public Map<String, Object> taxSummary(String periodCode)
    {
        Map<String, Object> vatResult = calculateVAT(periodCode);
        BigDecimal incomeTax = calculateIncomeTax(periodCode);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("periodCode", periodCode);
        result.put("vat", vatResult);
        result.put("incomeTax", incomeTax);

        // 税费合计
        BigDecimal vatPayable = (BigDecimal) vatResult.get("taxPayable");
        if (vatPayable == null) vatPayable = BigDecimal.ZERO;
        result.put("totalTax", vatPayable.add(incomeTax));

        log.info("税务汇总完成: period={}, totalTax={}", periodCode, result.get("totalTax"));
        return result;
    }

    private BigDecimal toBigDecimal(Object value)
    {
        if (value == null) return BigDecimal.ZERO;
        if (value instanceof BigDecimal) return (BigDecimal) value;
        return new BigDecimal(value.toString());
    }
}
