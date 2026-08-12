package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.WxMsgRuleMapper;
import com.jonlink.system.domain.WxMsgRule;
import com.jonlink.system.service.IWxMsgRuleService;

/**
 * 推送规则Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@Service
public class WxMsgRuleServiceImpl implements IWxMsgRuleService 
{
    @Autowired
    private WxMsgRuleMapper wxMsgRuleMapper;

    /**
     * 查询推送规则
     * 
     * @param id 推送规则主键
     * @return 推送规则
     */
    @Override
    public WxMsgRule selectWxMsgRuleById(Long id)
    {
        return wxMsgRuleMapper.selectWxMsgRuleById(id);
    }

    /**
     * 查询推送规则列表
     * 
     * @param wxMsgRule 推送规则
     * @return 推送规则
     */
    @Override
    public List<WxMsgRule> selectWxMsgRuleList(WxMsgRule wxMsgRule)
    {
        return wxMsgRuleMapper.selectWxMsgRuleList(wxMsgRule);
    }

    /**
     * 新增推送规则
     * 
     * @param wxMsgRule 推送规则
     * @return 结果
     */
    @Override
    public int insertWxMsgRule(WxMsgRule wxMsgRule)
    {
        wxMsgRule.setCreateTime(DateUtils.getNowDate());
        return wxMsgRuleMapper.insertWxMsgRule(wxMsgRule);
    }

    /**
     * 修改推送规则
     * 
     * @param wxMsgRule 推送规则
     * @return 结果
     */
    @Override
    public int updateWxMsgRule(WxMsgRule wxMsgRule)
    {
        wxMsgRule.setUpdateTime(DateUtils.getNowDate());
        return wxMsgRuleMapper.updateWxMsgRule(wxMsgRule);
    }

    /**
     * 批量删除推送规则
     * 
     * @param ids 需要删除的推送规则主键
     * @return 结果
     */
    @Override
    public int deleteWxMsgRuleByIds(Long[] ids)
    {
        return wxMsgRuleMapper.deleteWxMsgRuleByIds(ids);
    }

    /**
     * 删除推送规则信息
     * 
     * @param id 推送规则主键
     * @return 结果
     */
    @Override
    public int deleteWxMsgRuleById(Long id)
    {
        return wxMsgRuleMapper.deleteWxMsgRuleById(id);
    }
}
