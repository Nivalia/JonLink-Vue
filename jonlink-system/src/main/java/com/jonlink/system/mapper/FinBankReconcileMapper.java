package com.jonlink.system.mapper;

import java.util.List;
import java.util.Map;
import com.jonlink.system.domain.FinBankReconcile;

/**
 * 银行对账Mapper接口
 *
 * @author jonlink
 */
public interface FinBankReconcileMapper
{
    public FinBankReconcile selectFinBankReconcileById(Long id);

    public List<FinBankReconcile> selectFinBankReconcileList(FinBankReconcile reconcile);

    public int insertFinBankReconcile(FinBankReconcile reconcile);

    public int updateFinBankReconcile(FinBankReconcile reconcile);

    public int deleteFinBankReconcileById(Long id);

    public int deleteFinBankReconcileByIds(Long[] ids);

    /** 批量插入银行对账单 */
    public int batchInsert(List<FinBankReconcile> list);

    /** 查询未对账的银行流水 */
    public List<FinBankReconcile> selectUnmatched(Long bankAccountId);

    /** 查询未匹配的系统流水 */
    public List<Map<String, Object>> selectUnmatchedSystemFlows(Long bankAccountId, String startDate, String endDate);

    /** 生成余额调节表 */
    public Map<String, Object> getReconciliationReport(Long bankAccountId, String periodCode);
}
