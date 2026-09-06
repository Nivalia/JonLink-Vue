package com.jonlink.system.service;

import java.util.List;
import com.jonlink.system.domain.FinBankAccount;

/**
 * 银行账户Service接口
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public interface IFinBankAccountService 
{
    /**
     * 查询银行账户
     * 
     * @param id 银行账户主键
     * @return 银行账户
     */
    public FinBankAccount selectFinBankAccountById(Long id);

    /**
     * 查询银行账户列表
     * 
     * @param finBankAccount 银行账户
     * @return 银行账户集合
     */
    public List<FinBankAccount> selectFinBankAccountList(FinBankAccount finBankAccount);

    /**
     * 新增银行账户
     * 
     * @param finBankAccount 银行账户
     * @return 结果
     */
    public int insertFinBankAccount(FinBankAccount finBankAccount);

    /**
     * 修改银行账户
     * 
     * @param finBankAccount 银行账户
     * @return 结果
     */
    public int updateFinBankAccount(FinBankAccount finBankAccount);

    /**
     * 批量删除银行账户
     * 
     * @param ids 需要删除的银行账户主键集合
     * @return 结果
     */
    public int deleteFinBankAccountByIds(Long[] ids);

    /**
     * 删除银行账户信息
     * 
     * @param id 银行账户主键
     * @return 结果
     */
    public int deleteFinBankAccountById(Long id);
}
