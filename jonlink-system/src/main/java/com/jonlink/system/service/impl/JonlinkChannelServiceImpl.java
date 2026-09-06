package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.exception.ServiceException;
import com.jonlink.common.utils.DateUtils;
import com.jonlink.common.utils.SecurityUtils;
import com.jonlink.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.domain.JonlinkContactPerson;
import com.jonlink.system.mapper.JonlinkContactPersonMapper;
import com.jonlink.system.mapper.JonlinkChannelMapper;
import com.jonlink.system.domain.JonlinkChannel;
import com.jonlink.system.service.IJonlinkChannelService;

/**
 * 上游渠道商Service业务层处理
 *
 * @author jonlink
 * @date 2026-08-13
 */
@Service
public class JonlinkChannelServiceImpl implements IJonlinkChannelService
{
    @Autowired
    private JonlinkChannelMapper jonlinkChannelMapper;

    @Autowired
    private JonlinkContactPersonMapper jonlinkContactPersonMapper;

    @Override
    public JonlinkChannel selectJonlinkChannelById(Long id)
    {
        return jonlinkChannelMapper.selectJonlinkChannelById(id);
    }

    @Override
    public List<JonlinkChannel> selectJonlinkChannelList(JonlinkChannel jonlinkChannel)
    {
        return jonlinkChannelMapper.selectJonlinkChannelList(jonlinkChannel);
    }

    @Override
    public List<JonlinkChannel> selectJonlinkChannelOptions()
    {
        return jonlinkChannelMapper.selectJonlinkChannelOptions();
    }

    @Override
    public int insertJonlinkChannel(JonlinkChannel jonlinkChannel)
    {
        if (!checkChannelNameUnique(jonlinkChannel))
        {
            throw new ServiceException("渠道商名称已存在");
        }
        String username = currentUsername();
        jonlinkChannel.setCreateBy(username);
        jonlinkChannel.setCreateTime(DateUtils.getNowDate());
        jonlinkChannel.setUpdateBy(username);
        jonlinkChannel.setUpdateTime(DateUtils.getNowDate());
        if (StringUtils.isEmpty(jonlinkChannel.getStatus()))
        {
            jonlinkChannel.setStatus("1");
        }
        // 联系人联动:若选了 contactId,自动从联系人表带出姓名/电话/微信冗余字段
        if (jonlinkChannel.getContactId() != null)
        {
            JonlinkContactPerson cp = jonlinkContactPersonMapper.selectJonlinkContactPersonById(jonlinkChannel.getContactId());
            if (cp != null)
            {
                jonlinkChannel.setContactPerson(cp.getContactName());
                jonlinkChannel.setPhone(cp.getPhone());
                jonlinkChannel.setWechat(cp.getWechat());
            }
        }
        return jonlinkChannelMapper.insertJonlinkChannel(jonlinkChannel);
    }

    @Override
    public int updateJonlinkChannel(JonlinkChannel jonlinkChannel)
    {
        if (!checkChannelNameUnique(jonlinkChannel))
        {
            throw new ServiceException("渠道商名称已存在");
        }
        jonlinkChannel.setUpdateBy(currentUsername());
        jonlinkChannel.setUpdateTime(DateUtils.getNowDate());
        if (jonlinkChannel.getContactId() != null)
        {
            JonlinkContactPerson cp = jonlinkContactPersonMapper.selectJonlinkContactPersonById(jonlinkChannel.getContactId());
            if (cp != null)
            {
                jonlinkChannel.setContactPerson(cp.getContactName());
                jonlinkChannel.setPhone(cp.getPhone());
                jonlinkChannel.setWechat(cp.getWechat());
            }
        }
        return jonlinkChannelMapper.updateJonlinkChannel(jonlinkChannel);
    }

    @Override
    public int deleteJonlinkChannelByIds(Long[] ids)
    {
        return jonlinkChannelMapper.deleteJonlinkChannelByIds(ids);
    }

    @Override
    public int deleteJonlinkChannelById(Long id)
    {
        return jonlinkChannelMapper.deleteJonlinkChannelById(id);
    }

    @Override
    public boolean checkChannelNameUnique(JonlinkChannel jonlinkChannel)
    {
        if (StringUtils.isEmpty(jonlinkChannel.getChannelName()))
        {
            return true;
        }
        return jonlinkChannelMapper.checkChannelNameUnique(jonlinkChannel) == 0;
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
