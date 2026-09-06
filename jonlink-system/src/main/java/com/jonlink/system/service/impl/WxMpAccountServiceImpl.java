package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.utils.DateUtils;
import com.jonlink.common.utils.security.AesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.WxMpAccountMapper;
import com.jonlink.system.domain.WxMpAccount;
import com.jonlink.system.service.IWxMpAccountService;

/**
 * 账号配置Service业务层处理
 * - appSecret 入库前 AES 加密(已加密自动跳过),列表/详情查询脱敏(仅后 4 位)
 *
 * @author jonlink
 * @date 2026-08-08
 */
@Service
public class WxMpAccountServiceImpl implements IWxMpAccountService
{
    @Autowired
    private WxMpAccountMapper wxMpAccountMapper;

    @Override
    public WxMpAccount selectWxMpAccountById(Long id)
    {
        WxMpAccount a = wxMpAccountMapper.selectWxMpAccountById(id);
        maskSecret(a);
        return a;
    }

    @Override
    public List<WxMpAccount> selectWxMpAccountList(WxMpAccount wxMpAccount)
    {
        List<WxMpAccount> list = wxMpAccountMapper.selectWxMpAccountList(wxMpAccount);
        if (list != null)
        {
            list.forEach(this::maskSecret);
        }
        return list;
    }

    @Override
    public int insertWxMpAccount(WxMpAccount wxMpAccount)
    {
        wxMpAccount.setCreateTime(DateUtils.getNowDate());
        encryptSecret(wxMpAccount);
        return wxMpAccountMapper.insertWxMpAccount(wxMpAccount);
    }

    @Override
    public int updateWxMpAccount(WxMpAccount wxMpAccount)
    {
        wxMpAccount.setUpdateTime(DateUtils.getNowDate());
        // 编辑时: 前端若保留原值(以 "****xxxx" 形式),视为不覆盖
        if (wxMpAccount.getAppSecret() != null && wxMpAccount.getAppSecret().startsWith("****"))
        {
            wxMpAccount.setAppSecret(null);
        }
        else if (wxMpAccount.getAppSecret() != null && !wxMpAccount.getAppSecret().isEmpty())
        {
            encryptSecret(wxMpAccount);
        }
        return wxMpAccountMapper.updateWxMpAccount(wxMpAccount);
    }

    @Override
    public int deleteWxMpAccountByIds(Long[] ids)
    {
        return wxMpAccountMapper.deleteWxMpAccountByIds(ids);
    }

    @Override
    public int deleteWxMpAccountById(Long id)
    {
        return wxMpAccountMapper.deleteWxMpAccountById(id);
    }

    /** 入库前加密(空值不加密,已带 "AES:" 前缀视为已加密跳过) */
    private void encryptSecret(WxMpAccount a)
    {
        if (a.getAppSecret() == null || a.getAppSecret().isEmpty())
        {
            return;
        }
        a.setAppSecret(AesUtils.encrypt(a.getAppSecret()));
    }

    /** 读出后脱敏(避免明文回显) */
    private void maskSecret(WxMpAccount a)
    {
        if (a == null || a.getAppSecret() == null || a.getAppSecret().isEmpty())
        {
            return;
        }
        try
        {
            a.setAppSecret(AesUtils.mask(a.getAppSecret()));
        }
        catch (Exception e)
        {
            a.setAppSecret("****");
        }
    }
}
