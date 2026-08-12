package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.WxMpUserMapper;
import com.jonlink.system.domain.WxMpUser;
import com.jonlink.system.service.IWxMpUserService;

/**
 * 粉丝管理Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@Service
public class WxMpUserServiceImpl implements IWxMpUserService 
{
    @Autowired
    private WxMpUserMapper wxMpUserMapper;

    /**
     * 查询粉丝管理
     * 
     * @param id 粉丝管理主键
     * @return 粉丝管理
     */
    @Override
    public WxMpUser selectWxMpUserById(Long id)
    {
        return wxMpUserMapper.selectWxMpUserById(id);
    }

    /**
     * 查询粉丝管理列表
     * 
     * @param wxMpUser 粉丝管理
     * @return 粉丝管理
     */
    @Override
    public List<WxMpUser> selectWxMpUserList(WxMpUser wxMpUser)
    {
        return wxMpUserMapper.selectWxMpUserList(wxMpUser);
    }

    /**
     * 新增粉丝管理
     * 
     * @param wxMpUser 粉丝管理
     * @return 结果
     */
    @Override
    public int insertWxMpUser(WxMpUser wxMpUser)
    {
        wxMpUser.setCreateTime(DateUtils.getNowDate());
        return wxMpUserMapper.insertWxMpUser(wxMpUser);
    }

    /**
     * 修改粉丝管理
     * 
     * @param wxMpUser 粉丝管理
     * @return 结果
     */
    @Override
    public int updateWxMpUser(WxMpUser wxMpUser)
    {
        wxMpUser.setUpdateTime(DateUtils.getNowDate());
        return wxMpUserMapper.updateWxMpUser(wxMpUser);
    }

    /**
     * 批量删除粉丝管理
     * 
     * @param ids 需要删除的粉丝管理主键
     * @return 结果
     */
    @Override
    public int deleteWxMpUserByIds(Long[] ids)
    {
        return wxMpUserMapper.deleteWxMpUserByIds(ids);
    }

    /**
     * 删除粉丝管理信息
     * 
     * @param id 粉丝管理主键
     * @return 结果
     */
    @Override
    public int deleteWxMpUserById(Long id)
    {
        return wxMpUserMapper.deleteWxMpUserById(id);
    }
}
