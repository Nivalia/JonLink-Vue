package com.jonlink.system.wx.service;

import java.util.List;
import java.util.Map;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.jonlink.common.utils.StringUtils;
import com.jonlink.common.utils.http.HttpUtils;
import com.jonlink.system.domain.WxMpAccount;
import com.jonlink.system.mapper.WxMpAccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 微信公众平台 API 客户端
 *
 * 说明：未配置有效 appid/app_secret 时进入 MOCK 模式（返回模拟数据并告警日志），
 * 保证业务链路(核销→佣金→台账→结算→推送记录)在无微信环境也能完整跑通；
 * 配置真实账号后自动切换真实调用，业务代码零改动。
 *
 * @author jonlink
 */
@Service
public class WxMpService
{
    private static final Logger log = LoggerFactory.getLogger(WxMpService.class);

    private static final String API = "https://api.weixin.qq.com/cgi-bin";

    @Autowired
    private WxMpAccountMapper wxMpAccountMapper;
    @Autowired
    private com.jonlink.system.mapper.WxMpUserMapper wxMpUserMapper;

    private volatile String accessToken;
    private volatile long tokenExpireAt = 0;

    /** 读取启用的公众号账号 */
    private WxMpAccount getAccount()
    {
        WxMpAccount q = new WxMpAccount();
        q.setStatus("1");
        List<WxMpAccount> list = wxMpAccountMapper.selectWxMpAccountList(q);
        return list.isEmpty() ? null : list.get(0);
    }

    /** 是否真实可用(配置了 appid+secret) */
    public boolean isReal()
    {
        WxMpAccount acc = getAccount();
        return acc != null && StringUtils.isNotEmpty(acc.getAppId()) && StringUtils.isNotEmpty(acc.getAppSecret());
    }

    /** 获取 access_token（带缓存，未配置时 mock） */
    public String getAccessToken()
    {
        if (!isReal())
        {
            log.warn("[wx-mock] 未配置公众号 appid/secret，使用 mock access_token");
            return "mock_access_token_" + System.currentTimeMillis();
        }
        if (accessToken != null && System.currentTimeMillis() < tokenExpireAt)
        {
            return accessToken;
        }
        WxMpAccount acc = getAccount();
        String url = API + "/token?grant_type=client_credential&appid=" + acc.getAppId() + "&secret=" + acc.getAppSecret();
        String resp = HttpUtils.sendGet(url);
        JSONObject json = JSON.parseObject(resp);
        if (json != null && json.getString("access_token") != null)
        {
            accessToken = json.getString("access_token");
            tokenExpireAt = System.currentTimeMillis() + (json.getIntValue("expires_in", 7200) - 200) * 1000L;
            return accessToken;
        }
        log.error("[wx] 获取 access_token 失败: {}", resp);
        return null;
    }

    /** 发送模板消息；返回 (是否成功, 微信msgid/错误信息) */
    public Map<String, Object> sendTemplateMsg(String openid, String templateId, Map<String, Object> data,
            String url)
    {
        if (StringUtils.isEmpty(openid) || StringUtils.isEmpty(templateId))
        {
            return Map.of("ok", false, "err", "openid或templateId为空");
        }
        if (!isReal())
        {
            log.warn("[wx-mock] 模拟发送模板消息 openid={} templateId={}", openid, templateId);
            return Map.of("ok", true, "msgid", "mock_msg_" + System.currentTimeMillis());
        }
        String token = getAccessToken();
        if (token == null)
        {
            return Map.of("ok", false, "err", "access_token 获取失败");
        }
        JSONObject body = new JSONObject();
        body.put("touser", openid);
        body.put("template_id", templateId);
        body.put("url", url);
        JSONObject dataObj = new JSONObject();
        data.forEach((k, v) -> {
            JSONObject kv = new JSONObject();
            kv.put("value", v);
            kv.put("color", "#173177");
            dataObj.put(k, kv);
        });
        body.put("data", dataObj);
        String resp = HttpUtils.sendPost(API + "/message/template/send?access_token=" + token, body.toJSONString());
        JSONObject json = JSON.parseObject(resp);
        if (json != null && json.getIntValue("errcode") == 0)
        {
            return Map.of("ok", true, "msgid", String.valueOf(json.getLongValue("msgid")));
        }
        log.error("[wx] 模板消息发送失败: {}", resp);
        return Map.of("ok", false, "err", json == null ? resp : json.getString("errmsg"));
    }

    /** 创建永久二维码(分销); 返回 ticket/qrUrl/错误 */
    public Map<String, Object> createPermanentQr(int sceneId)
    {
        JSONObject body = new JSONObject();
        JSONObject scene = new JSONObject();
        scene.put("scene_id", sceneId);
        body.put("action_name", "QR_LIMIT_SCENE");
        body.put("action_info", new JSONObject().fluentPut("scene", scene));
        return createQr(body);
    }

    /** 创建临时二维码(活动/通知/公告); expireSeconds ≤ 2592000(30天) */
    public Map<String, Object> createTempQr(String sceneStr, int expireSeconds)
    {
        JSONObject body = new JSONObject();
        JSONObject scene = new JSONObject();
        scene.put("scene_str", sceneStr);
        body.put("expire_seconds", Math.min(expireSeconds, 2592000));
        body.put("action_name", "QR_STR_SCENE");
        body.put("action_info", new JSONObject().fluentPut("scene", scene));
        return createQr(body);
    }

    private Map<String, Object> createQr(JSONObject body)
    {
        if (!isReal())
        {
            long t = System.currentTimeMillis();
            log.warn("[wx-mock] 模拟创建二维码: {}", body.toJSONString());
            return Map.of("ok", true, "ticket", "mock_ticket_" + t, "qrUrl", "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=mock_ticket_" + t);
        }
        String token = getAccessToken();
        if (token == null)
        {
            return Map.of("ok", false, "err", "access_token 获取失败");
        }
        String resp = HttpUtils.sendPost(API + "/qrcode/create?access_token=" + token, body.toJSONString());
        JSONObject json = JSON.parseObject(resp);
        if (json != null && json.getString("ticket") != null)
        {
            String qrUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + json.getString("ticket");
            return Map.of("ok", true, "ticket", json.getString("ticket"), "qrUrl", qrUrl,
                    "expireSeconds", json.getIntValue("expire_seconds", 0));
        }
        log.error("[wx] 创建二维码失败: {}", resp);
        return Map.of("ok", false, "err", json == null ? resp : json.getString("errmsg"));
    }

    /** 获取用户基本信息(关注事件时用) */
    public Map<String, Object> getUserInfo(String openid)
    {
        if (!isReal())
        {
            log.warn("[wx-mock] 模拟获取用户信息 openid={}", openid);
            return Map.of("nickname", "微信用户", "sex", "0", "subscribe", "1");
        }
        String token = getAccessToken();
        if (token == null)
        {
            return Map.of();
        }
        String resp = HttpUtils.sendGet(API + "/user/info?access_token=" + token + "&openid=" + openid);
        JSONObject json = JSON.parseObject(resp);
        if (json != null && json.getIntValue("errcode", -1) == -1 || (json != null && json.getIntValue("errcode") == 0))
        {
            return Map.of("nickname", json.getString("nickname"), "sex", json.getString("sex"),
                    "subscribe", json.getString("subscribe"), "avatar", json.getString("headimgurl"),
                    "city", json.getString("city"), "province", json.getString("province"), "country", json.getString("country"));
        }
        log.error("[wx] 获取用户信息失败: {}", resp);
        return Map.of();
    }

    /** 设置行业 */
    public boolean setIndustry(String industryId1, String industryId2)
    {
        if (!isReal())
        {
            log.warn("[wx-mock] 模拟设置行业");
            return true;
        }
        String token = getAccessToken();
        if (token == null)
        {
            return false;
        }
        JSONObject body = new JSONObject();
        body.put("industry_id1", industryId1);
        body.put("industry_id2", industryId2);
        String resp = HttpUtils.sendPost(API + "/template/api_set_industry?access_token=" + token, body.toJSONString());
        JSONObject json = JSON.parseObject(resp);
        return json != null && json.getIntValue("errcode") == 0;
    }

    /** 拉取模板列表 */
    public List<JSONObject> getAllTemplates()
    {
        if (!isReal())
        {
            log.warn("[wx-mock] 模拟拉取模板列表");
            return List.of();
        }
        String token = getAccessToken();
        if (token == null)
        {
            return List.of();
        }
        String resp = HttpUtils.sendGet(API + "/template/get_all_private_template?access_token=" + token);
        JSONObject json = JSON.parseObject(resp);
        if (json != null && json.getIntValue("errcode", -1) == 0)
        {
            return json.getJSONArray("template_list").toJavaList(JSONObject.class);
        }
        log.error("[wx] 拉取模板列表失败: {}", resp);
        return List.of();
    }

    /** 创建模板(按短ID) */
    public String createTemplate(String templateIdShort)
    {
        if (!isReal())
        {
            log.warn("[wx-mock] 模拟创建模板 shortId={}", templateIdShort);
            return "mock_template_" + templateIdShort;
        }
        String token = getAccessToken();
        if (token == null)
        {
            return null;
        }
        JSONObject body = new JSONObject();
        body.put("template_id_short", templateIdShort);
        String resp = HttpUtils.sendPost(API + "/template/api_add_template?access_token=" + token, body.toJSONString());
        JSONObject json = JSON.parseObject(resp);
        if (json != null && json.getIntValue("errcode") == 0)
        {
            return json.getString("template_id");
        }
        log.error("[wx] 创建模板失败: {}", resp);
        return null;
    }

    /**
     * 从公众号拉取粉丝列表并入库（真实模式走微信 API，mock 模式造模拟数据）。
     *
     * @return {total 拉取总数, added 新增, updated 更新}
     */
    public Map<String, Object> syncFollowers()
    {
        int added = 0;
        int updated = 0;
        int total = 0;
        if (!isReal())
        {
            log.warn("[wx-mock] 模拟同步粉丝(生成 3 条测试粉丝)");
            String[][] mocks = {
                { "openid_customer_2", "李四", "13700000002", "1" },
                { "openid_customer_3", "王五", "13600000003", "1" },
                { "openid_customer_4", "赵六", "13500000004", "0" },
            };
            for (String[] m : mocks)
            {
                com.jonlink.system.domain.WxMpUser u = new com.jonlink.system.domain.WxMpUser();
                u.setOpenid(m[0]);
                u.setNickname(m[1]);
                u.setPhone(m[2]);
                u.setSubscribe(m[3]);
                u.setSex("0");
                u.setProvince("河北");
                u.setCity("石家庄");
                u.setActivityCount(0L);
                u.setActivityLevel("3");
                u.setSubscribeTime(new java.util.Date());
                int r = wxMpUserMapper.upsertWxMpUser(u);
                if (r == 1) added++;
                else if (r == 2) updated++;
                total++;
            }
            return Map.of("total", total, "added", added, "updated", updated, "mock", true);
        }
        String token = getAccessToken();
        if (token == null)
        {
            return Map.of("total", 0, "added", 0, "updated", 0, "msg", "获取 access_token 失败");
        }
        // 1. 分页拉取 openid 列表
        String nextOpenid = "";
        java.util.List<String> openids = new java.util.ArrayList<>();
        for (int page = 0; page < 100; page++)
        {
            String url = API + "/user/get?access_token=" + token + "&next_openid=" + nextOpenid;
            JSONObject json = JSON.parseObject(HttpUtils.sendGet(url));
            if (json == null || json.getIntValue("errcode", -1) != 0)
            {
                log.error("[wx] 拉取粉丝列表失败: {}", json == null ? "null" : json.toJSONString());
                break;
            }
            JSONArray data = json.getJSONObject("data") == null ? null : json.getJSONObject("data").getJSONArray("openid");
            if (data != null)
            {
                for (int i = 0; i < data.size(); i++)
                {
                    openids.add(data.getString(i));
                }
            }
            nextOpenid = json.getString("next_openid");
            if (StringUtils.isEmpty(nextOpenid))
            {
                break;
            }
        }
        total = openids.size();
        // 2. 批量获取用户详情（每批 100）
        for (int i = 0; i < openids.size(); i += 100)
        {
            java.util.List<String> batch = openids.subList(i, Math.min(i + 100, openids.size()));
            JSONObject body = new JSONObject();
            JSONArray list = new JSONArray();
            for (String oid : batch)
            {
                JSONObject item = new JSONObject();
                item.put("openid", oid);
                item.put("lang", "zh_CN");
                list.add(item);
            }
            body.put("user_list", list);
            String resp = HttpUtils.sendPost(API + "/user/info/batchget?access_token=" + token, body.toJSONString());
            JSONObject json = JSON.parseObject(resp);
            if (json == null || json.getIntValue("errcode", -1) != 0)
            {
                log.error("[wx] 批量获取粉丝详情失败: {}", resp);
                continue;
            }
            JSONArray userList = json.getJSONArray("user_info_list");
            if (userList == null)
            {
                continue;
            }
            for (int j = 0; j < userList.size(); j++)
            {
                JSONObject u = userList.getJSONObject(j);
                com.jonlink.system.domain.WxMpUser user = new com.jonlink.system.domain.WxMpUser();
                user.setOpenid(u.getString("openid"));
                user.setNickname(u.getString("nickname"));
                user.setAvatar(u.getString("headimgurl"));
                user.setSex(String.valueOf(u.getIntValue("sex", 0)));
                user.setCountry(u.getString("country"));
                user.setProvince(u.getString("province"));
                user.setCity(u.getString("city"));
                user.setSubscribe(String.valueOf(u.getIntValue("subscribe", 0)));
                user.setActivityCount(0L);
                user.setActivityLevel("3");
                int r = wxMpUserMapper.upsertWxMpUser(user);
                if (r == 1)
                {
                    added++;
                }
                else if (r == 2)
                {
                    updated++;
                }
            }
        }
        return Map.of("total", total, "added", added, "updated", updated, "mock", false);
    }
}
