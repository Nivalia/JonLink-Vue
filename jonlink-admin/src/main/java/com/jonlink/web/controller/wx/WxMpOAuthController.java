package com.jonlink.web.controller.wx;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.jonlink.common.annotation.Anonymous;
import com.jonlink.common.core.controller.BaseController;
import com.jonlink.common.core.domain.AjaxResult;
import com.jonlink.common.utils.StringUtils;
import com.jonlink.system.wx.service.WxMpOAuthService;
import com.jonlink.system.wx.service.TicketService;

/**
 * 公众号网页授权入口
 *
 * - /wx/oauth/authorize  生成微信 authorize URL（mock 模式直接返回 direct: 伪 URL）
 * - /wx/oauth/callback   微信回调：code 换 token → 拉 userinfo → 落库
 * - /wx/oauth/jump       一站式入口：直接 302 到 authorize URL
 * - /wx/oauth/user       凭 openid 拉粉丝信息（H5 用，不走微信 API）
 *
 * 所有接口均免登录（@ss 不校验），用于微信浏览器/H5 唤起。
 *
 * @author jonlink
 */
@Anonymous
@RestController
@RequestMapping("/wx/oauth")
public class WxMpOAuthController extends BaseController
{
    private static final Logger log = LoggerFactory.getLogger(WxMpOAuthController.class);

    /** 验证码存储: phone -> {code, expireAt} */
    private static final ConcurrentHashMap<String, long[]> CODE_STORE = new ConcurrentHashMap<>();
    /** 验证码有效期 5 分钟 */
    private static final long CODE_EXPIRE_MS = TimeUnit.MINUTES.toMillis(5);

    @Autowired
    private WxMpOAuthService oauthService;
    @Autowired
    private com.jonlink.system.wx.service.TicketService ticketService;

    /** 一站式跳转（推荐）：直接 302 到微信 authorize，mock 模式 302 到回调 */
    @GetMapping("/jump")
    public void jump(@RequestParam String redirectUri,
                     @RequestParam(defaultValue = "snsapi_base") String scope,
                     @RequestParam(required = false) String state,
                     HttpServletResponse response) throws IOException
    {
        String url = oauthService.buildAuthorizeUrl(redirectUri, scope, state);
        log.info("[wx-oauth] jump → {}", url.startsWith("direct:") ? "DIRECT " + url.substring(7) : url);
        if (url.startsWith("direct:")) {
            response.sendRedirect(url.substring(7));
        } else {
            response.sendRedirect(url);
        }
    }

    /** 仅生成 URL（前端拿到后自行跳转） */
    @GetMapping("/authorize")
    public AjaxResult authorize(@RequestParam String redirectUri,
                                @RequestParam(defaultValue = "snsapi_base") String scope,
                                @RequestParam(required = false) String state)
    {
        return success(oauthService.buildAuthorizeUrl(redirectUri, scope, state));
    }

    /** 微信回调：code 换 token → userinfo → 落库 → 重定向到 redirectUri */
    @GetMapping("/callback")
    public void callback(@RequestParam(required = false) String code,
                         @RequestParam(required = false) String state,
                         @RequestParam(required = false) String openid,  // mock 透传
                         @RequestParam(required = false) String redirectUri,
                         HttpServletResponse response) throws IOException
    {
        try {
            // mock 模式无 code 时自动造 openid（保留测试链路）
            if (StringUtils.isEmpty(code) && StringUtils.isEmpty(openid)) {
                openid = "mock_openid_" + java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 16);
            }
            var user = oauthService.handleCallback(code, state, openid);
            String openidStr = (String) user.get("openid");
            String ticket = ticketService.create(openidStr);
            String target = StringUtils.isNotEmpty(redirectUri)
                    ? redirectUri + (redirectUri.contains("?") ? "&" : "?")
                      + "openid=" + URLEncoder.encode(openidStr, StandardCharsets.UTF_8)
                      + "&ticket=" + URLEncoder.encode(ticket, StandardCharsets.UTF_8)
                      + "&nickname=" + URLEncoder.encode(safe(user.get("nickname")), StandardCharsets.UTF_8)
                      + "&avatar=" + URLEncoder.encode(safe(user.get("avatar")), StandardCharsets.UTF_8)
                      + "&phone=" + URLEncoder.encode(safe(user.get("phone")), StandardCharsets.UTF_8)
                      + (state != null ? "&state=" + URLEncoder.encode(state, StandardCharsets.UTF_8) : "")
                    : "/wx/oauth/user?openid=" + openidStr;
            log.info("[wx-oauth] callback → redirect to {}", target);
            response.sendRedirect(target);
        } catch (Exception e) {
            log.error("[wx-oauth] callback failed", e);
            response.sendRedirect("/wx/oauth/error?msg=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
        }
    }

    /** H5 拉粉丝信息（凭 openid，不走微信 API） */
    @GetMapping("/user")
    public AjaxResult user(@RequestParam String openid)
    {
        var q = new com.jonlink.system.domain.WxMpUser();
        q.setOpenid(openid);
        var list = wxMpUserMapper().selectWxMpUserList(q);
        if (list.isEmpty()) return AjaxResult.error("用户未授权");
        return success(list.get(0));
    }

    @GetMapping("/error")
    public AjaxResult error(@RequestParam(required = false) String msg)
    {
        return AjaxResult.error("授权失败: " + msg);
    }

    /** H5 绑定手机号 */
    @PostMapping("/bindPhone")
    public AjaxResult bindPhone(@RequestParam String openid, @RequestParam String phone, @RequestParam(required = false) String code)
    {
        if (StringUtils.isEmpty(openid) || StringUtils.isEmpty(phone)) {
            return AjaxResult.error("openid 和手机号不能为空");
        }
        // 验证短信验证码
        if (StringUtils.isEmpty(code)) {
            return AjaxResult.error("验证码不能为空");
        }
        long[] stored = CODE_STORE.get(phone);
        if (stored == null) {
            return AjaxResult.error("验证码已过期，请重新获取");
        }
        if (System.currentTimeMillis() > stored[1]) {
            CODE_STORE.remove(phone);
            return AjaxResult.error("验证码已过期，请重新获取");
        }
        if (!String.valueOf((int) stored[0]).equals(code)) {
            return AjaxResult.error("验证码错误");
        }
        CODE_STORE.remove(phone);
        var q = new com.jonlink.system.domain.WxMpUser();
        q.setOpenid(openid);
        var list = wxMpUserMapper().selectWxMpUserList(q);
        if (list.isEmpty()) return AjaxResult.error("用户未授权");
        var user = list.get(0);
        user.setPhone(phone);
        wxMpUserMapper().updateWxMpUser(user);
        log.info("[wx-oauth] bindPhone openid={}, phone={}", openid, phone);
        return AjaxResult.success("绑定成功");
    }

    /** 发送验证码（生成6位随机验证码，暂存内存，实际生产环境需对接短信服务） */
    @PostMapping("/sendCode")
    public AjaxResult sendCode(@RequestParam String phone)
    {
        if (StringUtils.isEmpty(phone) || !phone.matches("^1[3-9]\\d{9}$")) {
            return AjaxResult.error("手机号格式不正确");
        }
        int code = (int) (Math.random() * 900000 + 100000);
        long expireAt = System.currentTimeMillis() + CODE_EXPIRE_MS;
        CODE_STORE.put(phone, new long[]{code, expireAt});
        log.info("[wx-oauth] sendCode phone={}, code={} (生产环境需对接短信服务)", phone, code);
        return AjaxResult.success("验证码已发送");
    }

    private static String safe(Object o) { return o == null ? "" : o.toString(); }

    private com.jonlink.system.mapper.WxMpUserMapper wxMpUserMapper()
    {
        return com.jonlink.common.utils.spring.SpringUtils.getBean(com.jonlink.system.mapper.WxMpUserMapper.class);
    }
}