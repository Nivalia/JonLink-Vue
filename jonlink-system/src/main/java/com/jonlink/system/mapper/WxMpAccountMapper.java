package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.WxMpAccount;

/**
 * 账号配置Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public interface WxMpAccountMapper 
{
    /**
     * 查询账号配置
     * 
     * @param id 账号配置主键
     * @return 账号配置
     */
    public WxMpAccount selectWxMpAccountById(Long id);

    /**
     * 查询账号配置列表
     * 
     * @param wxMpAccount 账号配置
     * @return 账号配置集合
     */
    public List<WxMpAccount> selectWxMpAccountList(WxMpAccount wxMpAccount);

    /**
     * 新增账号配置
     * 
     * @param wxMpAccount 账号配置
     * @return 结果
     */
    public int insertWxMpAccount(WxMpAccount wxMpAccount);

    /**
     * 修改账号配置
     * 
     * @param wxMpAccount 账号配置
     * @return 结果
     */
    public int updateWxMpAccount(WxMpAccount wxMpAccount);

    /**
     * 删除账号配置
     * 
     * @param id 账号配置主键
     * @return 结果
     */
    public int deleteWxMpAccountById(Long id);

    /**
     * 批量删除账号配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWxMpAccountByIds(Long[] ids);
}
