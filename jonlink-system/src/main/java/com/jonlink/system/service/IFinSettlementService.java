package com.jonlink.system.service;

import com.jonlink.system.domain.FinReceivable;
import com.jonlink.system.domain.FinPayable;
import java.math.BigDecimal;

/**
 * 核销引擎:应收/应付核销(部分/全额)
 * - 核销金额累加到 paid_amount,remain_amount 减少
 * - status:0=未收/付 → 1=部分 → 2=完成
 * - 联动写入 fin_allocation 核销记录
 *
 * @author jonlink
 */
public interface IFinSettlementService
{
    /** 核销应收(收客户钱) */
    FinReceivable cancelReceivable(Long receivableId, BigDecimal amount, String operator);

    /** 核销应收(收客户钱) — 带收款单ID，写核销记录 */
    FinReceivable cancelReceivable(Long receivableId, BigDecimal amount, String operator, Long receiptId);

    /** 核销应付(付上游/下游钱) */
    FinPayable cancelPayable(Long payableId, BigDecimal amount, String operator);

    /** 核销应付(付上游/下游钱) — 带付款单ID，写核销记录 */
    FinPayable cancelPayable(Long payableId, BigDecimal amount, String operator, Long paymentId);
}