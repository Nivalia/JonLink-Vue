package com.jonlink.system.service.impl;

import com.jonlink.system.mapper.FinSubjectMapper;
import com.jonlink.system.mapper.FinVoucherMapper;
import com.jonlink.system.service.IFinReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 报表引擎实现
 * @author jonlink
 */
@Service
public class FinReportServiceImpl implements IFinReportService
{
    @Autowired private FinVoucherMapper voucherMapper;
    @Autowired private FinSubjectMapper subjectMapper;

    @Override
    public Object subjectBalance(String periodCode)
    {
        if (periodCode == null || periodCode.length() < 6) return Collections.emptyList();
        List<Map<String, Object>> rows = voucherMapper.sumEntriesBySubjectInPeriod(periodCode);
        List<Map<String, Object>> subjects = subjectMapper.listLeafSubjects();
        Map<Long, Map<String, Object>> map = new LinkedHashMap<>();
        for (Map<String, Object> s : subjects) {
            Long id = ((Number) s.get("id")).longValue();
            Map<String, Object> r = new LinkedHashMap<>();
            r.put("subjectId", id);
            r.put("subjectCode", s.get("subjectCode"));
            r.put("subjectName", s.get("subjectName"));
            r.put("subjectType", s.get("subjectType"));
            r.put("balanceDirection", s.get("balanceDirection"));
            r.put("debitTotal", BigDecimal.ZERO);
            r.put("creditTotal", BigDecimal.ZERO);
            r.put("endBalance", BigDecimal.ZERO);
            map.put(id, r);
        }
        for (Map<String, Object> row : rows) {
            Long sid = ((Number) row.get("subjectId")).longValue();
            BigDecimal d = (BigDecimal) row.getOrDefault("debitTotal", BigDecimal.ZERO);
            BigDecimal c = (BigDecimal) row.getOrDefault("creditTotal", BigDecimal.ZERO);
            Map<String, Object> r = map.get(sid);
            if (r == null) continue;
            r.put("debitTotal", d);
            r.put("creditTotal", c);
            String dir = (String) r.get("balanceDirection");
            BigDecimal bal = "0".equals(dir) ? d.subtract(c) : c.subtract(d);
            r.put("endBalance", bal);
        }
        return new ArrayList<>(map.values());
    }

    @Override
    public Object incomeStatement(String periodCode)
    {
        if (periodCode == null) return Collections.emptyList();
        List<Map<String, Object>> result = new ArrayList<>();
        BigDecimal totalIncome = BigDecimal.ZERO;  // 上游佣金
        BigDecimal totalExpense = BigDecimal.ZERO;  // 下游佣金

        // 查询返佣收入 (600102) - 上游佣金
        Long incomeSubjectId = subjectMapper.selectIdByCode("600102");
        if (incomeSubjectId != null) {
            Map<String, Object> agg = voucherMapper.sumDebitCreditBySubjectPeriod(incomeSubjectId, periodCode);
            BigDecimal debit = agg != null ? (BigDecimal) agg.getOrDefault("debitTotal", BigDecimal.ZERO) : BigDecimal.ZERO;
            BigDecimal credit = agg != null ? (BigDecimal) agg.getOrDefault("creditTotal", BigDecimal.ZERO) : BigDecimal.ZERO;
            totalIncome = credit.subtract(debit);
            Map<String, Object> r = new LinkedHashMap<>();
            r.put("subjectCode", "600102");
            r.put("subjectName", "上游佣金");
            r.put("amount", totalIncome);
            result.add(r);
        }

        // 查询佣金支出 (640101) - 下游佣金
        Long expenseSubjectId = subjectMapper.selectIdByCode("640101");
        if (expenseSubjectId != null) {
            Map<String, Object> agg = voucherMapper.sumDebitCreditBySubjectPeriod(expenseSubjectId, periodCode);
            BigDecimal debit = agg != null ? (BigDecimal) agg.getOrDefault("debitTotal", BigDecimal.ZERO) : BigDecimal.ZERO;
            BigDecimal credit = agg != null ? (BigDecimal) agg.getOrDefault("creditTotal", BigDecimal.ZERO) : BigDecimal.ZERO;
            totalExpense = debit.subtract(credit);
            Map<String, Object> r = new LinkedHashMap<>();
            r.put("subjectCode", "640101");
            r.put("subjectName", "下游佣金");
            r.put("amount", totalExpense);
            result.add(r);
        }

        // 利润合计 = 上游佣金 - 下游佣金
        Map<String, Object> profit = new LinkedHashMap<>();
        profit.put("subjectCode", "");
        profit.put("subjectName", "利润合计");
        profit.put("amount", totalIncome.subtract(totalExpense));
        result.add(profit);
        return result;
    }

    @Override
    public Object balanceSheet(String periodCode)
    {
        if (periodCode == null) return new LinkedHashMap<>();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("assets", buildBalanceSection("1", periodCode));
        result.put("liabilities", buildBalanceSection("2", periodCode));
        result.put("equity", buildBalanceSection("3", periodCode));
        return result;
    }

    private List<Map<String, Object>> buildBalanceSection(String subjectType, String periodCode)
    {
        List<Map<String, Object>> subjects = subjectMapper.listBySubjectType(subjectType);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> s : subjects) {
            BigDecimal bal = voucherMapper.getSubjectBalance((Long) s.get("id"), periodCode);
            Map<String, Object> r = new LinkedHashMap<>();
            r.put("subjectCode", s.get("subjectCode"));
            r.put("subjectName", s.get("subjectName"));
            r.put("balance", bal == null ? BigDecimal.ZERO : bal);
            result.add(r);
        }
        return result;
    }

    @Override
    public Object ledgerPerformance(String startDate, String endDate)
    {
        return voucherMapper.sumLedgerPerformance(startDate, endDate);
    }
}