package com.jonlink.system.service;

import java.util.List;
import java.util.Map;
import com.jonlink.system.domain.FinBankReconcile;

/**
 * 银行对账服务接口
 *
 * @author jonlink
 */
public interface IFinBankReconcileService
{
    /** 查询列表 */
    List<FinBankReconcile> list(FinBankReconcile query);

    /** 查询单个 */
    FinBankReconcile getById(Long id);

    /** 批量导入银行流水 */
    int batchImport(Long bankAccountId, List<FinBankReconcile> records, String operator);

    /** 手动对账(银行流水 ↔ 系统流水) */
    void reconcile(Long reconcileId, Long cashFlowId, String operator);

    /** 取消对账 */
    void cancelReconcile(Long reconcileId, String operator);

    /** 查询未对账流水 */
    Map<String, Object> getUnmatched(Long bankAccountId);

    /** 生成余额调节表 */
    Map<String, Object> getReconciliationReport(Long bankAccountId, String periodCode);
}
