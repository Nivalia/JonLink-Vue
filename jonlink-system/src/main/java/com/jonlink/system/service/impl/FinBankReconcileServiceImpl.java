package com.jonlink.system.service.impl;

import com.jonlink.common.exception.ServiceException;
import com.jonlink.system.domain.FinBankReconcile;
import com.jonlink.system.mapper.FinBankReconcileMapper;
import com.jonlink.system.service.IFinBankReconcileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 银行对账服务实现
 * @author jonlink
 */
@Service
public class FinBankReconcileServiceImpl implements IFinBankReconcileService
{
    @Autowired private FinBankReconcileMapper reconcileMapper;

    @Override
    public List<FinBankReconcile> list(FinBankReconcile query) {
        return reconcileMapper.selectFinBankReconcileList(query);
    }

    @Override
    public FinBankReconcile getById(Long id) {
        return reconcileMapper.selectFinBankReconcileById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchImport(Long bankAccountId, List<FinBankReconcile> records, String operator) {
        if (records == null || records.isEmpty()) throw new ServiceException("导入数据为空");
        for (FinBankReconcile r : records) {
            r.setBankAccountId(bankAccountId);
            r.setStatus("0");
            r.setCreateBy(operator);
        }
        return reconcileMapper.batchInsert(records);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reconcile(Long reconcileId, Long cashFlowId, String operator) {
        FinBankReconcile r = reconcileMapper.selectFinBankReconcileById(reconcileId);
        if (r == null) throw new ServiceException("对账记录不存在");
        if (!"0".equals(r.getStatus())) throw new ServiceException("该记录已对账");

        r.setStatus("1");
        r.setCashFlowId(cashFlowId);
        r.setUpdateBy(operator);
        reconcileMapper.updateFinBankReconcile(r);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelReconcile(Long reconcileId, String operator) {
        FinBankReconcile r = reconcileMapper.selectFinBankReconcileById(reconcileId);
        if (r == null) throw new ServiceException("对账记录不存在");
        if ("0".equals(r.getStatus())) throw new ServiceException("该记录未对账，无需取消");

        r.setStatus("0");
        r.setCashFlowId(null);
        r.setUpdateBy(operator);
        reconcileMapper.updateFinBankReconcile(r);
    }

    @Override
    public Map<String, Object> getUnmatched(Long bankAccountId) {
        Map<String, Object> result = new HashMap<>();
        result.put("bankFlows", reconcileMapper.selectUnmatched(bankAccountId));
        result.put("systemFlows", reconcileMapper.selectUnmatchedSystemFlows(bankAccountId, "2020-01-01", "2099-12-31"));
        return result;
    }

    @Override
    public Map<String, Object> getReconciliationReport(Long bankAccountId, String periodCode) {
        return reconcileMapper.getReconciliationReport(bankAccountId, periodCode);
    }
}
