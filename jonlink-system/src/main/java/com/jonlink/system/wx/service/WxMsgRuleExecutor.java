package com.jonlink.system.wx.service;

import java.util.HashMap;
import java.util.Map;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.jonlink.common.utils.StringUtils;
import com.jonlink.system.domain.WxMsgRule;
import com.jonlink.system.mapper.WxMsgRuleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 推送规则执行器 (R17)：业务事件 → 按 rule_code 查 wx_msg_rule → 组装模板内容 → 找受众 → 推送。
 * 规则表可配：停用即停发、可换模板、可改文案，代码不写死任何消息。
 *
 * @author jonlink
 */
@Service
public class WxMsgRuleExecutor
{
    private static final Logger log = LoggerFactory.getLogger(WxMsgRuleExecutor.class);

    /** 规则编码 */
    public static final String RULE_SETTLE_UP = "SETTLE_UP";
    public static final String RULE_SETTLE_DOWN = "SETTLE_DOWN";
    public static final String RULE_DIST_COMMISSION = "DIST_COMMISSION";
    public static final String RULE_ORDER_VERIFY = "ORDER_VERIFY";

    @Autowired
    private WxMsgRuleMapper wxMsgRuleMapper;
    @Autowired
    private com.jonlink.system.mapper.WxMpTemplateMapper wxMpTemplateMapper;
    @Autowired
    private WxMsgPushService wxMsgPushService;
    @Autowired
    private TicketService ticketService;

    /**
     * 执行推送规则。
     *
     * @param ruleCode 规则编码
     * @param phone 目标手机号(受众规则优先, 无则用此)
     * @param params 内容参数(content_rule 占位符映射: {保单号}->value)
     * @param bizNo 幂等业务单号
     */
    public boolean execute(String ruleCode, String phone, Map<String, Object> params, String bizNo)
    {
        WxMsgRule rule = selectByCode(ruleCode);
        if (rule == null)
        {
            log.warn("[rule] 规则不存在: {}", ruleCode);
            return false;
        }
        if (!"1".equals(rule.getEnabled()))
        {
            log.info("[rule] 规则已停用: {}", ruleCode);
            return false;
        }
        try
        {
            // 1. 受众 (audience_rule JSON: {"source":"channel_phone"} 或 fans_openid)
            String targetPhone = phone;
            String targetOpenid = null;
            JSONObject audience = parseJson(rule.getAudienceRule());
            if (audience != null)
            {
                String source = audience.getString("source");
                if ("fans_openid".equals(source))
                {
                    targetOpenid = audience.getString("openid");
                }
                else if (StringUtils.isNotEmpty(audience.getString("phone")))
                {
                    targetPhone = audience.getString("phone");
                }
            }
            if (StringUtils.isEmpty(targetPhone) && StringUtils.isEmpty(targetOpenid))
            {
                log.warn("[rule] 无受众, 跳过: {}", ruleCode);
                return false;
            }
            // 2. 组装内容 (content_rule: {"keyword1":"{保单号}",...})
            Map<String, Object> data = new HashMap<>();
            JSONObject content = parseJson(rule.getContentRule());
            if (content != null)
            {
                for (String k : content.keySet())
                {
                    String template = content.getString(k);
                    data.put(k, replacePlaceholder(template, params));
                }
            }
            // 3. url (含{ticket}占位——防伪参数, 推送时生成绑定 openid 的 24h 凭证)
            String url = rule.getUrlRule();
            if (StringUtils.isNotEmpty(url))
            {
                url = replacePlaceholder(url, params);
                if (url.contains("{ticket}"))
                {
                    String openidForTicket = targetOpenid;
                    if (StringUtils.isEmpty(openidForTicket))
                    {
                        com.jonlink.system.domain.WxMpUser u = wxMsgPushService.findUserByPhone(targetPhone);
                        if (u != null)
                        {
                            openidForTicket = u.getOpenid();
                        }
                    }
                    String ticket = ticketService.create(openidForTicket);
                    if (StringUtils.isNotEmpty(ticket))
                    {
                        url = url.replace("{ticket}", ticket);
                    }
                    else
                    {
                        log.warn("[rule] ticket 生成失败(无 openid), url 保留占位: rule={}", ruleCode);
                    }
                }
            }
            // 4. 推送 (rule.templateId 为 wx_mp_template 主键, 转微信模板ID)
            String wxTemplateId = null;
            com.jonlink.system.domain.WxMpTemplate tpl = rule.getTemplateId() == null ? null
                    : wxMpTemplateMapper.selectWxMpTemplateById(rule.getTemplateId());
            if (tpl != null)
            {
                wxTemplateId = tpl.getTemplateId();
            }
            if (wxTemplateId == null)
            {
                log.warn("[rule] 规则关联模板不存在或为空: rule={} tplId={}", ruleCode, rule.getTemplateId());
                return false;
            }
            Map<String, Object> r;
            if (StringUtils.isNotEmpty(targetOpenid))
            {
                r = wxMsgPushService.sendByOpenid(targetOpenid, targetPhone, wxTemplateId,
                        data, url, bizNo, rule.getBizType());
            }
            else
            {
                r = wxMsgPushService.sendByPhone(targetPhone, wxTemplateId, data, url, bizNo, rule.getBizType());
            }
            return Boolean.TRUE.equals(r.get("ok"));
        }
        catch (Exception e)
        {
            log.error("[rule] 执行异常 rule={}: {}", ruleCode, e.getMessage());
            return false;
        }
    }

    /** 按 rule_code 查规则(本地缓存可后续优化) */
    private WxMsgRule selectByCode(String ruleCode)
    {
        WxMsgRule q = new WxMsgRule();
        q.setRuleCode(ruleCode);
        java.util.List<WxMsgRule> list = wxMsgRuleMapper.selectWxMsgRuleList(q);
        return list.isEmpty() ? null : list.get(0);
    }

    private JSONObject parseJson(String s)
    {
        if (StringUtils.isEmpty(s))
        {
            return null;
        }
        try
        {
            return JSON.parseObject(s);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /** 占位符替换 {xxx} -> params 值; 防伪 {ticket} 保留给前端 */
    private String replacePlaceholder(String template, Map<String, Object> params)
    {
        if (StringUtils.isEmpty(template))
        {
            return template;
        }
        String out = template;
        if (params != null)
        {
            for (Map.Entry<String, Object> e : params.entrySet())
            {
                out = out.replace("{" + e.getKey() + "}", String.valueOf(e.getValue()));
            }
        }
        return out;
    }
}
