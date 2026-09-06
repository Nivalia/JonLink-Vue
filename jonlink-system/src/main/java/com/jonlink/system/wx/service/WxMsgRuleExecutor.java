package com.jonlink.system.wx.service;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
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
 * 推送规则执行器：台账事件 → 规则匹配 → 受众(业务员+上级) → 内容组装 → 推送。
 *
 * 受众规则：
 *   - dist           → 业务员 + 上级分销员（如果存在）
 *   - phone          → 指定手机号
 *   - openid         → 指定 openid
 *   - all            → 全部粉丝
 *
 * @author jonlink
 */
@Service
public class WxMsgRuleExecutor
{
    private static final Logger log = LoggerFactory.getLogger(WxMsgRuleExecutor.class);

    // 规则编码常量
    public static final String RULE_POLICY_CREATED = "POLICY_CREATED";
    public static final String RULE_DOWN_SETTLED = "DOWN_SETTLED";
    public static final String RULE_UP_SETTLED = "UP_SETTLED";
    public static final String RULE_COMMISSION_PAID = "COMMISSION_PAID";
    public static final String RULE_SETTLE_UP = "UP_SETTLED";
    public static final String RULE_SETTLE_DOWN = "DOWN_SETTLED";
    public static final String RULE_ORDER_VERIFY = "POLICY_CREATED";

    @Autowired
    private WxMsgRuleMapper wxMsgRuleMapper;
    @Autowired
    private com.jonlink.system.mapper.WxMpTemplateMapper wxMpTemplateMapper;
    @Autowired
    private WxMsgPushService wxMsgPushService;
    @Autowired
    private DistFanSyncService distFanSyncService;
    @Autowired
    private TicketService ticketService;

    /**
     * 执行推送规则（台账触发）。
     *
     * @param ruleCode  规则编码
     * @param ledger    台账数据 (Map: policy_no, applicant, premium, channel_ref 等)
     * @param bizNo     幂等业务单号
     * @return 是否成功
     */
    public boolean executeByLedger(String ruleCode, Map<String, Object> ledger, String bizNo)
    {
        WxMsgRule rule = selectByCode(ruleCode);
        if (rule == null)
        {
            log.warn("[rule] 规则不存在: {}", ruleCode);
            return false;
        }
        // 兼容多种启用标记: 1/Y/true 都算启用 (RuoYi 模板默认 '1', 业务手工录入可能 'Y')
        String en = rule.getEnabled();
        boolean enabled = "1".equals(en) || "Y".equalsIgnoreCase(en) || "true".equalsIgnoreCase(en);
        if (!enabled)
        {
            log.info("[rule] 规则已停用: {}", ruleCode);
            return false;
        }

        try
        {
            // 1. 解析受众规则
            JSONObject audience = parseJson(rule.getAudienceRule());
            String audienceType = audience != null ? audience.getString("type") : "dist";

            // 2. 获取受众 openid 列表
            List<String> targetOpenids = resolveAudience(audienceType, audience, ledger);

            if (targetOpenids.isEmpty())
            {
                log.warn("[rule] 无受众, 跳过: rule={}, ledger={}", ruleCode, ledger.get("id"));
                return false;
            }

            // 3. 组装内容 (contentRule: keyword → 源字段映射)
            Map<String, Object> data = assembleContent(rule.getContentRule(), ledger);

            // 4. 处理 URL
            String url = processUrl(rule.getUrlRule(), ledger, targetOpenids.get(0));

            // 5. 获取模板
            String wxTemplateId = getWxTemplateId(rule.getTemplateId());
            if (wxTemplateId == null)
            {
                log.warn("[rule] 模板不存在: rule={}, tplId={}", ruleCode, rule.getTemplateId());
                return false;
            }

            // 6. 推送给所有受众
            boolean allOk = true;
            for (String openid : targetOpenids)
            {
                Map<String, Object> r = wxMsgPushService.sendByOpenid(openid, null, wxTemplateId, data, url, bizNo, rule.getBizType());
                if (!Boolean.TRUE.equals(r.get("ok")))
                {
                    log.warn("[rule] 推送失败: rule={}, openid={}, msg={}", ruleCode, openid, r.get("msg"));
                    allOk = false;
                }
            }
            return allOk;
        }
        catch (Exception e)
        {
            log.error("[rule] 执行异常 rule={}: {}", ruleCode, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 执行推送规则（旧接口兼容：指定手机号）。
     */
    public boolean execute(String ruleCode, String phone, Map<String, Object> params, String bizNo)
    {
        WxMsgRule rule = selectByCode(ruleCode);
        if (rule == null) return false;
        if (!"1".equals(rule.getEnabled())) return false;

        try
        {
            JSONObject audience = parseJson(rule.getAudienceRule());
            String targetPhone = phone;
            String targetOpenid = null;

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
                return false;

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
                        if (u != null) openidForTicket = u.getOpenid();
                    }
                    String ticket = ticketService.create(openidForTicket);
                    if (StringUtils.isNotEmpty(ticket)) url = url.replace("{ticket}", ticket);
                }
            }

            String wxTemplateId = null;
            com.jonlink.system.domain.WxMpTemplate tpl = rule.getTemplateId() == null ? null
                    : wxMpTemplateMapper.selectWxMpTemplateById(rule.getTemplateId());
            if (tpl != null) wxTemplateId = tpl.getTemplateId();
            if (wxTemplateId == null) return false;

            Map<String, Object> r;
            if (StringUtils.isNotEmpty(targetOpenid))
                r = wxMsgPushService.sendByOpenid(targetOpenid, targetPhone, wxTemplateId, data, url, bizNo, rule.getBizType());
            else
                r = wxMsgPushService.sendByPhone(targetPhone, wxTemplateId, data, url, bizNo, rule.getBizType());
            return Boolean.TRUE.equals(r.get("ok"));
        }
        catch (Exception e)
        {
            log.error("[rule] 执行异常 rule={}: {}", ruleCode, e.getMessage());
            return false;
        }
    }

    // ========== 受众解析 ==========

    /**
     * 解析受众，返回 openid 列表。
     */
    private List<String> resolveAudience(String type, JSONObject audience, Map<String, Object> ledger)
    {
        List<String> openids = new ArrayList<>();

        switch (type)
        {
            case "dist":
                // 从业务员推送：台账 channel_ref → 业务员 → openid + 上级 openid
                openids.addAll(resolveDistAudience(ledger));
                break;
            case "phone":
                // 指定手机号
                if (audience != null)
                {
                    Object phones = audience.get("phones");
                    if (phones instanceof List)
                    {
                        for (Object p : (List<?>) phones)
                        {
                            String openid = findOpenidByPhone(String.valueOf(p));
                            if (openid != null) openids.add(openid);
                        }
                    }
                }
                break;
            case "openid":
                // 指定 openid
                if (audience != null)
                {
                    Object openidsArr = audience.get("openids");
                    if (openidsArr instanceof List)
                    {
                        for (Object o : (List<?>) openidsArr)
                        {
                            openids.add(String.valueOf(o));
                        }
                    }
                }
                break;
            case "all":
                // 全部粉丝（慎用）
                openids.addAll(getAllFanOpenids());
                break;
            default:
                log.warn("[rule] 未知受众类型: {}", type);
        }
        return openids;
    }

    /**
     * 业务员受众：台账 → 业务员 → 映射表查 openid + 上级 openid。
     */
    private List<String> resolveDistAudience(Map<String, Object> ledger)
    {
        List<String> openids = new ArrayList<>();

        // 获取业务员 user_id
        Object channelRef = ledger.get("channel_ref");
        if (channelRef == null) return openids;
        Long distUserId = Long.valueOf(String.valueOf(channelRef));

        // 从映射表查业务员的 openid
        List<String> distOpenids = distFanSyncService.findOpenidsByDistUserId(distUserId);
        if (!distOpenids.isEmpty())
        {
            openids.addAll(distOpenids);
            log.debug("[rule] 业务员 openid (映射表): userId={}, openids={}", distUserId, distOpenids);
        }
        else
        {
            log.warn("[rule] 业务员无映射: userId={}", distUserId);
        }

        // 上级分销员的 openid
        Long parentUserId = distFanSyncService.getParentUserId(distUserId);
        if (parentUserId != null)
        {
            List<String> parentOpenids = distFanSyncService.findOpenidsByDistUserId(parentUserId);
            if (!parentOpenids.isEmpty())
            {
                openids.addAll(parentOpenids);
                log.debug("[rule] 上级 openid (映射表): parentId={}, openids={}", parentUserId, parentOpenids);
            }
        }

        return openids;
    }

    /**
     * 通过手机号查找 openid。
     */
    private String findOpenidByPhone(String phone)
    {
        if (StringUtils.isEmpty(phone)) return null;
        try
        {
            com.jonlink.system.mapper.WxMpUserMapper wxMpUserMapper =
                com.jonlink.common.utils.spring.SpringUtils.getBean(com.jonlink.system.mapper.WxMpUserMapper.class);
            com.jonlink.system.domain.WxMpUser q = new com.jonlink.system.domain.WxMpUser();
            q.setPhone(phone);
            java.util.List<com.jonlink.system.domain.WxMpUser> list = wxMpUserMapper.selectWxMpUserList(q);
            if (!list.isEmpty() && StringUtils.isNotEmpty(list.get(0).getOpenid()))
            {
                return list.get(0).getOpenid();
            }
        }
        catch (Exception e)
        {
            log.warn("[rule] 查找 openid 失败: phone={}, {}", phone, e.getMessage());
        }
        return null;
    }

    /**
     * 获取全部粉丝 openid（仅 all 受众类型使用）。
     */
    private List<String> getAllFanOpenids()
    {
        List<String> openids = new ArrayList<>();
        try
        {
            com.jonlink.system.mapper.WxMpUserMapper wxMpUserMapper =
                com.jonlink.common.utils.spring.SpringUtils.getBean(com.jonlink.system.mapper.WxMpUserMapper.class);
            com.jonlink.system.domain.WxMpUser q = new com.jonlink.system.domain.WxMpUser();
            q.setSubscribe("1");
            List<com.jonlink.system.domain.WxMpUser> fans = wxMpUserMapper.selectWxMpUserList(q);
            for (com.jonlink.system.domain.WxMpUser f : fans)
            {
                if (StringUtils.isNotEmpty(f.getOpenid())) openids.add(f.getOpenid());
            }
        }
        catch (Exception e)
        {
            log.warn("[rule] 获取全部粉丝失败: {}", e.getMessage());
        }
        return openids;
    }

    // ========== 内容组装 ==========

    /**
     * 组装模板内容。
     * contentRule 格式: [{"keyword":"keyword1","source":"ledger_policy_no"}, ...]
     */
    private Map<String, Object> assembleContent(String contentRule, Map<String, Object> ledger)
    {
        Map<String, Object> data = new HashMap<>();
        if (StringUtils.isEmpty(contentRule) || ledger == null) return data;

        try
        {
            List<JSONObject> rules = JSON.parseArray(contentRule, JSONObject.class);
            for (JSONObject r : rules)
            {
                String keyword = r.getString("keyword");
                String source = r.getString("source");
                String fixedValue = r.getString("fixedValue");
                if (keyword == null) continue;

                Object value = null;
                if ("fixed".equals(source))
                {
                    value = fixedValue;
                }
                else if (source != null && source.startsWith("ledger_"))
                {
                    // 从台账取值：ledger_policy_no → policy_no
                    String ledgerField = source.substring(7);
                    value = ledger.get(ledgerField);
                }
                else if (source != null && source.startsWith("contact_"))
                {
                    // 从联系人取值（预留）
                    value = ledger.get(source);
                }
                else if (source != null && source.startsWith("dist_"))
                {
                    // 从业务员取值（预留）
                    value = ledger.get(source);
                }
                else
                {
                    // 直接从台账取
                    value = ledger.get(source);
                }

                // 空值处理：微信模板消息字段不能为空，使用占位符
                data.put(keyword, value != null ? String.valueOf(value) : "-");
            }
        }
        catch (Exception e)
        {
            log.warn("[rule] 内容组装异常: {}", e.getMessage());
        }
        return data;
    }

    // ========== URL 处理 ==========

    private String processUrl(String urlRule, Map<String, Object> ledger, String openid)
    {
        if (StringUtils.isEmpty(urlRule)) return null;
        String url = replacePlaceholder(urlRule, ledger);
        if (url.contains("{ticket}"))
        {
            String ticket = ticketService.create(openid);
            if (StringUtils.isNotEmpty(ticket))
            {
                url = url.replace("{ticket}", ticket);
            }
        }
        return url;
    }

    // ========== 工具方法 ==========

    private String getWxTemplateId(Long tplId)
    {
        if (tplId == null) return null;
        com.jonlink.system.domain.WxMpTemplate tpl = wxMpTemplateMapper.selectWxMpTemplateById(tplId);
        return tpl != null ? tpl.getTemplateId() : null;
    }

    private WxMsgRule selectByCode(String ruleCode)
    {
        WxMsgRule q = new WxMsgRule();
        q.setRuleCode(ruleCode);
        java.util.List<WxMsgRule> list = wxMsgRuleMapper.selectWxMsgRuleList(q);
        return list.isEmpty() ? null : list.get(0);
    }

    private JSONObject parseJson(String s)
    {
        if (StringUtils.isEmpty(s)) return null;
        try { return JSON.parseObject(s); } catch (Exception e) { return null; }
    }

    private String replacePlaceholder(String template, Map<String, Object> params)
    {
        if (StringUtils.isEmpty(template) || params == null) return template;
        String out = template;
        for (Map.Entry<String, Object> e : params.entrySet())
        {
            out = out.replace("{" + e.getKey() + "}", String.valueOf(e.getValue()));
        }
        return out;
    }
}
