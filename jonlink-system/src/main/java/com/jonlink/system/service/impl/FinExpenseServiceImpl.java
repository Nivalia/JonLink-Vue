package com.jonlink.system.service.impl;

import java.util.List;
import java.util.Map;
import com.jonlink.common.exception.ServiceException;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.FinExpenseItemMapper;
import com.jonlink.system.mapper.FinExpenseMapper;
import com.jonlink.system.domain.FinExpense;
import com.jonlink.system.domain.FinExpenseItem;
import com.jonlink.system.service.IFinExpenseService;

/**
 * 费用报销单Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@Service
public class FinExpenseServiceImpl implements IFinExpenseService
{
    @Autowired
    private FinExpenseMapper finExpenseMapper;
    @Autowired
    private FinExpenseItemMapper itemMapper;

    /**
     * 查询费用报销单
     * 
     * @param id 费用报销单主键
     * @return 费用报销单
     */
    @Override
    public FinExpense selectFinExpenseById(Long id)
    {
        return finExpenseMapper.selectFinExpenseById(id);
    }

    /**
     * 查询费用报销单列表
     * 
     * @param finExpense 费用报销单
     * @return 费用报销单
     */
    @Override
    public List<FinExpense> selectFinExpenseList(FinExpense finExpense)
    {
        return finExpenseMapper.selectFinExpenseList(finExpense);
    }

    /**
     * 新增费用报销单
     * 
     * @param finExpense 费用报销单
     * @return 结果
     */
    @Override
    public int insertFinExpense(FinExpense finExpense)
    {
        finExpense.setCreateTime(DateUtils.getNowDate());
        return finExpenseMapper.insertFinExpense(finExpense);
    }

    /**
     * 修改费用报销单
     * 
     * @param finExpense 费用报销单
     * @return 结果
     */
    @Override
    public int updateFinExpense(FinExpense finExpense)
    {
        finExpense.setUpdateTime(DateUtils.getNowDate());
        return finExpenseMapper.updateFinExpense(finExpense);
    }

    /**
     * 批量删除费用报销单
     * 
     * @param ids 需要删除的费用报销单主键
     * @return 结果
     */
    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public int deleteFinExpenseByIds(Long[] ids)
    {
        for (Long id : ids) {
            FinExpense e = finExpenseMapper.selectFinExpenseById(id);
            if (e != null && !"0".equals(e.getStatus())) {
                throw new ServiceException("仅草稿状态可删除，报销单 [" + e.getExpenseNo() + "] 当前状态=" + e.getStatus());
            }
            itemMapper.deleteByExpenseId(id);
        }
        return finExpenseMapper.deleteFinExpenseByIds(ids);
    }

    /**
     * 删除费用报销单信息
     * 
     * @param id 费用报销单主键
     * @return 结果
     */
    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public int deleteFinExpenseById(Long id)
    {
        FinExpense e = finExpenseMapper.selectFinExpenseById(id);
        if (e != null && !"0".equals(e.getStatus())) {
            throw new ServiceException("仅草稿状态可删除，报销单 [" + e.getExpenseNo() + "] 当前状态=" + e.getStatus());
        }
        itemMapper.deleteByExpenseId(id);
        return finExpenseMapper.deleteFinExpenseById(id);
    }

    /**
     * 审批费用报销单
     * @param id 报销单ID
     * @param action approve=通过, reject=驳回
     * @param operator 操作人
     */
    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public void approve(Long id, String action, String operator)
    {
        FinExpense e = finExpenseMapper.selectFinExpenseById(id);
        if (e == null) throw new ServiceException("报销单不存在");
        if (!"0".equals(e.getStatus())) {
            throw new ServiceException("仅草稿状态可审批，当前状态=" + e.getStatus());
        }
        FinExpense upd = new FinExpense();
        upd.setId(id);
        if ("approve".equals(action)) {
            upd.setStatus("2"); // 已审核
            upd.setApprover(operator);
        } else if ("reject".equals(action)) {
            upd.setStatus("4"); // 驳回
            upd.setApprover(operator);
        } else {
            throw new ServiceException("无效的操作: " + action);
        }
        upd.setUpdateBy(operator);
        upd.setUpdateTime(DateUtils.getNowDate());
        finExpenseMapper.updateFinExpense(upd);
    }

    @Override
    public Long createWithItems(String applicant, String deptName, String expenseType,
                                java.math.BigDecimal totalAmount,
                                List<java.util.Map<String, Object>> items,
                                String operator)
    {
        if (applicant == null || applicant.trim().isEmpty()) throw new ServiceException("申请人不能为空");
        if (totalAmount == null || totalAmount.compareTo(java.math.BigDecimal.ZERO) <= 0) throw new ServiceException("金额必须大于 0");
        if (items == null || items.isEmpty()) throw new ServiceException("至少需要一条明细");

        FinExpense e = new FinExpense();
        e.setExpenseNo("EX-" + new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date())
            + "-" + String.format("%06d", (long)(System.nanoTime() % 1000000)));
        e.setApplicant(applicant);
        e.setDeptName(deptName);
        e.setExpenseType(expenseType == null ? "0" : expenseType);
        e.setTotalAmount(totalAmount);
        e.setStatus("0");
        e.setCreateBy(operator);
        finExpenseMapper.insertFinExpense(e);

        int sort = 1;
        for (Map<String, Object> it : items) {
            FinExpenseItem item = new FinExpenseItem();
            item.setExpenseId(e.getId());
            item.setItemName((String) it.getOrDefault("itemName", ""));
            Object amt = it.get("amount");
            item.setAmount(amt == null ? java.math.BigDecimal.ZERO : new java.math.BigDecimal(amt.toString()));
            item.setInvoiceId(it.get("invoiceId") == null ? null : Long.valueOf(it.get("invoiceId").toString()));
            item.setRemark((String) it.get("remark"));
            item.setSortOrder((long) (sort++));
            itemMapper.insertFinExpenseItem(item);
        }
        return e.getId();
    }
}
