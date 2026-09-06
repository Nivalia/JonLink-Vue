package com.jonlink.system.service.impl;

import com.jonlink.common.exception.ServiceException;
import com.jonlink.system.domain.FinPeriod;
import com.jonlink.system.domain.FinSubject;
import com.jonlink.system.domain.FinVoucher;
import com.jonlink.system.domain.FinVoucherEntry;
import com.jonlink.system.mapper.FinPeriodMapper;
import com.jonlink.system.mapper.FinSubjectMapper;
import com.jonlink.system.mapper.FinVoucherMapper;
import com.jonlink.system.service.IFinClosingService;
import com.jonlink.system.service.IFinVoucherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 期末结转服务实现
 * @author jonlink
 */
@Service
public class FinClosingServiceImpl implements IFinClosingService
{
    private static final Logger log = LoggerFactory.getLogger(FinClosingServiceImpl.class);

    /** 本年利润科目编码 */
    private static final String PROFIT_SUBJECT_CODE = "4103";

    @Autowired private FinSubjectMapper subjectMapper;
    @Autowired private FinVoucherMapper voucherMapper;
    @Autowired private FinPeriodMapper periodMapper;
    @Autowired private IFinVoucherService voucherService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long closePeriod(String periodCode, String operator)
    {
        if (periodCode == null || periodCode.length() < 6)
            throw new ServiceException("期间编码格式错误");

        // 1. 校验期间
        FinPeriod period = periodMapper.selectFinPeriodByCode(periodCode);
        if (period == null) throw new ServiceException("期间不存在: " + periodCode);
        if ("1".equals(period.getStatus())) throw new ServiceException("期间已结账，不可结转");

        // 2. 检查是否已结转(查是否有 source_type='closing' 的凭证)
        FinVoucher q = new FinVoucher();
        q.setPeriodCode(periodCode);
        q.setSourceType("closing");
        List<FinVoucher> existList = voucherMapper.selectFinVoucherList(q);
        if (existList != null && !existList.isEmpty()) {
            throw new ServiceException("该期间已执行期末结转，如需重新结转请先反结转");
        }

        // 3. 查询所有损益类末级科目(subject_type='5', is_leaf='1')
        List<FinSubject> subjects = subjectMapper.listLeafSubjectsByType("5");
        if (subjects == null || subjects.isEmpty()) {
            throw new ServiceException("未找到损益类末级科目");
        }

        // 4. 查询每个科目的余额
        BigDecimal totalIncome = BigDecimal.ZERO;   // 收入合计(贷方余额)
        BigDecimal totalExpense = BigDecimal.ZERO;   // 支出合计(借方余额)
        List<FinVoucherEntry> entries = new ArrayList<>();
        int sort = 1;

        for (FinSubject s : subjects) {
            BigDecimal balance = voucherMapper.getSubjectBalance(s.getId(), periodCode);
            if (balance == null || balance.compareTo(BigDecimal.ZERO) == 0) continue;

            // 损益类科目: balance = debit - credit
            // 如果 balance > 0 → 借方余额(支出类)
            // 如果 balance < 0 → 贷方余额(收入类)
            if (balance.compareTo(BigDecimal.ZERO) > 0) {
                // 支出类: 借方余额，需要结转到本年利润的借方
                totalExpense = totalExpense.add(balance);
                // 分录: 借 本年利润 / 贷 支出科目
                entries.add(createEntry(subjectMapper.selectIdByCode(PROFIT_SUBJECT_CODE), null, balance, sort++));
                entries.add(createEntry(s.getId(), null, null, balance, sort++));
            } else {
                // 收入类: 贷方余额(取绝对值)，需要结转到本年利润的贷方
                BigDecimal absBalance = balance.negate();
                totalIncome = totalIncome.add(absBalance);
                // 分录: 借 收入科目 / 贷 本年利润
                entries.add(createEntry(s.getId(), null, absBalance, sort++));
                entries.add(createEntry(subjectMapper.selectIdByCode(PROFIT_SUBJECT_CODE), null, null, absBalance, sort++));
            }
        }

        if (entries.isEmpty()) {
            throw new ServiceException("该期间无损益发生额，无需结转");
        }

        // 5. 构造凭证
        FinVoucher voucher = new FinVoucher();
        voucher.setPeriodCode(periodCode);
        voucher.setVoucherDate(new Date());
        voucher.setSummary("期末结转-" + periodCode);
        voucher.setSourceType("closing");
        voucher.setEntries(entries);

        // 6. 保存凭证(草稿状态)
        Long voucherId = voucherService.saveWithEntries(voucher);

        log.info("期末结转完成: period={}, income={}, expense={}, voucherId={}",
                periodCode, totalIncome, totalExpense, voucherId);
        return voucherId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reverseClose(String periodCode, String operator)
    {
        if (periodCode == null || periodCode.length() < 6)
            throw new ServiceException("期间编码格式错误");

        // 查找结转凭证
        FinVoucher q = new FinVoucher();
        q.setPeriodCode(periodCode);
        q.setSourceType("closing");
        List<FinVoucher> list = voucherMapper.selectFinVoucherList(q);
        if (list == null || list.isEmpty()) {
            throw new ServiceException("该期间未执行期末结转");
        }

        for (FinVoucher v : list) {
            // 反过账(如果已过账)
            if ("2".equals(v.getStatus())) {
                voucherService.unpost(v.getId(), operator);
            }
            // 删除凭证(草稿状态)
            voucherService.deleteFinVoucherById(v.getId());
        }
        log.info("期末结转反操作完成: period={}, count={}", periodCode, list.size());
    }

    @Override
    public Map<String, Object> preview(String periodCode)
    {
        if (periodCode == null || periodCode.length() < 6)
            throw new ServiceException("期间编码格式错误");

        List<FinSubject> subjects = subjectMapper.listLeafSubjectsByType("5");
        List<Map<String, Object>> details = new ArrayList<>();
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;

        if (subjects != null) {
            for (FinSubject s : subjects) {
                BigDecimal balance = voucherMapper.getSubjectBalance(s.getId(), periodCode);
                if (balance == null || balance.compareTo(BigDecimal.ZERO) == 0) continue;

                Map<String, Object> item = new LinkedHashMap<>();
                item.put("subjectId", s.getId());
                item.put("subjectCode", s.getSubjectCode());
                item.put("subjectName", s.getSubjectName());
                item.put("balance", balance);

                if (balance.compareTo(BigDecimal.ZERO) > 0) {
                    item.put("direction", "expense");
                    totalExpense = totalExpense.add(balance);
                } else {
                    item.put("direction", "income");
                    totalIncome = totalIncome.add(balance.negate());
                }
                details.add(item);
            }
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("periodCode", periodCode);
        result.put("totalIncome", totalIncome);
        result.put("totalExpense", totalExpense);
        result.put("netProfit", totalIncome.subtract(totalExpense));
        result.put("details", details);
        return result;
    }

    private FinVoucherEntry createEntry(Long subjectId, String summary, BigDecimal debit, BigDecimal credit, int sort) {
        FinVoucherEntry e = new FinVoucherEntry();
        e.setSubjectId(subjectId);
        e.setSummary(summary);
        e.setDebitAmount(debit != null ? debit : BigDecimal.ZERO);
        e.setCreditAmount(credit != null ? credit : BigDecimal.ZERO);
        e.setSortOrder((long) sort);
        return e;
    }

    private FinVoucherEntry createEntry(Long subjectId, String summary, BigDecimal amount, int sort) {
        // 借方便捷方法
        return createEntry(subjectId, summary, amount, null, sort);
    }
}
