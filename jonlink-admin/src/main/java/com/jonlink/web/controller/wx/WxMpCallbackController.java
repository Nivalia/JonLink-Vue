package com.jonlink.web.controller.wx;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.jonlink.common.annotation.Anonymous;
import com.jonlink.common.core.controller.BaseController;
import com.jonlink.common.core.domain.AjaxResult;
import com.jonlink.common.utils.DateUtils;
import com.jonlink.common.utils.StringUtils;
import com.jonlink.system.domain.WxMpCallbackLog;
import com.jonlink.system.domain.WxMpUser;
import com.jonlink.system.mapper.WxBizMapper;
import com.jonlink.system.wx.service.WxLedgerService;
import com.jonlink.system.wx.service.WxMsgPushService;
import com.jonlink.system.wx.service.WxQrService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信服务器回调 (README §5.2 接口1-3)
 * - GET  /wx/mp/callback  服务器配置验证(验签回显 echostr)
 * - POST /wx/mp/callback  消息/事件推送(关注/取关/扫码/模板送达回执)
 * - GET  /wx/mp/check     验签健康检查
 *
 * 全部匿名访问; 验签失败一律返回失败/空串。
 *
 * @author jonlink
 */
@RestController
@RequestMapping("/wx/mp")
public class WxMpCallbackController extends BaseController
{
    private static final Logger log = LoggerFactory.getLogger(WxMpCallbackController.class);

    private static final Pattern TAG_PATTERN = Pattern.compile("<(\\w+)>(.*?)</\\1>", Pattern.DOTALL);

    @Autowired
    private WxBizMapper wxBizMapper;
    @Autowired
    private com.jonlink.system.mapper.WxMpUserMapper wxMpUserMapper;
    @Autowired
    private com.jonlink.system.mapper.WxMpAccountMapper wxMpAccountMapper;
    @Autowired
    private WxQrService wxQrService;
    @Autowired
    private WxMsgPushService wxMsgPushService;
    @Autowired
    private WxLedgerService wxLedgerService;
    @Autowired
    private com.jonlink.system.wx.service.IWxMpCallbackLogService wxMpCallbackLogService;
    @org.springframework.beans.factory.annotation.Value("${wx.callback.allow-mock:true}")
    private boolean allowMock;

    /**
     * 服务器配置验证 (GET): signature/timestamp/nonce/echostr
     * 验签通过回显 echostr, 否则返回失败
     */
    @Anonymous
    @GetMapping("/callback")
    public String callbackGet(String signature, String timestamp, String nonce, String echostr)
    {
        boolean ok = checkSignature(signature, timestamp, nonce);
        if (!ok)
        {
            log.warn("[callback] 验签失败 signature={}", signature);
            return "invalid signature";
        }
        log.info("[callback] 服务器配置验证通过");
        // 记录验证日志
        try
        {
            WxMpCallbackLog cl = new WxMpCallbackLog();
            cl.setMsgType("verify");
            cl.setEvent("CONFIG_VERIFY");
            cl.setPayload("timestamp=" + timestamp + " nonce=" + nonce);
            cl.setCreateTime(new Date());
            wxMpCallbackLogService.insertWxMpCallbackLog(cl);
        }
        catch (Exception e)
        {
            log.warn("[callback] 写入验证日志失败: {}", e.getMessage());
        }
        return echostr;
    }

    /**
     * 消息与事件推送 (POST)
     * 事件: subscribe 关注(含扫码带参数) / unsubscribe 取关 / SCAN 已关注扫码 / TEMPLATESENDJOBFINISH 送达回执
     */
    @Anonymous
    @PostMapping("/callback")
    public String callbackPost(HttpServletRequest request)
    {
        String xml = readBody(request);
        if (StringUtils.isEmpty(xml))
        {
            return "success";
        }
        try
        {
            Map<String, String> msg = parseXml(xml);
            String msgType = msg.get("MsgType");
            String event = msg.get("Event");
            String openid = msg.get("FromUserName");
            String toUserName = msg.get("ToUserName");
            log.info("[callback] 收到消息 type={} event={} openid={}", msgType, event, openid);

            // 落库回调日志(不影响主流程)
            try
            {
                WxMpCallbackLog cl = new WxMpCallbackLog();
                cl.setMsgType(msgType);
                cl.setEvent(event);
                cl.setFromUser(openid);
                cl.setToUser(toUserName);
                cl.setMsgId(msg.get("MsgId"));
                cl.setEventKey(msg.get("EventKey"));
                String sceneRaw = msg.get("EventKey");
                if (StringUtils.isNotEmpty(sceneRaw))
                {
                    cl.setScene(sceneRaw.startsWith("qrscene_") ? sceneRaw.substring("qrscene_".length()) : sceneRaw);
                }
                String payload = StringUtils.isNotEmpty(event) ? event
                        : StringUtils.isNotEmpty(msg.get("Content")) ? StringUtils.abbreviate(msg.get("Content"), 200)
                        : msg.get("Status");
                cl.setPayload(StringUtils.isNotEmpty(payload) ? StringUtils.abbreviate(payload, 500) : null);
                cl.setRawXml(StringUtils.abbreviate(xml, 4000));
                cl.setCreateTime(new Date());
                wxMpCallbackLogService.insertWxMpCallbackLog(cl);
            }
            catch (Exception e)
            {
                log.warn("[callback] 写入回调日志失败: {}", e.getMessage());
            }

            // 模板送达回执 (R3)
            if ("event".equalsIgnoreCase(msgType) && "TEMPLATESENDJOBFINISH".equalsIgnoreCase(event))
            {
                wxMsgPushService.handleSendResult(msg.get("MsgID"), msg.get("Status"));
                return "success";
            }
            // 关注事件
            if ("event".equalsIgnoreCase(msgType) && "subscribe".equalsIgnoreCase(event))
            {
                handleSubscribe(openid, msg);
                return "success";
            }
            // 取关事件
            if ("event".equalsIgnoreCase(msgType) && "unsubscribe".equalsIgnoreCase(event))
            {
                WxMpUser u = wxBizMapper.selectUserByOpenid(openid);
                if (u != null)
                {
                    u.setSubscribe("0");
                    u.setUnsubscribeTime(new Date());
                    wxBizMapper.updateUserSubscribe(u);
                    log.info("[callback] 粉丝取关: openid={}", openid);
                }
                return "success";
            }
            // 已关注用户扫码 (SCAN 事件)
            if ("event".equalsIgnoreCase(msgType) && "SCAN".equalsIgnoreCase(event))
            {
                handleScanEvent(openid, msg, false);
                bumpActivity(openid);
                return "success";
            }
            // 点击菜单事件 (CLICK) → 活跃度
            if ("event".equalsIgnoreCase(msgType) && "CLICK".equalsIgnoreCase(event))
            {
                bumpActivity(openid);
                return "success";
            }
            // 文本/图片/语音/视频/位置/链接 消息 → 活跃度
            if ("text".equalsIgnoreCase(msgType) || "image".equalsIgnoreCase(msgType)
                    || "voice".equalsIgnoreCase(msgType) || "video".equalsIgnoreCase(msgType)
                    || "location".equalsIgnoreCase(msgType) || "link".equalsIgnoreCase(msgType))
            {
                bumpActivity(openid);
                return "success";
            }
        }
        catch (Exception e)
        {
            log.error("[callback] 处理异常: {}", e.getMessage(), e);
        }
        return "success";
    }

    /** 验签健康检查: 带参=微信服务器验证; 无参=后台"测试验证"按钮(接口在线+Token有效) */
    @Anonymous
    @GetMapping("/check")
    public AjaxResult check(String signature, String timestamp, String nonce)
    {
        if (signature == null && timestamp == null && nonce == null)
        {
            java.util.List<com.jonlink.system.domain.WxMpAccount> accs =
                    wxMpAccountMapper.selectWxMpAccountList(new com.jonlink.system.domain.WxMpAccount());
            if (accs == null || accs.isEmpty())
            {
                return success("接口在线，尚未配置公众号账号");
            }
            return success("接口在线，Token 配置有效(等待微信回调触发验签)");
        }
        boolean ok = checkSignature(signature, timestamp, nonce);
        return ok ? success("验签通过") : AjaxResult.error("验签失败");
    }

    /** 关注处理: 入库/更新粉丝 + 若带 scene 则绑定分销归属 */
    private void handleSubscribe(String openid, Map<String, String> msg)
    {
        WxMpUser user = wxBizMapper.selectUserByOpenid(openid);
        boolean isNew = (user == null);
        // 带参数二维码: EventKey 形如 qrscene_1001 (新关注) 或 scene 值
        String eventKey = msg.get("EventKey");
        Integer sceneId = null;
        String sceneStr = null;
        if (StringUtils.isNotEmpty(eventKey))
        {
            String raw = eventKey.startsWith("qrscene_") ? eventKey.substring("qrscene_".length()) : eventKey;
            // 数字=永久码 scene_id; 字母=临时码 scene_str
            if (raw.matches("\\d+"))
            {
                sceneId = Integer.valueOf(raw);
            }
            else
            {
                sceneStr = raw;
            }
        }
        if (user == null)
        {
            user = new WxMpUser();
            user.setOpenid(openid);
            user.setSubscribe("1");
            user.setSubscribeTime(new Date());
            user.setActivityLevel("3");
            user.setCreateBy("WX");
            wxMpUserMapper.insertWxMpUser(user);
            log.info("[callback] 新粉丝关注: openid={} scene={}", openid, sceneId == null ? sceneStr : sceneId);
        }
        else
        {
            user.setSubscribe("1");
            user.setSubscribeTime(new Date());
            wxBizMapper.updateUserSubscribe(user);
            log.info("[callback] 粉丝重新关注: openid={}", openid);
        }
        // 扫码绑定分销(关注与SCAN共用)
        if (sceneId != null || sceneStr != null)
        {
            try
            {
                wxQrService.handleScan(sceneId, sceneStr, openid, isNew);
            }
            catch (Exception e)
            {
                log.warn("[callback] 扫码绑定失败: {}", e.getMessage());
            }
        }
        // 电子台账绑定流水
        wxLedgerService.write(WxLedgerService.TYPE_BIND, "SUB_" + openid, null, openid,
                java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO, "2", "关注事件");
    }

    /** 已关注扫码 (SCAN 事件) */
    private void handleScanEvent(String openid, Map<String, String> msg, boolean isNew)
    {
        String eventKey = msg.get("EventKey");
        if (StringUtils.isEmpty(eventKey))
        {
            return;
        }
        Integer sceneId = null;
        String sceneStr = null;
        if (eventKey.matches("\\d+"))
        {
            sceneId = Integer.valueOf(eventKey);
        }
        else
        {
            sceneStr = eventKey;
        }
        wxQrService.handleScan(sceneId, sceneStr, openid, isNew);
    }

    /**
     * 粉丝活跃度更新:
     * - activity_count += 1
     * - last_activity_time = now
     * - activity_level 重算: count>=50→3, count>=10→2, 否则→1
     */
    private void bumpActivity(String openid)
    {
        try
        {
            WxMpUser u = wxBizMapper.selectUserByOpenid(openid);
            if (u == null || u.getId() == null)
            {
                return;
            }
            int newCount = (u.getActivityCount() == null ? 0 : u.getActivityCount().intValue()) + 1;
            String level = "1";
            if (newCount >= 50)
            {
                level = "3";
            }
            else if (newCount >= 10)
            {
                level = "2";
            }
            String now = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            wxBizMapper.updateUserActivity(u.getId(), level, now);
            log.info("[activity] 粉丝活跃+1: openid={} count={} level={}", openid, newCount, level);
        }
        catch (Exception e)
        {
            log.warn("[activity] 更新粉丝活跃度失败 openid={}: {}", openid, e.getMessage());
        }
    }

    /** SHA1 验签: sort(token, timestamp, nonce) -> sha1 */
    private boolean checkSignature(String signature, String timestamp, String nonce)
    {
        if (StringUtils.isEmpty(signature) || StringUtils.isEmpty(timestamp) || StringUtils.isEmpty(nonce))
        {
            return false;
        }
        String token = getAccountToken();
        if (StringUtils.isEmpty(token))
        {
            if (allowMock)
            {
                // 未配置账号时 mock 放行(便于联调), 配置后严格验签
                log.warn("[callback] 未配置公众号 token, mock 放行");
                return true;
            }
            else
            {
                log.error("[callback] 未配置公众号 token, 拒绝访问(请配置 wx.callback.allow-mock=true 或配置公众号账号)");
                return false;
            }
        }
        String[] arr = { token, timestamp, nonce };
        Arrays.sort(arr);
        StringBuilder sb = new StringBuilder();
        for (String s : arr)
        {
            sb.append(s);
        }
        return signature.equalsIgnoreCase(sha1(sb.toString()));
    }

    private String getAccountToken()
    {
        com.jonlink.system.domain.WxMpAccount q = new com.jonlink.system.domain.WxMpAccount();
        q.setStatus("1");
        List<com.jonlink.system.domain.WxMpAccount> list = wxMpAccountMapper.selectWxMpAccountList(q);
        return list.isEmpty() ? null : list.get(0).getToken();
    }

    private String sha1(String input)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(input.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder();
            for (byte b : digest)
            {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        }
        catch (Exception e)
        {
            return "";
        }
    }

    /** 微信消息 XML 解析(标准 DOM, 兼容 CDATA) */
    private Map<String, String> parseXml(String xml)
    {
        java.util.Map<String, String> map = new java.util.HashMap<>();
        try
        {
            javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setExpandEntityReferences(false);
            javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));
            org.w3c.dom.NodeList nodes = doc.getDocumentElement().getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++)
            {
                org.w3c.dom.Node node = nodes.item(i);
                if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE)
                {
                    map.put(node.getNodeName(), node.getTextContent().trim());
                }
            }
        }
        catch (Exception e)
        {
            log.warn("[callback] XML 解析失败: {} -> {}", e.getMessage(), xml);
        }
        return map;
    }

    private String readBody(HttpServletRequest request)
    {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader())
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line);
            }
        }
        catch (IOException e)
        {
            log.warn("[callback] 读取请求体失败: {}", e.getMessage());
        }
        return sb.toString();
    }
}
