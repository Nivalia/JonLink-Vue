package com.jonlink.system.wx.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.jonlink.common.utils.StringUtils;
import com.jonlink.common.utils.http.HttpUtils;
import com.jonlink.system.domain.WxMpAccount;
import com.jonlink.system.domain.WxMpUser;
import com.jonlink.system.mapper.WxMpAccountMapper;
import com.jonlink.system.mapper.WxMpUserMapper;

/**
 * 公众号网页授权（snsapi_base / snsapi_userinfo）
 *
 * 流程：
 * 1. buildAuthorizeUrl() → 拼接微信 authorize URL（mock 模式返回 direct:...?openid=...）
 * 2. exchangeCode()      → code 换 access_token + openid；再拉 userinfo（userinfo scope）
 * 3. upsert user         → 写入/更新 wx_mp_user（昵称/头像/省市区/性别）
 *
 * mock 模式：未配置 appid/secret 时直接发 mock openid，便于本地/CI 跑通业务流程。
 *
 * @author jonlink
 */
@Service
public class WxMpOAuthService
{
    private static final Logger log = LoggerFactory.getLogger(WxMpOAuthService.class);

    private static final String AUTH_URL  = "https://open.weixin.qq.com/connect/oauth2/authorize";
    private static final String TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
    private static final String USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo";

    @Autowired
    private WxMpAccountMapper wxMpAccountMapper;
    @Autowired
    private WxMpUserMapper wxMpUserMapper;
    @org.springframework.beans.factory.annotation.Value("${wx.oauth.callback-base:http://115.190.215.93/wx/oauth/callback}")
    private String callbackBase;

    private WxMpAccount getAccount() {
        WxMpAccount q = new WxMpAccount();
        q.setStatus("1");
        var list = wxMpAccountMapper.selectWxMpAccountList(q);
        return list.isEmpty() ? null : list.get(0);
    }

    public boolean isReal() {
        WxMpAccount acc = getAccount();
        return acc != null && StringUtils.isNotEmpty(acc.getAppId()) && StringUtils.isNotEmpty(acc.getAppSecret());
    }

    /**
     * 生成跳转 URL。
     * @param redirectUri 授权后回调地址（绝对 URL，需 urlEncode）
     * @param scope       snsapi_base（静默）/ snsapi_userinfo（弹窗拉昵称）
     * @param state       业务透传（CSRF 防伪）
     * @return 完整微信 authorize URL；mock 模式返回 direct: 开头的伪 URL 让前端直接 callback
     */
    public String buildAuthorizeUrl(String redirectUri, String scope, String state) {
        if (!isReal()) {
            // mock：跳到本服务的 /wx/oauth/callback，让 callback 统一落库 + 生成 ticket
            // （避免 BUG-4 绕过身份验证：必须经 callback 才能拿 ticket）
            String mockOpenid = "mock_openid_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
            StringBuilder sb = new StringBuilder("direct:/wx/oauth/callback?");
            sb.append("openid=").append(mockOpenid);
            sb.append("&scope=mock");
            sb.append("&redirectUri=").append(urlEnc(redirectUri));
            if (StringUtils.isNotEmpty(state)) sb.append("&state=").append(urlEnc(state));
            log.warn("[wx-mock] 模拟授权：mockOpenid={}, redirect={}", mockOpenid, redirectUri);
            return sb.toString();
        }
        WxMpAccount acc = getAccount();
        String s = StringUtils.isEmpty(scope) ? "snsapi_base" : scope;
        String st = StringUtils.isEmpty(state) ? UUID.randomUUID().toString().substring(0, 8) : state;
        // OAuth redirect_uri 必须指向后端 callback，callback 处理完再跳转到业务页面
        String callbackUrl = callbackBase + "?redirectUri=" + urlEnc(redirectUri);
        if (StringUtils.isNotEmpty(state)) callbackUrl += "&state=" + urlEnc(st);
        return AUTH_URL
                + "?appid=" + acc.getAppId()
                + "&redirect_uri=" + urlEnc(callbackUrl)
                + "&response_type=code"
                + "&scope=" + s
                + "&state=" + urlEnc(st)
                + "#wechat_redirect";
    }

    /**
     * code 换 access_token，再 userinfo；落库 wx_mp_user（upsert）。
     * mock 模式跳过所有 HTTP，造一个假粉丝直接落库。
     *
     * @return user，包含 openid/nickname/avatar/city/province/country/sex 等；失败抛 RuntimeException
     */
    public Map<String, Object> handleCallback(String code, String state, String mockOpenid) {
        WxMpUser user = null;
        if (StringUtils.isNotEmpty(mockOpenid)) {
            // mock 直接落地（首次同步关注时的占位用户）
            user = upsertMock(mockOpenid);
        } else if (!isReal() || StringUtils.isEmpty(code)) {
            throw new IllegalArgumentException("未配置公众号或 code 缺失");
        } else {
            WxMpAccount acc = getAccount();
            String appSecret = com.jonlink.common.utils.security.AesUtils.decrypt(acc.getAppSecret());
            String url = TOKEN_URL
                    + "?appid=" + acc.getAppId()
                    + "&secret=" + appSecret
                    + "&code=" + code
                    + "&grant_type=authorization_code";
            String resp = HttpUtils.sendGet(url);
            log.info("[wx-oauth] token exchange resp={}", resp);
            JSONObject json = JSON.parseObject(resp);
            if (json == null || json.getString("access_token") == null) {
                throw new RuntimeException("换取 access_token 失败: " + resp);
            }
            String accessToken = json.getString("access_token");
            String openid = json.getString("openid");
            String scope = json.getString("scope");
            // snsapi_userinfo 才拉详细信息
            Map<String, Object> userinfo = new HashMap<>();
            if (StringUtils.isNotEmpty(scope) && scope.contains("userinfo")) {
                String u = USERINFO_URL + "?access_token=" + accessToken + "&openid=" + openid + "&lang=zh_CN";
                String ur = HttpUtils.sendGet(u);
                log.info("[wx-oauth] userinfo resp={}", ur);
                JSONObject uj = JSON.parseObject(ur);
                if (uj != null) {
                    userinfo.put("nickname", uj.getString("nickname"));
                    userinfo.put("avatar", uj.getString("headimgurl"));
                    userinfo.put("sex", String.valueOf(uj.getIntValue("sex", 0)));
                    userinfo.put("country", uj.getString("country"));
                    userinfo.put("province", uj.getString("province"));
                    userinfo.put("city", uj.getString("city"));
                    userinfo.put("unionid", uj.getString("unionid"));
                }
            }
            userinfo.put("openid", openid);
            user = upsertFromUserinfo(userinfo);
        }
        Map<String, Object> out = new HashMap<>();
        out.put("openid", user.getOpenid());
        out.put("nickname", user.getNickname());
        out.put("avatar", user.getAvatar());
        out.put("phone", user.getPhone());
        out.put("city", user.getCity());
        out.put("province", user.getProvince());
        out.put("sex", user.getSex());
        out.put("subscribe", user.getSubscribe());
        out.put("state", state);
        out.put("mock", !isReal());
        return out;
    }

    private WxMpUser upsertMock(String openid) {
        WxMpUser q = new WxMpUser();
        q.setOpenid(openid);
        var list = wxMpUserMapper.selectWxMpUserList(q);
        WxMpUser u = list.isEmpty() ? null : list.get(0);
        if (u == null) {
            u = new WxMpUser();
            u.setOpenid(openid);
            u.setNickname("游客_" + openid.substring(Math.max(0, openid.length() - 6)));
            u.setAvatar("");
            u.setSubscribe("1");
            u.setActivityLevel("3");
            u.setSex("0");
            u.setCreateBy("wx-oauth-mock");
            wxMpUserMapper.insertWxMpUser(u);
        } else {
            u.setLastActivityTime(new java.util.Date());
            wxMpUserMapper.updateWxMpUser(u);
        }
        return u;
    }

    private WxMpUser upsertFromUserinfo(Map<String, Object> info) {
        String openid = (String) info.get("openid");
        WxMpUser q = new WxMpUser();
        q.setOpenid(openid);
        var list = wxMpUserMapper.selectWxMpUserList(q);
        WxMpUser u = list.isEmpty() ? new WxMpUser() : list.get(0);
        u.setOpenid(openid);
        if (info.get("nickname") != null) u.setNickname((String) info.get("nickname"));
        if (info.get("avatar") != null) u.setAvatar((String) info.get("avatar"));
        if (info.get("sex") != null) u.setSex((String) info.get("sex"));
        if (info.get("country") != null) u.setCountry((String) info.get("country"));
        if (info.get("province") != null) u.setProvince((String) info.get("province"));
        if (info.get("city") != null) u.setCity((String) info.get("city"));
        u.setLastActivityTime(new java.util.Date());
        if (list.isEmpty()) {
            u.setSubscribe("1");
            u.setSubscribeTime(new java.util.Date());
            u.setActivityLevel("3");
            u.setCreateBy("wx-oauth");
            log.info("[wx-oauth] upsert insert openid={}, nickname={}", openid, u.getNickname());
            wxMpUserMapper.insertWxMpUser(u);
        } else {
            log.info("[wx-oauth] upsert update id={}, openid={}, nickname={}", u.getId(), openid, u.getNickname());
            wxMpUserMapper.updateWxMpUser(u);
        }
        return u;
    }

    private static String urlEnc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}