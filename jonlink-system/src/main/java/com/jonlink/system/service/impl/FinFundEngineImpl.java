package com.jonlink.system.service.impl;

import com.jonlink.common.exception.ServiceException;
import com.jonlink.system.domain.FinBankAccount;
import com.jonlink.system.domain.FinCashFlow;
import com.jonlink.system.domain.FinPayment;
import com.jonlink.system.domain.FinReceipt;
import com.jonlink.system.mapper.FinBankAccountMapper;
import com.jonlink.system.mapper.FinCashFlowMapper;
import com.jonlink.system.mapper.FinPaymentMapper;
import com.jonlink.system.mapper.FinReceiptMapper;
import com.jonlink.system.service.IFinFundEngine;
import com.jonlink.system.service.IFinSettlementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资金引擎实现
 * @author jonlink
 */
@Service
public class FinFundEngineImpl implements IFinFundEngine
{
    private static final Logger log = LoggerFactory.getLogger(FinFundEngineImpl.class);

    @Autowired private FinReceiptMapper receiptMapper;
    @Autowired private FinPaymentMapper paymentMapper;
    @Autowired private FinBankAccountMapper accountMapper;
    @Autowired private FinCashFlowMapper flowMapper;
    @Autowired private IFinSettlementService settlementService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FinReceipt confirmReceipt(Long receiptId, String operator)
    {
        FinReceipt r = receiptMapper.selectFinReceiptById(receiptId);
        if (r == null) throw new ServiceException("收款单不存在: " + receiptId);
        if (!"0".equals(r.getStatus())) throw new ServiceException("收款单非未确认状态");
        if (r.getBankAccountId() == null) throw new ServiceException("收款单未指定账户");
        FinBankAccount acc = accountMapper.selectFinBankAccountById(r.getBankAccountId());
        if (acc == null) throw new ServiceException("账户不存在: " + r.getBankAccountId());
        if (!"1".equals(acc.getStatus())) throw new ServiceException("账户已停用");
        if (r.getAmount() == null || r.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw new ServiceException("收款金额必须大于 0");

        BigDecimal oldBal = acc.getCurrentBalance() == null ? BigDecimal.ZERO : acc.getCurrentBalance();
        BigDecimal newBal = oldBal.add(r.getAmount());

        // 乐观锁更新余额
        Map<String, Object> params = new HashMap<>();
        params.put("id", acc.getId());
        params.put("oldBalance", oldBal);
        params.put("newBalance", newBal);
        params.put("updateBy", operator);
        int rows = accountMapper.updateBalanceWithOptimisticLock(params);
        if (rows == 0) {
            throw new ServiceException("账户余额已被其他操作修改，请重试");
        }

        FinCashFlow flow = new FinCashFlow();
        flow.setBankAccountId(acc.getId());
        flow.setDirection("1"); // 1=in
        flow.setAmount(r.getAmount());
        flow.setBalanceAfter(newBal);
        flow.setBizType(r.getBizType() == null ? "RECEIPT" : r.getBizType());
        flow.setBizId(receiptId);
        flow.setFlowTime(new Date());
        flow.setRemark("收款单 " + r.getBillNo() + " 确认");
        flow.setCreateBy(operator);
        flowMapper.insertFinCashFlow(flow);

        r.setStatus("1");
        r.setConfirmTime(new Date());
        r.setUpdateBy(operator);
        receiptMapper.updateFinReceipt(r);

        // 联动:若来源是 receivable 则核销
        if ("receivable".equals(r.getSourceType()) && r.getSourceId() != null) {
            try {
                settlementService.cancelReceivable(r.getSourceId(), r.getAmount(), operator, receiptId);
            } catch (ServiceException e) {
                // 不阻断(收/付已完成),只记日志
                log.warn("核销应收失败: {}", e.getMessage());
            }
        }
        return r;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FinPayment confirmPayment(Long paymentId, String operator)
    {
        FinPayment p = paymentMapper.selectFinPaymentById(paymentId);
        if (p == null) throw new ServiceException("付款单不存在: " + paymentId);
        if (!"0".equals(p.getStatus())) throw new ServiceException("付款单非未确认状态");
        if (p.getBankAccountId() == null) throw new ServiceException("付款单未指定账户");
        FinBankAccount acc = accountMapper.selectFinBankAccountById(p.getBankAccountId());
        if (acc == null) throw new ServiceException("账户不存在: " + p.getBankAccountId());
        if (!"1".equals(acc.getStatus())) throw new ServiceException("账户已停用");
        if (p.getAmount() == null || p.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw new ServiceException("付款金额必须大于 0");
        BigDecimal oldBal = acc.getCurrentBalance() == null ? BigDecimal.ZERO : acc.getCurrentBalance();
        if (p.getAmount().compareTo(oldBal) > 0)
            throw new ServiceException("付款金额(" + p.getAmount() + ")大于账户余额(" + oldBal + ")");

        BigDecimal newBal = oldBal.subtract(p.getAmount());

        // 乐观锁更新余额
        Map<String, Object> params = new HashMap<>();
        params.put("id", acc.getId());
        params.put("oldBalance", oldBal);
        params.put("newBalance", newBal);
        params.put("updateBy", operator);
        int rows = accountMapper.updateBalanceWithOptimisticLock(params);
        if (rows == 0) {
            throw new ServiceException("账户余额已被其他操作修改，请重试");
        }

        FinCashFlow flow = new FinCashFlow();
        flow.setBankAccountId(acc.getId());
        flow.setDirection("0"); // 0=out
        flow.setAmount(p.getAmount());
        flow.setBalanceAfter(newBal);
        flow.setBizType(p.getBizType() == null ? "PAYMENT" : p.getBizType());
        flow.setBizId(paymentId);
        flow.setFlowTime(new Date());
        flow.setRemark("付款单 " + p.getBillNo() + " 确认");
        flow.setCreateBy(operator);
        flowMapper.insertFinCashFlow(flow);

        p.setStatus("1");
        p.setConfirmTime(new Date());
        p.setUpdateBy(operator);
        paymentMapper.updateFinPayment(p);

        if ("payable".equals(p.getSourceType()) && p.getSourceId() != null) {
            try {
                settlementService.cancelPayable(p.getSourceId(), p.getAmount(), operator, paymentId);
            } catch (ServiceException e) {
                log.warn("核销应付失败: {}", e.getMessage());
            }
        }
        return p;
    }

    @Override
    public List<FinBankAccount> listActiveAccounts()
    {
        FinBankAccount q = new FinBankAccount();
        q.setStatus("1");
        return accountMapper.selectFinBankAccountList(q);
    }
}