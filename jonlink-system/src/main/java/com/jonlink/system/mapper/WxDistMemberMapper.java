package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.WxDistMember;

/**
 * 分销员档案Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public interface WxDistMemberMapper 
{
    /**
     * 查询分销员档案
     * 
     * @param id 分销员档案主键
     * @return 分销员档案
     */
    public WxDistMember selectWxDistMemberById(Long id);

    /**
     * 查询分销员档案列表
     * 
     * @param wxDistMember 分销员档案
     * @return 分销员档案集合
     */
    public List<WxDistMember> selectWxDistMemberList(WxDistMember wxDistMember);

    /**
     * 新增分销员档案
     * 
     * @param wxDistMember 分销员档案
     * @return 结果
     */
    public int insertWxDistMember(WxDistMember wxDistMember);

    /**
     * 修改分销员档案
     * 
     * @param wxDistMember 分销员档案
     * @return 结果
     */
    public int updateWxDistMember(WxDistMember wxDistMember);

    /**
     * 删除分销员档案
     * 
     * @param id 分销员档案主键
     * @return 结果
     */
    public int deleteWxDistMemberById(Long id);

    /**
     * 批量删除分销员档案
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWxDistMemberByIds(Long[] ids);
}
