package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.utils.DateUtils;
import com.jonlink.common.utils.SecurityUtils;
import com.jonlink.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.JonlinkChannelUserMapper;
import com.jonlink.system.domain.JonlinkChannelUser;
import com.jonlink.system.service.IJonlinkChannelUserService;

/**
 * 渠道/业务员档案Service业务层处理
 *
 * @author jonlink
 * @date 2026-08-13
 */
@Service
public class JonlinkChannelUserServiceImpl implements IJonlinkChannelUserService
{
    @Autowired
    private JonlinkChannelUserMapper jonlinkChannelUserMapper;

    @Override
    public JonlinkChannelUser selectJonlinkChannelUserById(Long id)
    {
        return jonlinkChannelUserMapper.selectJonlinkChannelUserById(id);
    }

    @Override
    public List<JonlinkChannelUser> selectJonlinkChannelUserList(JonlinkChannelUser jonlinkChannelUser)
    {
        return jonlinkChannelUserMapper.selectJonlinkChannelUserList(jonlinkChannelUser);
    }

    @Override
    public List<JonlinkChannelUser> selectJonlinkChannelUserOptions()
    {
        return jonlinkChannelUserMapper.selectJonlinkChannelUserOptions();
    }

    @Override
    public int insertJonlinkChannelUser(JonlinkChannelUser jonlinkChannelUser)
    {
        String username = currentUsername();
        jonlinkChannelUser.setCreateBy(username);
        jonlinkChannelUser.setCreateTime(DateUtils.getNowDate());
        jonlinkChannelUser.setUpdateBy(username);
        jonlinkChannelUser.setUpdateTime(DateUtils.getNowDate());
        if (StringUtils.isEmpty(jonlinkChannelUser.getStatus()))
        {
            jonlinkChannelUser.setStatus("1");
        }
        if (StringUtils.isEmpty(jonlinkChannelUser.getUserType()))
        {
            jonlinkChannelUser.setUserType("1");
        }
        return jonlinkChannelUserMapper.insertJonlinkChannelUser(jonlinkChannelUser);
    }

    @Override
    public int updateJonlinkChannelUser(JonlinkChannelUser jonlinkChannelUser)
    {
        jonlinkChannelUser.setUpdateBy(currentUsername());
        jonlinkChannelUser.setUpdateTime(DateUtils.getNowDate());
        return jonlinkChannelUserMapper.updateJonlinkChannelUser(jonlinkChannelUser);
    }

    @Override
    public int deleteJonlinkChannelUserByIds(Long[] ids)
    {
        return jonlinkChannelUserMapper.deleteJonlinkChannelUserByIds(ids);
    }

    @Override
    public int deleteJonlinkChannelUserById(Long id)
    {
        return jonlinkChannelUserMapper.deleteJonlinkChannelUserById(id);
    }

    private String currentUsername()
    {
        try
        {
            String u = SecurityUtils.getUsername();
            return StringUtils.isNotEmpty(u) ? u : "system";
        }
        catch (Exception e)
        {
            return "system";
        }
    }
}
