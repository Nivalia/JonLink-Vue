package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.WxMsgRule;

/**
 * 推送规则Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public interface WxMsgRuleMapper 
{
    /**
     * 查询推送规则
     * 
     * @param id 推送规则主键
     * @return 推送规则
     */
    public WxMsgRule selectWxMsgRuleById(Long id);

    /**
     * 查询推送规则列表
     * 
     * @param wxMsgRule 推送规则
     * @return 推送规则集合
     */
    public List<WxMsgRule> selectWxMsgRuleList(WxMsgRule wxMsgRule);

    /**
     * 新增推送规则
     * 
     * @param wxMsgRule 推送规则
     * @return 结果
     */
    public int insertWxMsgRule(WxMsgRule wxMsgRule);

    /**
     * 修改推送规则
     * 
     * @param wxMsgRule 推送规则
     * @return 结果
     */
    public int updateWxMsgRule(WxMsgRule wxMsgRule);

    /**
     * 删除推送规则
     * 
     * @param id 推送规则主键
     * @return 结果
     */
    public int deleteWxMsgRuleById(Long id);

    /**
     * 批量删除推送规则
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWxMsgRuleByIds(Long[] ids);
}
