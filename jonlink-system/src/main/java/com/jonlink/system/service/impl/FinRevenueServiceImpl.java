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
import com.jonlink.system.mapper.FinRevenueMapper;
import com.jonlink.system.mapper.FinSubjectMapper;
import com.jonlink.system.mapper.FinPeriodMapper;
import com.jonlink.system.domain.FinVoucher;
import com.jonlink.system.domain.FinVoucherEntry;
import com.jonlink.system.domain.FinPeriod;
import com.jonlink.system.service.IFinRevenueService;
import com.jonlink.system.service.IFinVoucherService;

/**
 * 保单收入确认Service业务层处理
 *
 * @author jonlink
 * @date 2026-08-23
 */
@Service
public class FinRevenueServiceImpl implements IFinRevenueService
{
    /** 应收账款科目编码 */
    private static final String SUBJECT_CODE_RECEIVABLE = "1122";
    /** 主营业务收入科目编码 */
    private static final String SUBJECT_CODE_REVENUE = "6001";

    @Autowired
    private FinRevenueMapper finRevenueMapper;

    @Autowired
    private FinSubjectMapper finSubjectMapper;

    @Autowired
    private FinPeriodMapper finPeriodMapper;

    @Autowired
    private IFinVoucherService finVoucherService;

    /**
     * 确认保单收入
     * 借:应收账款(1122) 贷:主营业务收入(6001)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmRevenue(Long policyId, String operator)
    {
        // 1. 查询保单
        Map<String, Object> policy = finRevenueMapper.getPolicyForRevenue(policyId);
        if (policy == null)
        {
            throw new ServiceException("保单不存在,ID=" + policyId);
        }

        // 2. 检查是否已确认
        Object confirmedFlag = policy.get("revenue_confirmed");
        if ("1".equals(String.valueOf(confirmedFlag)))
        {
            throw new ServiceException("该保单已确认收入,保单号=" + policy.get("policy_no"));
        }

        // 3. 获取保费
        BigDecimal premium = (BigDecimal) policy.get("premium");
        if (premium == null || premium.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new ServiceException("保单保费无效,保单号=" + policy.get("policy_no"));
        }

        // 4. 获取期间(按保单日期)
        Date ledgerDate = (Date) policy.get("ledger_date");
        String periodCode = new java.text.SimpleDateFormat("yyyyMM").format(ledgerDate);
        FinPeriod period = finPeriodMapper.selectFinPeriodByCode(periodCode);
        if (period == null)
        {
            throw new ServiceException("期间不存在:" + periodCode);
        }
        if ("1".equals(period.getStatus()))
        {
            throw new ServiceException("期间 [" + period.getPeriodName() + "] 已结账,不可新增凭证");
        }

        // 5. 查科目ID
        Long receivableSubjectId = finSubjectMapper.selectIdByCode(SUBJECT_CODE_RECEIVABLE);
        Long revenueSubjectId = finSubjectMapper.selectIdByCode(SUBJECT_CODE_REVENUE);
        if (receivableSubjectId == null)
        {
            throw new ServiceException("科目不存在:" + SUBJECT_CODE_RECEIVABLE);
        }
        if (revenueSubjectId == null)
        {
            throw new ServiceException("科目不存在:" + SUBJECT_CODE_REVENUE);
        }

        // 6. 构造凭证
        String policyNo = String.valueOf(policy.get("policy_no"));
        FinVoucher voucher = new FinVoucher();
        voucher.setPeriodCode(periodCode);
        voucher.setVoucherDate(ledgerDate);
        voucher.setSourceType("revenue_confirm");
        voucher.setSourceId(policyId);
        voucher.setSummary("保单收入确认-" + policyNo);

        List<FinVoucherEntry> entries = new ArrayList<>();

        // 借:应收账款
        FinVoucherEntry drEntry = new FinVoucherEntry();
        drEntry.setSubjectId(receivableSubjectId);
        drEntry.setSummary("保单收入确认-" + policyNo);
        drEntry.setDebitAmount(premium);
        drEntry.setCreditAmount(BigDecimal.ZERO);
        entries.add(drEntry);

        // 贷:主营业务收入
        FinVoucherEntry crEntry = new FinVoucherEntry();
        crEntry.setSubjectId(revenueSubjectId);
        crEntry.setSummary("保单收入确认-" + policyNo);
        crEntry.setDebitAmount(BigDecimal.ZERO);
        crEntry.setCreditAmount(premium);
        entries.add(crEntry);

        voucher.setEntries(entries);

        // 7. 保存凭证
        Long voucherId = finVoucherService.saveWithEntries(voucher);

        // 8. 更新保单收入确认状态
        finRevenueMapper.updateRevenueStatus(policyId, "1", voucherId);
    }

    /**
     * 反确认(删除已生成凭证,还原确认状态)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reverseRevenue(Long policyId, String operator)
    {
        // 1. 查询保单
        Map<String, Object> policy = finRevenueMapper.getPolicyForRevenue(policyId);
        if (policy == null)
        {
            throw new ServiceException("保单不存在,ID=" + policyId);
        }

        // 2. 检查是否已确认
        Object confirmedFlag = policy.get("revenue_confirmed");
        if (!"1".equals(String.valueOf(confirmedFlag)))
        {
            throw new ServiceException("该保单未确认收入,无法反确认,保单号=" + policy.get("policy_no"));
        }

        // 3. 获取关联凭证ID
        Object voucherIdObj = policy.get("revenue_voucher_id");
        if (voucherIdObj == null)
        {
            throw new ServiceException("关联凭证不存在,保单号=" + policy.get("policy_no"));
        }
        Long voucherId = Long.valueOf(String.valueOf(voucherIdObj));

        // 4. 删除凭证(仅草稿状态可删)
        finVoucherService.deleteFinVoucherByIds(new Long[]{voucherId});

        // 5. 还原确认状态
        finRevenueMapper.updateRevenueStatus(policyId, "0", null);
    }

    /**
     * 查询收入汇总
     */
    @Override
    public Map<String, Object> summary(String startDate, String endDate)
    {
        return finRevenueMapper.summary(startDate, endDate);
    }
}
