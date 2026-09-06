package com.jonlink.system.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import com.jonlink.system.domain.FinCommission;

/**
 * 佣金结算服务接口
 */
public interface IFinCommissionService
{
    List<FinCommission> list(FinCommission query);

    FinCommission getById(Long id);

    /** 根据保费和比例计算佣金金额 */
    BigDecimal calculateCommission(BigDecimal premium, BigDecimal rate, String taxFlag);

    /** 批量创建佣金记录(从保单台账) */
    int batchCreate(List<FinCommission> list, String operator);

    /** 确认佣金(生成应收/应付单) */
    void confirm(Long id, String operator);

    /** 标记已支付 */
    void markPaid(Long id, Long receiptOrPaymentId, String operator);

    /** 汇总统计 */
    Map<String, Object> summary(String startDate, String endDate, String direction);
}
