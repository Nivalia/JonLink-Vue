package com.jonlink.system.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonlink.common.exception.ServiceException;
import com.jonlink.system.domain.FinLedgerVoucherLog;
import com.jonlink.system.domain.FinVoucher;
import com.jonlink.system.domain.FinVoucherEntry;
import com.jonlink.system.domain.FinVoucherTemplate;
import com.jonlink.system.mapper.FinLedgerVoucherLogMapper;
import com.jonlink.system.mapper.FinSubjectMapper;
import com.jonlink.system.mapper.FinVoucherTemplateMapper;
import com.jonlink.system.service.IFinLedgerBookEngine;
import com.jonlink.system.service.IFinVoucherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 台账记账引擎实现
 * @author jonlink
 */
@Service
public class FinLedgerBookEngineImpl implements IFinLedgerBookEngine
{
    private static final Logger log = LoggerFactory.getLogger(FinLedgerBookEngineImpl.class);

    @Autowired
    private FinVoucherTemplateMapper templateMapper;
    @Autowired
    private FinSubjectMapper subjectMapper;
    @Autowired
    private IFinVoucherService voucherService;
    @Autowired
    private FinLedgerVoucherLogMapper logMapper;

    private static final ObjectMapper OM = new ObjectMapper();

    /** templateCode -> bookType 枚举(0=台账记账 1=上游结算 2=下游结算 3=客户收款 4=费用报销) */
    private static final Map<String, String> BOOK_TYPE_MAP = new HashMap<>();
    static {
        BOOK_TYPE_MAP.put("ledger_book",      "0");
        BOOK_TYPE_MAP.put("settle_up",        "1");
        BOOK_TYPE_MAP.put("settle_down",      "2");
        BOOK_TYPE_MAP.put("receipt_customer", "3");
        BOOK_TYPE_MAP.put("expense_pay",      "4");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FinVoucher book(String templateCode,
                           Long ledgerId,
                           String policyNo,
                           Map<String, Object> ledgerContext,
                           String periodCode,
                           String voucherDate,
                           String operator)
    {
        if (templateCode == null) throw new ServiceException("模板编码不能为空");
        FinVoucherTemplate tpl = templateMapper.selectFinVoucherTemplateByCode(templateCode);
        if (tpl == null) throw new ServiceException("模板不存在: " + templateCode);
        if (!"1".equals(tpl.getStatus())) throw new ServiceException("模板已禁用: " + templateCode);

        // 解析 entries_json
        List<Map<String, Object>> entriesSpec;
        try {
            entriesSpec = OM.readValue(tpl.getEntriesJson(), new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            throw new ServiceException("模板 entries_json 解析失败: " + e.getMessage());
        }
        if (entriesSpec == null || entriesSpec.isEmpty()) throw new ServiceException("模板分录为空");

        // 替换 {policyNo} / {settleNo} / {billNo} / {expenseNo} 占位
        String summary = tpl.getVoucherSummary();
        if (summary != null) summary = replacePlaceholders(summary, policyNo);
        String[] placeholders = extractPlaceholders(tpl.getEntriesJson());
        for (String ph : placeholders) {
            // 已在每条 entry 的 summary 中替换
        }

        // 构造 voucher
        FinVoucher voucher = new FinVoucher();
        voucher.setPeriodCode(periodCode);
        try {
            voucher.setVoucherDate(voucherDate == null ? null : new java.text.SimpleDateFormat("yyyy-MM-dd").parse(voucherDate));
        } catch (Exception ex) {
            throw new ServiceException("凭证日期格式错误: " + voucherDate + " (应为 yyyy-MM-dd)");
        }
        voucher.setSummary(summary != null ? summary : policyNo);
        voucher.setSourceType(templateCode);
        voucher.setSourceId(ledgerId);
        voucher.setVoucherMaker(operator);

        // 构造 entries
        List<FinVoucherEntry> entries = new ArrayList<>();
        int sort = 1;
        for (Map<String, Object> spec : entriesSpec) {
            String direction = (String) spec.get("direction");
            String subjectCode = (String) spec.get("subjectCode");
            String entrySummaryTpl = (String) spec.get("summary");
            String amountField = (String) spec.get("amountField");

            // 金额
            Object raw = ledgerContext.get(amountField);
            if (raw == null) {
                throw new ServiceException("缺少金额字段: " + amountField + "(模板: " + templateCode + ")");
            }
            BigDecimal amount = toBigDecimal(raw);
            if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
                log.warn("跳过 0 金额分录: {} / {}", subjectCode, amountField);
                continue;
            }

            // 科目 -> id
            Long subjectId = subjectMapper.selectIdByCode(subjectCode);
            if (subjectId == null) throw new ServiceException("科目不存在: " + subjectCode);

            FinVoucherEntry e = new FinVoucherEntry();
            e.setSubjectId(subjectId);
            e.setSummary(replacePlaceholders(entrySummaryTpl, policyNo));
            e.setDebitAmount("debit".equals(direction) ? amount : BigDecimal.ZERO);
            e.setCreditAmount("credit".equals(direction) ? amount : BigDecimal.ZERO);
            e.setSortOrder((long) (sort++));
            entries.add(e);
        }
        if (entries.isEmpty()) throw new ServiceException("所有分录金额均为 0,无法生成凭证");
        voucher.setEntries(entries);

        // 调 saveWithEntries(状态=0 草稿)
        Long voucherId;
        try {
            voucherId = voucherService.saveWithEntries(voucher);
        } catch (ServiceException e) {
            if (e.getMessage() != null && e.getMessage().contains("Duplicate") && e.getMessage().contains("uk_source")) {
                throw new ServiceException("该台账(" + templateCode + ")已记账,不能重复");
            }
            throw e;
        } catch (Exception e) {
            // MyBatis Duplicate 走 SQLIntegrityConstraintViolationException
            String m = e.getMessage();
            if (m != null && m.contains("Duplicate") && m.contains("uk_source")) {
                throw new ServiceException("该台账(" + templateCode + ")已记账,不能重复");
            }
            throw e;
        }

        // 自动审核并过账
        try {
            voucherService.audit(voucherId, operator);
            voucherService.post(voucherId, operator);
            log.info("凭证已自动审核过账: voucherId={}", voucherId);
        } catch (Exception e) {
            log.warn("自动审核过账失败: {}", e.getMessage());
        }

        // 写 log
        FinLedgerVoucherLog logRow = new FinLedgerVoucherLog();
        logRow.setLedgerId(ledgerId == null ? 0L : ledgerId);
        logRow.setLedgerPolicyNo(policyNo);
        BigDecimal ledgerAmount = entries.stream().map(x -> x.getDebitAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        logRow.setLedgerAmount(ledgerAmount);
        logRow.setVoucherId(voucherId);
        // 取凭证号
        FinVoucher saved = voucherService.selectFinVoucherById(voucherId);
        logRow.setVoucherNo(saved != null ? saved.getVoucherNo() : null);
        logRow.setBookType(BOOK_TYPE_MAP.getOrDefault(templateCode, "0"));
        logRow.setBookUser(operator);
        logRow.setBookTime(new Date());
        logRow.setStatus("1");
        logRow.setRemark("台账自动记账: " + templateCode);
        logRow.setCreateBy(operator);
        logMapper.insertFinLedgerVoucherLog(logRow);

        // 重新读 voucher(含 entries)
        return saved;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unbook(Long logId, String operator)
    {
        FinLedgerVoucherLog row = logMapper.selectFinLedgerVoucherLogById(logId);
        if (row == null) throw new ServiceException("日志不存在: " + logId);
        if (!"1".equals(row.getStatus())) throw new ServiceException("日志已反记账: " + logId);

        // 反过账(已过账 -> 草稿;草稿/已作废 -> 跳过,避免事务标记 rollback-only)
        FinVoucher v = voucherService.selectFinVoucherById(row.getVoucherId());
        if (v != null && "2".equals(v.getStatus())) {
            voucherService.unpost(row.getVoucherId(), operator);
        } else {
            log.warn("凭证 {} 非已过账状态(status={}),跳过 unpost", row.getVoucherNo(), v == null ? "?" : v.getStatus());
        }

        // 标 log.status=0
        row.setStatus("0");
        row.setUpdateBy(operator);
        row.setRemark((row.getRemark() == null ? "" : row.getRemark()) + "\n反记账 by " + operator + " @ " + new Date());
        logMapper.updateFinLedgerVoucherLog(row);
    }

    @Override
    public List<FinLedgerVoucherLog> listByLedgerId(Long ledgerId)
    {
        FinLedgerVoucherLog q = new FinLedgerVoucherLog();
        q.setLedgerId(ledgerId);
        return logMapper.selectFinLedgerVoucherLogList(q);
    }

    // ===== helpers =====
    private static String replacePlaceholders(String s, String value)
    {
        if (s == null) return null;
        String[] keys = {"policyNo", "settleNo", "billNo", "expenseNo"};
        for (String k : keys) s = s.replace("{" + k + "}", value == null ? "" : value);
        return s;
    }

    private static String[] extractPlaceholders(String json) { return new String[0]; }

    private static BigDecimal toBigDecimal(Object o)
    {
        if (o == null) return null;
        if (o instanceof BigDecimal) return (BigDecimal) o;
        if (o instanceof Number) return new BigDecimal(o.toString());
        try { return new BigDecimal(o.toString()); } catch (Exception e) { return null; }
    }
}