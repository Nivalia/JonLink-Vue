package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.FinBankAccountMapper;
import com.jonlink.system.domain.FinBankAccount;
import com.jonlink.system.service.IFinBankAccountService;

/**
 * 银行账户Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@Service
public class FinBankAccountServiceImpl implements IFinBankAccountService 
{
    @Autowired
    private FinBankAccountMapper finBankAccountMapper;

    /**
     * 查询银行账户
     * 
     * @param id 银行账户主键
     * @return 银行账户
     */
    @Override
    public FinBankAccount selectFinBankAccountById(Long id)
    {
        return finBankAccountMapper.selectFinBankAccountById(id);
    }

    /**
     * 查询银行账户列表
     * 
     * @param finBankAccount 银行账户
     * @return 银行账户
     */
    @Override
    public List<FinBankAccount> selectFinBankAccountList(FinBankAccount finBankAccount)
    {
        return finBankAccountMapper.selectFinBankAccountList(finBankAccount);
    }

    /**
     * 新增银行账户
     * 
     * @param finBankAccount 银行账户
     * @return 结果
     */
    @Override
    public int insertFinBankAccount(FinBankAccount finBankAccount)
    {
        finBankAccount.setCreateTime(DateUtils.getNowDate());
        return finBankAccountMapper.insertFinBankAccount(finBankAccount);
    }

    /**
     * 修改银行账户
     * 
     * @param finBankAccount 银行账户
     * @return 结果
     */
    @Override
    public int updateFinBankAccount(FinBankAccount finBankAccount)
    {
        finBankAccount.setUpdateTime(DateUtils.getNowDate());
        return finBankAccountMapper.updateFinBankAccount(finBankAccount);
    }

    /**
     * 批量删除银行账户
     * 
     * @param ids 需要删除的银行账户主键
     * @return 结果
     */
    @Override
    public int deleteFinBankAccountByIds(Long[] ids)
    {
        return finBankAccountMapper.deleteFinBankAccountByIds(ids);
    }

    /**
     * 删除银行账户信息
     * 
     * @param id 银行账户主键
     * @return 结果
     */
    @Override
    public int deleteFinBankAccountById(Long id)
    {
        return finBankAccountMapper.deleteFinBankAccountById(id);
    }
}
