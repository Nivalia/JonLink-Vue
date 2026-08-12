package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.WxMpAccountMapper;
import com.jonlink.system.domain.WxMpAccount;
import com.jonlink.system.service.IWxMpAccountService;

/**
 * 账号配置Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@Service
public class WxMpAccountServiceImpl implements IWxMpAccountService 
{
    @Autowired
    private WxMpAccountMapper wxMpAccountMapper;

    /**
     * 查询账号配置
     * 
     * @param id 账号配置主键
     * @return 账号配置
     */
    @Override
    public WxMpAccount selectWxMpAccountById(Long id)
    {
        return wxMpAccountMapper.selectWxMpAccountById(id);
    }

    /**
     * 查询账号配置列表
     * 
     * @param wxMpAccount 账号配置
     * @return 账号配置
     */
    @Override
    public List<WxMpAccount> selectWxMpAccountList(WxMpAccount wxMpAccount)
    {
        return wxMpAccountMapper.selectWxMpAccountList(wxMpAccount);
    }

    /**
     * 新增账号配置
     * 
     * @param wxMpAccount 账号配置
     * @return 结果
     */
    @Override
    public int insertWxMpAccount(WxMpAccount wxMpAccount)
    {
        wxMpAccount.setCreateTime(DateUtils.getNowDate());
        return wxMpAccountMapper.insertWxMpAccount(wxMpAccount);
    }

    /**
     * 修改账号配置
     * 
     * @param wxMpAccount 账号配置
     * @return 结果
     */
    @Override
    public int updateWxMpAccount(WxMpAccount wxMpAccount)
    {
        wxMpAccount.setUpdateTime(DateUtils.getNowDate());
        return wxMpAccountMapper.updateWxMpAccount(wxMpAccount);
    }

    /**
     * 批量删除账号配置
     * 
     * @param ids 需要删除的账号配置主键
     * @return 结果
     */
    @Override
    public int deleteWxMpAccountByIds(Long[] ids)
    {
        return wxMpAccountMapper.deleteWxMpAccountByIds(ids);
    }

    /**
     * 删除账号配置信息
     * 
     * @param id 账号配置主键
     * @return 结果
     */
    @Override
    public int deleteWxMpAccountById(Long id)
    {
        return wxMpAccountMapper.deleteWxMpAccountById(id);
    }
}
