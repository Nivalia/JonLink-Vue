package com.jonlink.system.service;

import com.jonlink.system.domain.FinBankAccount;
import com.jonlink.system.domain.FinPayment;
import com.jonlink.system.domain.FinReceipt;
import java.math.BigDecimal;

/**
 * 资金引擎
 * - 收款:FinReceipt.confirm → FinBankAccount.balance += amount + FinCashFlow(direction=in)
 * - 付款:FinPayment.confirm → FinBankAccount.balance -= amount + FinCashFlow(direction=out)
 * - 收款单来源可关联 fin_receivable(自动核销应收)
 * - 付款单来源可关联 fin_payable(自动核销应付)
 *
 * @author jonlink
 */
public interface IFinFundEngine
{
    /** 确认收款 → 余额+流水+(若 sourceType=receivable 则核销) */
    FinReceipt confirmReceipt(Long receiptId, String operator);

    /** 确认付款 → 余额-流水+(若 sourceType=payable 则核销) */
    FinPayment confirmPayment(Long paymentId, String operator);

    /** 选所有 active 账户(下拉用) */
    java.util.List<FinBankAccount> listActiveAccounts();
}