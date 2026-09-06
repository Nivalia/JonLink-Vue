package com.jonlink.system.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.jonlink.common.exception.ServiceException;
import com.jonlink.common.utils.DateUtils;
import com.jonlink.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jonlink.system.mapper.FinVoucherMapper;
import com.jonlink.system.mapper.FinVoucherEntryMapper;
import com.jonlink.system.mapper.FinSubjectMapper;
import com.jonlink.system.mapper.FinPeriodMapper;
import com.jonlink.system.domain.FinVoucher;
import com.jonlink.system.domain.FinVoucherEntry;
import com.jonlink.system.domain.FinSubject;
import com.jonlink.system.domain.FinPeriod;
import com.jonlink.system.service.IFinVoucherService;

/**
 * 记账凭证 Service
 *
 * M1 业务方法:
 *  saveWithEntries 多借多贷完整录入
 *  audit/cancelAudit/post/unpost/voidVoucher 状态机
 *  generateVoucherNo 凭证号
 *
 * @author jonlink
 */
@Service
public class FinVoucherServiceImpl implements IFinVoucherService
{
    @Autowired
    private FinVoucherMapper finVoucherMapper;

    @Autowired
    private FinVoucherEntryMapper finVoucherEntryMapper;

    @Autowired
    private FinSubjectMapper finSubjectMapper;

    @Autowired
    private FinPeriodMapper finPeriodMapper;

    // ===== 原有 CRUD =====

    @Override
    public FinVoucher selectFinVoucherById(Long id) {
        return finVoucherMapper.selectFinVoucherById(id);
    }

    @Override
    public List<FinVoucher> selectFinVoucherList(FinVoucher finVoucher) {
        return finVoucherMapper.selectFinVoucherList(finVoucher);
    }

    @Override
    public int insertFinVoucher(FinVoucher finVoucher) {
        finVoucher.setCreateTime(DateUtils.getNowDate());
        return finVoucherMapper.insertFinVoucher(finVoucher);
    }

    @Override
    public int updateFinVoucher(FinVoucher finVoucher) {
        finVoucher.setUpdateTime(DateUtils.getNowDate());
        return finVoucherMapper.updateFinVoucher(finVoucher);
    }

    @Override
    public int deleteFinVoucherByIds(Long[] ids) {
        // 仅 status=0(草稿) 可物理删,其余状态拒绝(避免审计断裂)
        for (Long id : ids) {
            FinVoucher v = finVoucherMapper.selectFinVoucherById(id);
            if (v == null) continue;
            if (!"0".equals(v.getStatus())) {
                throw new ServiceException("仅草稿状态凭证可删除,凭证 [" + v.getVoucherNo() + "] 当前状态=" + v.getStatus());
            }
            finVoucherEntryMapper.deleteByVoucherId(id);
            finVoucherMapper.deleteFinVoucherById(id);
        }
        return ids.length;
    }

    @Override
    public int deleteFinVoucherById(Long id) {
        finVoucherEntryMapper.deleteByVoucherId(id);
        return finVoucherMapper.deleteFinVoucherById(id);
    }

    // ===== M1 业务 =====

    /**
     * 多借多贷完整录入
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveWithEntries(FinVoucher voucher) {
        // 1. 基础校验
        if (voucher.getPeriodCode() == null || voucher.getPeriodCode().isEmpty()) {
            throw new ServiceException("期间不能为空");
        }
        if (voucher.getVoucherDate() == null) {
            throw new ServiceException("凭证日期不能为空");
        }
        List<FinVoucherEntry> entries = voucher.getEntries();
        if (entries == null || entries.size() < 2) {
            throw new ServiceException("凭证至少需要两条分录(借+贷)");
        }

        // 2. 校验期间存在
        FinPeriod period = finPeriodMapper.selectFinPeriodByCode(voucher.getPeriodCode());
        if (period == null) {
            throw new ServiceException("期间不存在:" + voucher.getPeriodCode());
        }
        if ("1".equals(period.getStatus())) {
            throw new ServiceException("期间 [" + period.getPeriodName() + "] 已结账,不可新增凭证");
        }

        // 3. 校验每条分录: 末级科目 + 借/贷二选一
        BigDecimal totalDebit = BigDecimal.ZERO;
        BigDecimal totalCredit = BigDecimal.ZERO;
        List<FinSubject> subjects = new ArrayList<>();
        for (FinVoucherEntry e : entries) {
            FinSubject s = finSubjectMapper.selectFinSubjectById(e.getSubjectId());
            if (s == null) {
                throw new ServiceException("分录科目不存在, ID=" + e.getSubjectId());
            }
            if (!"1".equals(s.getIsLeaf())) {
                throw new ServiceException("科目 [" + s.getSubjectCode() + " " + s.getSubjectName() + "] 非末级,不可记账");
            }
            if (!"1".equals(s.getStatus())) {
                throw new ServiceException("科目 [" + s.getSubjectCode() + "] 已停用");
            }
            boolean hasDebit = e.getDebitAmount() != null && e.getDebitAmount().compareTo(BigDecimal.ZERO) > 0;
            boolean hasCredit = e.getCreditAmount() != null && e.getCreditAmount().compareTo(BigDecimal.ZERO) > 0;
            if (hasDebit && hasCredit) {
                throw new ServiceException("分录 [" + s.getSubjectCode() + "] 借贷不能同时有金额");
            }
            if (!hasDebit && !hasCredit) {
                throw new ServiceException("分录 [" + s.getSubjectCode() + "] 借/贷金额必须填一个");
            }
            if (hasDebit) totalDebit = totalDebit.add(e.getDebitAmount());
            if (hasCredit) totalCredit = totalCredit.add(e.getCreditAmount());
            subjects.add(s);
        }

        // 4. 借贷平衡
        if (totalDebit.compareTo(totalCredit) != 0) {
            throw new ServiceException("借贷不平衡: 借=" + totalDebit + " 贷=" + totalCredit);
        }

        // 5. 生成凭证号(期间内流水)
        String voucherNo = generateVoucherNo(voucher.getPeriodCode());
        voucher.setVoucherNo(voucherNo);
        voucher.setTotalDebit(totalDebit);
        voucher.setTotalCredit(totalCredit);
        voucher.setStatus("0"); // 草稿
        voucher.setVoucherMaker(SecurityUtils.getUsername());
        voucher.setCreateBy(SecurityUtils.getUsername());
        voucher.setCreateTime(DateUtils.getNowDate());

        // 6. 头插入
        finVoucherMapper.insertFinVoucher(voucher);
        Long voucherId = voucher.getId();

        // 7. 分录插入(冗余科目编码/名称,免历史凭证受改名影响)
        int order = 1;
        for (int i = 0; i < entries.size(); i++) {
            FinVoucherEntry e = entries.get(i);
            FinSubject s = subjects.get(i);
            e.setVoucherId(voucherId);
            e.setSortOrder((long) order++);
            e.setCreateBy(SecurityUtils.getUsername());
            e.setCreateTime(DateUtils.getNowDate());
            finVoucherEntryMapper.insertFinVoucherEntry(e);
        }

        return voucherId;
    }

    @Override
    public List<FinVoucherEntry> selectEntriesByVoucherId(Long voucherId) {
        return finVoucherEntryMapper.selectByVoucherId(voucherId);
    }

    /**
     * 审核: 仅 0草稿 → 1已审核
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void audit(Long id, String operator) {
        FinVoucher v = finVoucherMapper.selectFinVoucherById(id);
        if (v == null) throw new ServiceException("凭证不存在");
        if (!"0".equals(v.getStatus())) {
            throw new ServiceException("仅草稿状态可审核,当前状态=" + v.getStatus());
        }
        // 校验期间未结账
        FinPeriod period = finPeriodMapper.selectFinPeriodByCode(v.getPeriodCode());
        if (period != null && "1".equals(period.getStatus())) {
            throw new ServiceException("期间已结账,不可审核");
        }
        FinVoucher upd = new FinVoucher();
        upd.setId(id);
        upd.setStatus("1");
        upd.setAuditor(operator);
        upd.setAuditTime(DateUtils.getNowDate());
        upd.setUpdateBy(operator);
        upd.setUpdateTime(DateUtils.getNowDate());
        finVoucherMapper.updateFinVoucher(upd);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelAudit(Long id, String operator) {
        FinVoucher v = finVoucherMapper.selectFinVoucherById(id);
        if (v == null) throw new ServiceException("凭证不存在");
        if (!"1".equals(v.getStatus())) {
            throw new ServiceException("仅已审核状态可反审核,当前状态=" + v.getStatus());
        }
        FinVoucher upd = new FinVoucher();
        upd.setId(id);
        upd.setStatus("0");
        upd.setAuditor(null);
        upd.setAuditTime(null);
        upd.setUpdateBy(operator);
        upd.setUpdateTime(DateUtils.getNowDate());
        finVoucherMapper.updateFinVoucher(upd);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void post(Long id, String operator) {
        FinVoucher v = finVoucherMapper.selectFinVoucherById(id);
        if (v == null) throw new ServiceException("凭证不存在");
        if (!"1".equals(v.getStatus())) {
            throw new ServiceException("仅已审核状态可过账,当前状态=" + v.getStatus());
        }
        // 过账再校验一次借贷平衡(防止并发改动)
        List<FinVoucherEntry> entries = finVoucherEntryMapper.selectByVoucherId(id);
        BigDecimal debit = BigDecimal.ZERO, credit = BigDecimal.ZERO;
        for (FinVoucherEntry e : entries) {
            if (e.getDebitAmount() != null) debit = debit.add(e.getDebitAmount());
            if (e.getCreditAmount() != null) credit = credit.add(e.getCreditAmount());
        }
        if (debit.compareTo(credit) != 0) {
            throw new ServiceException("借贷不平衡,过账拒绝: 借=" + debit + " 贷=" + credit);
        }
        // 校验期间未结账
        FinPeriod period = finPeriodMapper.selectFinPeriodByCode(v.getPeriodCode());
        if (period != null && "1".equals(period.getStatus())) {
            throw new ServiceException("期间已结账,不可过账");
        }
        FinVoucher upd = new FinVoucher();
        upd.setId(id);
        upd.setStatus("2");
        upd.setPoster(operator);
        upd.setPostTime(DateUtils.getNowDate());
        upd.setUpdateBy(operator);
        upd.setUpdateTime(DateUtils.getNowDate());
        finVoucherMapper.updateFinVoucher(upd);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unpost(Long id, String operator) {
        FinVoucher v = finVoucherMapper.selectFinVoucherById(id);
        if (v == null) throw new ServiceException("凭证不存在");
        if (!"2".equals(v.getStatus())) {
            throw new ServiceException("仅已过账状态可反过账,当前状态=" + v.getStatus());
        }
        // 校验期间未结账
        FinPeriod period = finPeriodMapper.selectFinPeriodByCode(v.getPeriodCode());
        if (period != null && "1".equals(period.getStatus())) {
            throw new ServiceException("期间已结账,不可反过账");
        }
        FinVoucher upd = new FinVoucher();
        upd.setId(id);
        upd.setStatus("1");
        upd.setPoster(null);
        upd.setPostTime(null);
        upd.setUpdateBy(operator);
        upd.setUpdateTime(DateUtils.getNowDate());
        finVoucherMapper.updateFinVoucher(upd);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void voidVoucher(Long id, String operator, String reason) {
        FinVoucher v = finVoucherMapper.selectFinVoucherById(id);
        if (v == null) throw new ServiceException("凭证不存在");
        if ("3".equals(v.getStatus())) {
            throw new ServiceException("凭证已作废,不可重复作废");
        }
        // 期间已结账不可作废
        FinPeriod period = finPeriodMapper.selectFinPeriodByCode(v.getPeriodCode());
        if (period != null && "1".equals(period.getStatus())) {
            throw new ServiceException("期间已结账,不可作废凭证(请先反结账)");
        }
        FinVoucher upd = new FinVoucher();
        upd.setId(id);
        upd.setStatus("3");
        String newRemark = v.getRemark() == null ? "" : v.getRemark();
        newRemark += "\n[作废 by " + operator + (reason == null || reason.isEmpty() ? "" : " 原因:" + reason) + " @ " + DateUtils.getNowDate() + "]";
        upd.setRemark(newRemark);
        upd.setUpdateBy(operator);
        upd.setUpdateTime(DateUtils.getNowDate());
        finVoucherMapper.updateFinVoucher(upd);
    }

    /**
     * 生成期间内下一个凭证号: 记-yyyyMM-NNNN
     */
    @Override
    public String generateVoucherNo(String periodCode) {
        // 期间格式 yyyyMM(8 位)
        String prefix = "记-" + periodCode + "-";
        Integer max = finVoucherMapper.selectMaxVoucherNoSeq(prefix);
        int next = (max == null ? 0 : max) + 1;
        return String.format("%s%04d", prefix, next);
    }

    @Override
    public Map<String, Object> getFullVoucher(Long id) {
        FinVoucher v = finVoucherMapper.selectFinVoucherById(id);
        if (v == null) return null;
        List<FinVoucherEntry> entries = finVoucherEntryMapper.selectByVoucherId(id);
        Map<String, Object> map = new HashMap<>();
        map.put("voucher", v);
        map.put("entries", entries);
        return map;
    }
}