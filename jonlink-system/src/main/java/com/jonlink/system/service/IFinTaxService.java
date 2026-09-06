package com.jonlink.system.service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 税务管理服务接口
 * - 增值税(进项/销项)计算
 * - 所得税计算
 * - 税务汇总
 *
 * @author jonlink
 */
public interface IFinTaxService
{
    /**
     * 计算增值税(进项/销项)
     * @param periodCode 期间编码(yyyyMM)
     * @return 包含 inputTax(进项税), outputTax(销项税), taxPayable(应纳税额)
     */
    Map<String, Object> calculateVAT(String periodCode);

    /**
     * 计算所得税
     * @param periodCode 期间编码(yyyyMM)
     * @return 应纳所得税额
     */
    BigDecimal calculateIncomeTax(String periodCode);

    /**
     * 税务汇总
     * @param periodCode 期间编码(yyyyMM)
     * @return 包含增值税、所得税等税务汇总信息
     */
    Map<String, Object> taxSummary(String periodCode);
}
