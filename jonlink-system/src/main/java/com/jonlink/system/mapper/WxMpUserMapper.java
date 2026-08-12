package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.WxMpUser;

/**
 * 粉丝管理Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public interface WxMpUserMapper 
{
    /**
     * 查询粉丝管理
     * 
     * @param id 粉丝管理主键
     * @return 粉丝管理
     */
    public WxMpUser selectWxMpUserById(Long id);

    /**
     * 查询粉丝管理列表
     * 
     * @param wxMpUser 粉丝管理
     * @return 粉丝管理集合
     */
    public List<WxMpUser> selectWxMpUserList(WxMpUser wxMpUser);

    /**
     * 新增粉丝管理
     * 
     * @param wxMpUser 粉丝管理
     * @return 结果
     */
    public int insertWxMpUser(WxMpUser wxMpUser);

    /** 粉丝同步 upsert(openid 唯一键冲突更新) */
    public int upsertWxMpUser(WxMpUser wxMpUser);

    /**
     * 修改粉丝管理
     * 
     * @param wxMpUser 粉丝管理
     * @return 结果
     */
    public int updateWxMpUser(WxMpUser wxMpUser);

    /**
     * 删除粉丝管理
     * 
     * @param id 粉丝管理主键
     * @return 结果
     */
    public int deleteWxMpUserById(Long id);

    /**
     * 批量删除粉丝管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWxMpUserByIds(Long[] ids);
}
