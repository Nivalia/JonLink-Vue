package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.WxDistMemberMapper;
import com.jonlink.system.domain.WxDistMember;
import com.jonlink.system.service.IWxDistMemberService;

/**
 * 分销员档案Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@Service
public class WxDistMemberServiceImpl implements IWxDistMemberService 
{
    @Autowired
    private WxDistMemberMapper wxDistMemberMapper;

    /**
     * 查询分销员档案
     * 
     * @param id 分销员档案主键
     * @return 分销员档案
     */
    @Override
    public WxDistMember selectWxDistMemberById(Long id)
    {
        return wxDistMemberMapper.selectWxDistMemberById(id);
    }

    /**
     * 查询分销员档案列表
     * 
     * @param wxDistMember 分销员档案
     * @return 分销员档案
     */
    @Override
    public List<WxDistMember> selectWxDistMemberList(WxDistMember wxDistMember)
    {
        return wxDistMemberMapper.selectWxDistMemberList(wxDistMember);
    }

    /**
     * 新增分销员档案
     * 
     * @param wxDistMember 分销员档案
     * @return 结果
     */
    @Override
    public int insertWxDistMember(WxDistMember wxDistMember)
    {
        wxDistMember.setCreateTime(DateUtils.getNowDate());
        return wxDistMemberMapper.insertWxDistMember(wxDistMember);
    }

    /**
     * 修改分销员档案
     * 
     * @param wxDistMember 分销员档案
     * @return 结果
     */
    @Override
    public int updateWxDistMember(WxDistMember wxDistMember)
    {
        wxDistMember.setUpdateTime(DateUtils.getNowDate());
        return wxDistMemberMapper.updateWxDistMember(wxDistMember);
    }

    /**
     * 批量删除分销员档案
     * 
     * @param ids 需要删除的分销员档案主键
     * @return 结果
     */
    @Override
    public int deleteWxDistMemberByIds(Long[] ids)
    {
        return wxDistMemberMapper.deleteWxDistMemberByIds(ids);
    }

    /**
     * 删除分销员档案信息
     * 
     * @param id 分销员档案主键
     * @return 结果
     */
    @Override
    public int deleteWxDistMemberById(Long id)
    {
        return wxDistMemberMapper.deleteWxDistMemberById(id);
    }
}
