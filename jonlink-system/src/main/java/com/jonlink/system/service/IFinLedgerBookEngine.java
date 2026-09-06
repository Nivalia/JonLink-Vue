package com.jonlink.system.service;

import com.jonlink.system.domain.FinVoucher;
import java.util.Map;

/**
 * 台账记账引擎
 * - 根据 templateCode 读取 fin_voucher_template.entries_json
 * - 替换 {policyNo} / {settleNo} / {billNo} / {expenseNo} 占位
 * - 从 ledgerContext 取 amountField 对应金额
 * - 调用 FinVoucherService.saveWithEntries 生成凭证(状态=草稿)
 * - 写 fin_ledger_voucher_log
 *
 * @author jonlink
 * @date 2026-08-19
 */
public interface IFinLedgerBookEngine
{
    /**
     * 通用记账入口
     *
     * @param templateCode    模板编码 (ledger_book / settle_up / settle_down / receipt_customer / expense_pay)
     * @param ledgerId        台账行ID(保单ID/结算单ID 等;0 表示独立结算)
     * @param policyNo        保单号 / 结算号(填入 {policyNo} / {settleNo} 占位)
     * @param ledgerContext   金额上下文:Map<amountField, BigDecimal>
     * @param periodCode      期间 (如 202608)
     * @param voucherDate     凭证日期 yyyy-MM-dd
     * @param operator        操作用户
     * @return 生成凭证
     */
    FinVoucher book(String templateCode,
                    Long ledgerId,
                    String policyNo,
                    Map<String, Object> ledgerContext,
                    String periodCode,
                    String voucherDate,
                    String operator);

    /**
     * 反记账:根据 logId 找到关联凭证,反过账(已过账→草稿),并将 log.status=0
     */
    void unbook(Long logId, String operator);

    /**
     * 仅查 log(对外)
     */
    java.util.List<com.jonlink.system.domain.FinLedgerVoucherLog> listByLedgerId(Long ledgerId);
}