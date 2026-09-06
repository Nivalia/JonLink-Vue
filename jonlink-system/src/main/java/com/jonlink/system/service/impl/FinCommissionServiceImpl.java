package com.jonlink.system.service.impl;

import com.jonlink.common.exception.ServiceException;
import com.jonlink.system.domain.FinCommission;
import com.jonlink.system.mapper.FinCommissionMapper;
import com.jonlink.system.service.IFinCommissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 佣金结算服务实现
 */
@Service
public class FinCommissionServiceImpl implements IFinCommissionService
{
    @Autowired private FinCommissionMapper commissionMapper;

    @Override
    public List<FinCommission> list(FinCommission query) {
        return commissionMapper.selectFinCommissionList(query);
    }

    @Override
    public FinCommission getById(Long id) {
        return commissionMapper.selectFinCommissionById(id);
    }

    @Override
    public BigDecimal calculateCommission(BigDecimal premium, BigDecimal rate, String taxFlag) {
        if (premium == null || rate == null) return BigDecimal.ZERO;
        // v4规则: 含税时基数 = 保费/1.06
        BigDecimal base = premium;
        if ("1".equals(taxFlag)) {
            base = premium.divide(new BigDecimal("1.06"), 4, RoundingMode.HALF_UP);
        }
        return base.multiply(rate).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchCreate(List<FinCommission> list, String operator) {
        int count = 0;
        for (FinCommission c : list) {
            // 自动计算佣金金额(v4: 按税开关决定基数)
            if (c.getCommissionAmount() == null || c.getCommissionAmount().compareTo(BigDecimal.ZERO) == 0) {
                c.setCommissionAmount(calculateCommission(c.getPremium(), c.getCommissionRate(), c.getTaxFlag()));
            }
            c.setStatus("0"); // 待结算
            c.setCreateBy(operator);
            commissionMapper.insertFinCommission(c);
            count++;
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirm(Long id, String operator) {
        FinCommission c = commissionMapper.selectFinCommissionById(id);
        if (c == null) throw new ServiceException("佣金记录不存在");
        if (!"0".equals(c.getStatus())) throw new ServiceException("仅待结算状态可确认");

        c.setStatus("1"); // 已确认
        c.setSettleDate(new Date());
        c.setUpdateBy(operator);
        commissionMapper.updateFinCommission(c);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markPaid(Long id, Long receiptOrPaymentId, String operator) {
        FinCommission c = commissionMapper.selectFinCommissionById(id);
        if (c == null) throw new ServiceException("佣金记录不存在");
        if (!"1".equals(c.getStatus())) throw new ServiceException("仅已确认状态可标记已支付");

        c.setStatus("2"); // 已支付
        if ("0".equals(c.getDirection())) {
            c.setReceiptId(receiptOrPaymentId); // 上游佣金=收款
        } else {
            c.setPaymentId(receiptOrPaymentId); // 下游佣金=付款
        }
        c.setUpdateBy(operator);
        commissionMapper.updateFinCommission(c);
    }

    @Override
    public Map<String, Object> summary(String startDate, String endDate, String direction) {
        return commissionMapper.summary(startDate, endDate, direction);
    }
}
