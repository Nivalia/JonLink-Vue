package com.jonlink.system.mapper;

import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * 税务管理Mapper接口
 *
 * @author jonlink
 */
public interface FinTaxMapper
{
    /**
     * 查询期间内进项税额合计(科目编码2221-应交增值税)
     * @param periodCode 期间编码
     * @return 进项税借方合计
     */
    Map<String, Object> sumInputTaxByPeriod(@Param("periodCode") String periodCode);

    /**
     * 查询期间内销项税额合计(科目编码2221-应交增值税)
     * @param periodCode 期间编码
     * @return 销项税贷方合计
     */
    Map<String, Object> sumOutputTaxByPeriod(@Param("periodCode") String periodCode);

    /**
     * 查询期间内收入类科目贷方发生额合计(用于所得税计算)
     * @param periodCode 期间编码
     * @return 收入合计
     */
    java.math.BigDecimal sumIncomeByPeriod(@Param("periodCode") String periodCode);

    /**
     * 查询期间内费用类科目借方发生额合计(用于所得税计算)
     * @param periodCode 期间编码
     * @return 费用合计
     */
    java.math.BigDecimal sumExpenseByPeriod(@Param("periodCode") String periodCode);
}
