package com.jonlink.system.wx.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.jonlink.common.utils.DateUtils;
import com.jonlink.common.utils.StringUtils;
import com.jonlink.common.utils.http.HttpUtils;
import com.jonlink.common.utils.security.AesUtils;
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

    /** 获取 access_token 三级缓存: ①内存 → ②库(微信接口响应存库) → ③微信接口刷新并写库 */
    public String getAccessToken()
    {
        WxMpAccount acc = getAccount();
        if (acc == null || StringUtils.isEmpty(acc.getAppId()) || StringUtils.isEmpty(acc.getAppSecret()))
        {
            log.warn("[wx-mock] 未配置公众号 appid/secret,使用 mock access_token");
            return "mock_access_token_" + System.currentTimeMillis();
        }
        // ① 内存缓存
        if (accessToken != null && System.currentTimeMillis() < tokenExpireAt)
        {
            return accessToken;
        }
        // ② 库缓存(token_expire_time 未过期)
        Long dbExpireAt = acc.getTokenExpireTime() == null ? 0L : acc.getTokenExpireTime().getTime();
        if (StringUtils.isNotEmpty(acc.getAccessToken()) && dbExpireAt > System.currentTimeMillis())
        {
            accessToken = acc.getAccessToken();
            tokenExpireAt = dbExpireAt;
            log.info("[wx-token] 命中数据库缓存,expiresAt={}", DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", new Date(tokenExpireAt)));
            return accessToken;
        }
        // ③ 调微信刷新 + 写库
        String appSecret = AesUtils.decrypt(acc.getAppSecret());
        String url = API + "/token?grant_type=client_credential&appid=" + acc.getAppId() + "&secret=" + appSecret;
        String resp = HttpUtils.sendGet(url);
        JSONObject json = JSON.parseObject(resp);
        if (json != null && json.getString("access_token") != null)
        {
            String newToken = json.getString("access_token");
            long expireIn = json.getIntValue("expires_in", 7200);
            // 提前 200 秒过期, 但最少保留 60 秒有效期
            long bufferSeconds = Math.min(200, Math.max(0, expireIn - 60));
            long newExpireAt = System.currentTimeMillis() + (expireIn - bufferSeconds) * 1000L;
            accessToken = newToken;
            tokenExpireAt = newExpireAt;
            // 写库: 更新启用账号的 access_token + token_expire_time
            WxMpAccount upd = new WxMpAccount();
            upd.setId(acc.getId());
            upd.setAccessToken(newToken);
            upd.setTokenExpireTime(new Date(newExpireAt));
            upd.setUpdateTime(DateUtils.getNowDate());
            try
            {
                wxMpAccountMapper.updateWxMpAccount(upd);
                log.info("[wx-token] 已刷新并写入数据库,expiresIn={}s", expireIn);
            }
            catch (Exception e)
            {
                log.warn("[wx-token] 写库失败,仅内存缓存: {}", e.getMessage());
            }
            return newToken;
        }
        log.error("[wx] 获取 access_token 失败: {}", resp);
        return null;
    }

    /**
     * 根据 AppID + Secret 获取公众号基本信息（名称等）
     *
     * @param appId     公众号 AppID
     * @param appSecret 公众号 AppSecret（明文）
     * @return 包含 nick_name 的 JSON，失败返回 null
     */
    public JSONObject fetchAccountBasicInfo(String appId, String appSecret)
    {
        if (StringUtils.isEmpty(appId) || StringUtils.isEmpty(appSecret))
        {
            log.warn("[wx-fetch] appId 或 appSecret 为空");
            return null;
        }
        appId = appId.trim();
        appSecret = appSecret.trim();
        log.info("[wx-fetch] appId={}, secretlen={}, secret前4位={}", appId, appSecret.length(), appSecret.substring(0, Math.min(4, appSecret.length())));
        // 1. 获取 access_token
        String tokenUrl = API + "/token?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret;
        log.info("[wx-fetch] 请求 access_token, appId={}, secretlen={}", appId, appSecret != null ? appSecret.length() : 0);
        String tokenResp = HttpUtils.sendGet(tokenUrl);
        log.info("[wx-fetch] access_token 响应: {}", tokenResp);
        JSONObject tokenJson = JSON.parseObject(tokenResp);
        if (tokenJson == null || tokenJson.getString("access_token") == null)
        {
            log.error("[wx-fetch] 获取 access_token 失败, resp={}", tokenResp);
            return null;
        }
        String accessToken = tokenJson.getString("access_token");
        // 2. 获取账号基本信息
        String infoUrl = API + "/account/getaccountbasicinfo?access_token=" + accessToken;
        log.info("[wx-fetch] 请求 getaccountbasicinfo");
        String infoResp = HttpUtils.sendGet(infoUrl);
        log.info("[wx-fetch] getaccountbasicinfo 响应: {}", infoResp);
        JSONObject infoJson = JSON.parseObject(infoResp);
        if (infoJson == null || (infoJson.getString("nickname") == null && infoJson.getString("nick_name") == null))
        {
            log.error("[wx-fetch] 获取账号信息失败, resp={}", infoResp);
            return null;
        }
        return infoJson;
    }
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
            String value = v != null ? String.valueOf(v).trim() : "";
            // 根据微信模板字段类型清洗值
            value = sanitizeFieldValue(k, value);
            JSONObject kv = new JSONObject();
            kv.put("value", value);
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
        log.error("[wx] 模板消息发送失败: openid={} templateId={} url={} data={} resp={}",
                openid, templateId, url, dataObj.toJSONString(), resp);
        return Map.of("ok", false, "err", json == null ? resp : json.getString("errmsg"));
    }

    /**
     * 根据微信模板字段类型清洗值。
     * 微信模板消息各类型有严格格式要求，不含前缀标签文字。
     *
     * @param key   关键词 key (如 amount3, character_string2, thing12)
     * @param value 原始值
     * @return 清洗后的值
     */
    private String sanitizeFieldValue(String key, String value) {
        if (value == null || value.isEmpty()) {
            return "-";
        }
        String k = key != null ? key.toLowerCase() : "";

        // amount 类型：只保留数字、小数点、¥/￥符号
        if (k.startsWith("amount")) {
            // 去掉中文前缀（如 "交费金额:" → "10000.00"）
            String cleaned = value.replaceAll("^[^\\d¥￥.]*", "").replaceAll("[^\\d¥￥.]", "");
            // 如果清洗后为空（全是中文），尝试提取数字
            if (cleaned.isEmpty()) {
                cleaned = value.replaceAll("[^\\d.]", "");
            }
            return cleaned.isEmpty() ? "0" : cleaned;
        }

        // character_string 类型：只允许字母数字和有限特殊字符，不允许中文
        if (k.startsWith("character_string")) {
            // 去掉中文前缀（如 "保单编号:" → "P000000001"）
            String cleaned = value.replaceAll("[^a-zA-Z0-9\\-_ ]", "");
            // 如果去掉中文后为空，说明整个值都是中文，尝试去掉冒号等分隔符
            if (cleaned.isEmpty()) {
                cleaned = value.replaceAll("[：:]", "").replaceAll("[\\u4e00-\\u9fa5]", "");
            }
            return cleaned.isEmpty() ? "N/A" : cleaned.trim();
        }

        // car_number 类型：车牌号，保留字母数字和中文省份简称
        if (k.startsWith("car_number")) {
            // 去掉 "车牌号:" 等前缀标签
            String cleaned = value.replaceAll("^[^\\u4e00-\\u9fa5a-zA-Z0-9]*", "");
            return cleaned.isEmpty() ? value : cleaned;
        }

        // thing 类型：最多 20 个字符，允许中文
        if (k.startsWith("thing")) {
            String cleaned = value.replaceAll("^[^\\u4e00-\\u9fa5a-zA-Z0-9\\-_ ]*", "");
            if (cleaned.isEmpty()) cleaned = value;
            return cleaned.length() > 20 ? cleaned.substring(0, 20) : cleaned;
        }

        // phrase 类型：最多 20 个字符，允许中文
        if (k.startsWith("phrase")) {
            String cleaned = value.replaceAll("^[^\\u4e00-\\u9fa5a-zA-Z0-9\\-_ ]*", "");
            if (cleaned.isEmpty()) cleaned = value;
            return cleaned.length() > 20 ? cleaned.substring(0, 20) : cleaned;
        }

        // time 类型：去掉中文前缀（如 "缴费时间:" → "2023-01-08"）
        if (k.startsWith("time") || k.startsWith("date")) {
            String cleaned = value.replaceAll("^[^\\d\\-/:年月日时分秒.]*", "");
            return cleaned.isEmpty() ? value : cleaned;
        }

        // 默认：去掉开头的中文前缀标签
        String cleaned = value.replaceAll("^[^\\u4e00-\\u9fa5]*[\\u4e00-\\u9fa5]+[:：]?\\s*", "");
        if (!cleaned.equals(value)) return cleaned.isEmpty() ? value : cleaned;

        return value;
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
        if (json != null && (json.getIntValue("errcode", -1) == 0 || !json.containsKey("errcode")))
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
        if (json != null && (!json.containsKey("errcode") || json.getIntValue("errcode") == 0))
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
            if (json == null)
            {
                log.error("[wx] 拉取粉丝列表失败: null");
                break;
            }
            if (json.containsKey("errcode") && json.getIntValue("errcode") != 0)
            {
                log.error("[wx] 拉取粉丝列表失败: {}", json.toJSONString());
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
        log.warn("[wx] 拉取到 {} 个 openid, 开始批量获取详情", total);
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
            if (json == null)
            {
                log.error("[wx] 批量获取粉丝详情失败: null");
                continue;
            }
            if (json.containsKey("errcode") && json.getIntValue("errcode") != 0)
            {
                log.error("[wx] 批量获取粉丝详情失败: {}", resp);
                continue;
            }
            JSONArray userList = json.getJSONArray("user_info_list");
            if (userList == null)
            {
                log.warn("[wx] batchget 返回 user_info_list 为空, resp={}", resp);
                continue;
            }
            if (i == 0)
            {
                log.warn("[wx] batchget 首批用户数据样本: {}", userList.size() > 0 ? userList.getJSONObject(0).toJSONString() : "empty");
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
