/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSON
 *  com.alibaba.fastjson2.JSONObject
 *  com.jonlink.common.annotation.Log
 *  com.jonlink.common.core.controller.BaseController
 *  com.jonlink.common.core.domain.AjaxResult
 *  com.jonlink.common.core.page.TableDataInfo
 *  com.jonlink.common.enums.BusinessType
 *  com.jonlink.common.utils.DateUtils
 *  com.jonlink.common.utils.SecurityUtils
 *  com.jonlink.common.utils.StringUtils
 *  com.jonlink.common.utils.spring.SpringUtils
 *  com.jonlink.system.domain.WxMpSendBatch
 *  com.jonlink.system.domain.WxMpTemplate
 *  com.jonlink.system.domain.WxMpTemplateMsg
 *  com.jonlink.system.domain.WxMpUser
 *  com.jonlink.system.domain.WxQrScene
 *  com.jonlink.system.mapper.WxBizMapper
 *  com.jonlink.system.mapper.WxMpSendBatchMapper
 *  com.jonlink.system.mapper.WxMpTemplateMapper
 *  com.jonlink.system.mapper.WxMpTemplateMsgMapper
 *  com.jonlink.system.wx.service.LedgerSettleService
 *  com.jonlink.system.wx.service.WxDistService
 *  com.jonlink.system.wx.service.WxMpService
 *  com.jonlink.system.wx.service.WxMsgPushService
 *  com.jonlink.system.wx.service.WxQrService
 *  com.jonlink.system.wx.service.WxVerifyService
 *  com.jonlink.web.controller.wx.WxMpBizController$1
 *  jakarta.servlet.http.HttpServletResponse
 *  org.apache.poi.ss.usermodel.Cell
 *  org.apache.poi.ss.usermodel.DateUtil
 *  org.apache.poi.ss.usermodel.Row
 *  org.apache.poi.ss.usermodel.Sheet
 *  org.apache.poi.ss.usermodel.Workbook
 *  org.apache.poi.ss.usermodel.WorkbookFactory
 *  org.apache.poi.xssf.usermodel.XSSFSheet
 *  org.apache.poi.xssf.usermodel.XSSFWorkbook
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.security.access.prepost.PreAuthorize
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 *  org.springframework.web.multipart.MultipartFile
 */
package com.jonlink.web.controller.wx;

import com.jonlink.common.core.domain.entity.SysUser;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.jonlink.common.annotation.Anonymous;
import com.jonlink.common.annotation.Log;
import com.jonlink.common.core.controller.BaseController;
import com.jonlink.common.core.domain.AjaxResult;
import com.jonlink.common.core.page.TableDataInfo;
import com.jonlink.common.enums.BusinessType;
import com.jonlink.common.utils.DateUtils;
import com.jonlink.common.utils.SecurityUtils;
import com.jonlink.common.utils.StringUtils;
import com.jonlink.common.utils.spring.SpringUtils;
import com.jonlink.system.domain.WxMpSendBatch;
import com.jonlink.system.domain.WxMpTemplate;
import com.jonlink.system.domain.WxMpTemplateMsg;
import com.jonlink.system.domain.WxMpUser;
import com.jonlink.system.domain.WxQrScene;
import com.jonlink.system.domain.WxBizOrder;
import com.jonlink.system.domain.JonlinkInsuranceLedger;
import com.jonlink.system.mapper.WxBizMapper;
import com.jonlink.system.mapper.WxMpUserMapper;
import com.jonlink.system.mapper.SysUserMapper;

import com.jonlink.system.mapper.WxMpSendBatchMapper;
import com.jonlink.system.mapper.WxMpTemplateMapper;
import com.jonlink.system.mapper.WxMpTemplateMsgMapper;
import com.jonlink.system.wx.service.LedgerSettleService;
import com.jonlink.system.wx.service.WxDistService;
import com.jonlink.system.wx.service.WxMpService;
import com.jonlink.system.wx.service.WxMsgPushService;
import com.jonlink.system.wx.service.WxQrService;
import com.jonlink.system.wx.service.WxVerifyService;
import com.jonlink.web.controller.wx.WxMpBizController;
import jakarta.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Date;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.jonlink.common.utils.ip.IpUtils;

@RestController
@RequestMapping
public class WxMpBizController
extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(WxMpBizController.class);
    @Autowired
    private WxVerifyService wxVerifyService;
    @Autowired
    private WxQrService wxQrService;
    @Autowired
    private WxMsgPushService wxMsgPushService;
    @Autowired
    private WxDistService wxDistService;
    @Autowired
    private LedgerSettleService ledgerSettleService;
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private WxMpTemplateMapper wxMpTemplateMapper;
    @Autowired
    private WxMpSendBatchMapper wxMpSendBatchMapper;
    @Autowired
    private WxMpTemplateMsgMapper wxMpTemplateMsgMapper;
    @Autowired
    private WxMpUserMapper wxMpUserMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private StringRedisTemplate redis;
    @Autowired
    private com.jonlink.system.mapper.JonlinkInsuranceLedgerMapper ledgerMapper;
    @Autowired
    private com.jonlink.system.mapper.WxBizMapper wxBizMapper;
    @Autowired
    private com.jonlink.system.mapper.WxBizOrderMapper wxBizOrderMapper;

    @PreAuthorize(value="@ss.hasPermi('wx:order:verify')")
    @Log(title="订单核销", businessType=BusinessType.OTHER)
    @PostMapping(value={"/wx/mp/verify"})
    public AjaxResult verify(@RequestBody Map<String, Object> body) {
        String orderNo = String.valueOf(body.getOrDefault("orderNo", ""));
        String phone = body.get("phone") == null ? null : String.valueOf(body.get("phone"));
        String carNo = body.get("carNo") == null ? null : String.valueOf(body.get("carNo"));
        BigDecimal amount = body.get("amount") == null ? null : new BigDecimal(String.valueOf(body.get("amount")));
        String extJson = body.get("extJson") == null ? null : String.valueOf(body.get("extJson"));
        // 限流: 同 orderNo 5s/3次, 同 IP 1s/10次(防刷 + 防重入)
        String clientIp = getCurrentRequestIp();
        if (!checkRateLimit("verify:order:" + orderNo, 3, 5) || !checkRateLimit("verify:ip:" + clientIp, 10, 1)) {
            log.warn("[verify] 触发限流 orderNo={} ip={}", orderNo, clientIp);
            return AjaxResult.error("请求过于频繁,请稍后再试");
        }
        Map r = this.wxVerifyService.verify(orderNo, phone, carNo, amount, extJson);
        return Boolean.TRUE.equals(r.get("ok")) ? this.success(r) : AjaxResult.error((String)(String.valueOf(r.get("code")) + ":" + String.valueOf(r.get("msg"))));
    }

    /**
     * H5 公开核销端点(扫码进入,匿名,无后台权限)。
     * 行为与 /wx/mp/verify 一致,但走 wxVerifyService.verify(同样有 wx_fc_config 必填校验)。
     * 不参与 Redis 限流(防刷放后台,这里只做业务校验)。
     */
    @Anonymous
    @PostMapping(value = { "/wx/h5/verify" })
    public AjaxResult h5Verify(@RequestBody Map<String, Object> body) {
        String orderNo = body.get("orderNo") == null ? "" : String.valueOf(body.get("orderNo"));
        String phone = body.get("phone") == null ? null : String.valueOf(body.get("phone"));
        String carNo = body.get("carNo") == null ? null : String.valueOf(body.get("carNo"));
        BigDecimal amount = body.get("amount") == null ? null : new BigDecimal(String.valueOf(body.get("amount")));
        String extJson = body.get("extJson") == null ? null : String.valueOf(body.get("extJson"));
        Map r = this.wxVerifyService.verify(orderNo, phone, carNo, amount, extJson);
        return Boolean.TRUE.equals(r.get("ok")) ? this.success(r) : AjaxResult.error((String)(String.valueOf(r.get("code")) + ":" + String.valueOf(r.get("msg"))));
    }

    /**
     * 弹窗用: 拉可同步的台账行(未同步过 + 未核销 + 日期区间)
     * GET /wx/order/syncCandidates?startDate=2026-08-01&endDate=2026-08-13
     */
    @GetMapping("/wx/order/syncCandidates")
    public AjaxResult syncCandidates(String startDate, String endDate) {
        JonlinkInsuranceLedger q = new JonlinkInsuranceLedger();
        List<JonlinkInsuranceLedger> list = ledgerMapper.selectJonlinkInsuranceLedgerList(q);
        List<Map<String, Object>> result = new java.util.ArrayList<>();
        java.time.LocalDate s = startDate == null || startDate.isEmpty() ? null : java.time.LocalDate.parse(startDate);
        java.time.LocalDate e = endDate == null || endDate.isEmpty() ? null : java.time.LocalDate.parse(endDate);
        for (JonlinkInsuranceLedger l : list) {
            // 仅未核销 + 在日期区间
            if (!"0".equals(l.getDownSettleStatus())) continue;
            if (l.getLedgerDate() == null) continue;
            java.time.LocalDate d = l.getLedgerDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            if (s != null && d.isBefore(s)) continue;
            if (e != null && d.isAfter(e)) continue;
            // 跳过: 已同步(source_type=2 的 wx_biz_order 已有同 policy_no)
            WxBizOrder exist = wxBizMapper.selectOrderByNo(l.getPolicyNo());
            if (exist != null) continue;
            Map<String, Object> row = new java.util.HashMap<>();
            row.put("id", l.getId());
            row.put("ledgerDate", l.getLedgerDate());
            row.put("policyNo", l.getPolicyNo());
            row.put("applicant", l.getApplicant());
            row.put("premium", l.getPremium());
            row.put("channelName", l.getChannelName());
            row.put("channelType", l.getChannelType());
            row.put("channelRef", l.getChannelRef());
            result.add(row);
        }
        return success(result);
    }

    /**
     * 台账同步 → wx_biz_order (source_type=2)
     * POST /wx/order/syncFromLedger  body: {ledgerIds:[1,2,3]}
     */
    @PreAuthorize(value="@ss.hasPermi('wx:order:add')")
    @Log(title="台账同步订单", businessType=BusinessType.INSERT)
    @PostMapping("/wx/order/syncFromLedger")
    public AjaxResult syncFromLedger(@RequestBody Map<String, Object> body) {
        Object idsObj = body.get("ledgerIds");
        if (!(idsObj instanceof List) || ((List<?>) idsObj).isEmpty()) {
            return AjaxResult.error("ledgerIds 不能为空");
        }
        @SuppressWarnings("unchecked")
        List<Number> ids = (List<Number>) idsObj;
        int success = 0, skip = 0;
        for (Number n : ids) {
            JonlinkInsuranceLedger l = ledgerMapper.selectJonlinkInsuranceLedgerById(n.longValue());
            if (l == null) { skip++; continue; }
            WxBizOrder exist = wxBizMapper.selectOrderByNo(l.getPolicyNo());
            if (exist != null) { skip++; continue; }
            WxBizOrder o = new WxBizOrder();
            o.setOrderNo(l.getPolicyNo());
            o.setPhone(ledgerSettleService.resolveChannelPhone(l));
            o.setCustomerName(l.getApplicant());
            o.setAmount(l.getPremium());
            o.setOrderType("1");
            o.setSourceType("2");
            o.setStatus("0");
            o.setVerifyStatus("0");
            o.setCreateBy(com.jonlink.common.utils.SecurityUtils.getUsername());
            o.setCreateTime(new Date());
            wxBizOrderMapper.insertWxBizOrder(o);
            success++;
        }
        Map<String, Object> r = new HashMap<>();
        r.put("success", success);
        r.put("skip", skip);
        r.put("msg", "同步成功 " + success + " 条,跳过 " + skip + " 条");
        return success(r);
    }

    /** Redis 滑动窗口限流;返回 true=允许, false=超限 */
    private boolean checkRateLimit(String key, int maxCount, int seconds) {
        try {
            String fullKey = "rl:" + key;
            Long cnt = redis.opsForValue().increment(fullKey);
            if (cnt != null && cnt == 1L) {
                redis.expire(fullKey, java.time.Duration.ofSeconds(seconds));
            }
            return cnt == null || cnt <= maxCount;
        } catch (Exception e) {
            log.warn("[rate-limit] 限流检查失败,放行: {}", e.getMessage());
            return true;
        }
    }

    private static String getCurrentRequestIp() {
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest req = attrs.getRequest();
                return IpUtils.getIpAddr(req);
            }
        } catch (Exception ignored) {
        }
        return "unknown";
    }

    @PreAuthorize(value="@ss.hasPermi('wx:qr:add')")
    @PostMapping(value={"/wx/qr/createDist"})
    public AjaxResult createDistQr(@RequestBody Map<String, Object> body) {
        Long userId = Long.valueOf(String.valueOf(body.get("userId")));
        try {
            WxQrScene scene = this.wxQrService.getOrCreateDistQr(userId);
            return this.success(scene);
        }
        catch (RuntimeException e) {
            return AjaxResult.error((String)e.getMessage());
        }
    }

    @PreAuthorize(value="@ss.hasPermi('wx:qr:add')")
    @PostMapping(value={"/wx/qr/createTemp"})
    public AjaxResult createTempQr(@RequestBody Map<String, Object> body) {
        String bizType = String.valueOf(body.getOrDefault("bizType", "1"));
        String sceneStr = String.valueOf(body.getOrDefault("sceneStr", "act_" + System.currentTimeMillis()));
        int expire = body.get("expireSeconds") == null ? 2592000 : Integer.parseInt(String.valueOf(body.get("expireSeconds")));
        String landingUrl = body.get("landingUrl") == null ? null : String.valueOf(body.get("landingUrl"));
        try {
            WxQrScene scene = this.wxQrService.createTempQr(bizType, sceneStr, expire, landingUrl);
            return this.success(scene);
        }
        catch (RuntimeException e) {
            return AjaxResult.error((String)e.getMessage());
        }
    }

    @PreAuthorize(value="@ss.hasPermi('wx:template:add')")
    @PostMapping(value={"/wx/mp/template/sync"})
    public AjaxResult syncTemplate() {
        List<JSONObject> list = this.wxMpService.getAllTemplates();
        if (list.isEmpty()) {
            return AjaxResult.warn((String)"微信未返回模板(未配置账号或账号下无模板)");
        }
        WxBizMapper wxBizMapper = this.getWxBizMapper();
        wxBizMapper.deleteAllTemplate();
        int n = 0;
        for (JSONObject t : list) {
            WxMpTemplate tp = new WxMpTemplate();
            tp.setTemplateId(t.getString("template_id"));
            tp.setTemplateIdShort(t.getString("template_id_short") != null ? t.getString("template_id_short") : "");
            tp.setTitle(t.getString("title"));
            tp.setContent(t.getString("content"));
            tp.setKeywordOrder(t.getString("keyword_id_list") == null ? "" : t.getString("keyword_id_list"));
            tp.setPrimaryIndustry(t.getString("primary_industry"));
            tp.setDeputyIndustry(t.getString("deputy_industry"));
            tp.setExample(t.getString("example"));
            // 从 content 自动提取关键词映射
            String content = t.getString("content") != null ? t.getString("content") : "";
            String example = t.getString("example") != null ? t.getString("example") : "";
            tp.setKeywordMeta(parseKeywordMeta(content, example));
            tp.setStatus("1");
            wxBizMapper.insertTemplate(tp);
            ++n;
        }
        return this.success("同步 " + n + " 个模板");
    }

    @PreAuthorize(value="@ss.hasPermi('wx:template:add')")
    @PostMapping(value={"/wx/template/create"})
    public AjaxResult createTemplate(@RequestBody Map<String, Object> body) {
        String shortId = String.valueOf(body.getOrDefault("templateIdShort", ""));
        String templateId = this.wxMpService.createTemplate(shortId);
        if (templateId == null) {
            return AjaxResult.error((String)"创建模板失败(未配置账号或微信拒绝)");
        }
        return this.success("模板创建成功: " + templateId);
    }

    /**
     * 设置行业 (README §5.2 接口6): industry_id1/industry_id2
     */
    @PreAuthorize("@ss.hasPermi('wx:template:edit')")
    @PostMapping("/wx/template/setIndustry")
    public AjaxResult setIndustry(@RequestBody Map<String, Object> body) {
        String id1 = String.valueOf(body.getOrDefault("industryId1", ""));
        String id2 = String.valueOf(body.getOrDefault("industryId2", ""));
        if (id1.isEmpty() || id2.isEmpty()) {
            return AjaxResult.error("请填写主营行业ID与副营行业ID");
        }
        boolean ok = this.wxMpService.setIndustry(id1, id2);
        return ok ? this.success("行业设置成功") : AjaxResult.error("设置行业失败(未配置账号或微信拒绝)");
    }

    /**
     * 渠道下拉 (README §9.5): type=0自定义 1公众号粉丝 2系统业务员; 缺省返回1+2
     */
    @PreAuthorize("@ss.hasPermi('ledger:ledger:add')")
    @GetMapping("/ledger/channels")
    public AjaxResult channels(@RequestParam(value = "type", required = false) String type) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (type == null || "1".equals(type)) {
            WxMpUser q = new WxMpUser();
            q.setSubscribe("1");
            for (WxMpUser u : this.wxMpUserMapper.selectWxMpUserList(q)) {
                if (u.getPhone() != null && !u.getPhone().isEmpty()) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("type", "1");
                    m.put("label", (u.getNickname() == null ? "" : u.getNickname()) + "(" + u.getPhone() + ")");
                    m.put("phone", u.getPhone());
                    result.add(m);
                }
            }
        }
        if (type == null || "2".equals(type)) {
            for (SysUser u : this.sysUserMapper.selectUserList(new SysUser())) {
                if (u.getPhonenumber() != null && !u.getPhonenumber().isEmpty()) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("type", "2");
                    m.put("label", u.getNickName() + "(" + u.getPhonenumber() + ")");
                    m.put("phone", u.getPhonenumber());
                    result.add(m);
                }
            }
        }
        return this.success(result);
    }

    @PreAuthorize(value="@ss.hasPermi('wx:msg:add')")
    @PostMapping(value={"/wx/msg/send"})
    public AjaxResult sendMsg(@RequestBody Map<String, Object> body) {
        String phone = String.valueOf(body.getOrDefault("phone", ""));
        String templateId = String.valueOf(body.getOrDefault("templateId", ""));
        String bizNo = body.get("bizNo") == null ? null : String.valueOf(body.get("bizNo"));
        Map<String, Object> data = (Map<String, Object>)body.getOrDefault("data", Map.of());
        Map<String, Object> r = this.wxMsgPushService.sendByPhone(phone, templateId, data, null, bizNo, "manual");
        return Boolean.TRUE.equals(r.get("ok")) ? this.success(r) : AjaxResult.error((String)String.valueOf(r.get("msg")));
    }

    /**
     * 核心推送 (README §5.2 接口7): POST /wx/mp/send/sendByPhone
     * 与 /wx/msg/send 等价, 按 README 路由名暴露
     */
    @PreAuthorize(value="@ss.hasPermi('wx:msg:add')")
    @PostMapping(value={"/wx/mp/send/sendByPhone"})
    public AjaxResult sendByPhone(@RequestBody Map<String, Object> body) {
        return sendMsg(body);
    }

    @PreAuthorize(value="@ss.hasPermi('wx:msg:edit')")
    @PostMapping(value={"/wx/msg/retry"})
    public AjaxResult retry() {
        int n = this.wxMsgPushService.retryFailed();
        return this.success("重试完成, 成功 " + n + " 条");
    }

    @PreAuthorize(value="@ss.hasPermi('wx:dist:member:list')")
    @GetMapping(value={"/wx/dist/chain/{userId}"})
    public TableDataInfo chain(@PathVariable Long userId) {
        List chain = this.wxDistService.relationChain(userId);
        return this.getDataTable(chain);
    }

    @GetMapping(value={"/wx/dist/tree/{userId}"})
    public AjaxResult tree(@PathVariable Long userId) {
        return this.success(this.wxDistService.tree(userId));
    }

    @PreAuthorize(value="@ss.hasPermi('led:settle:do')")
    @Log(title="上游结算", businessType=BusinessType.UPDATE)
    @PostMapping(value={"/ledger/settle/up"})
    public AjaxResult settleUp(@RequestBody Map<String, Object> body) {
        Long ledgerId = Long.valueOf(String.valueOf(body.get("ledgerId")));
        Map r = this.ledgerSettleService.settle(ledgerId, "0", SecurityUtils.getUsername());
        return Boolean.TRUE.equals(r.get("ok")) ? this.success(r) : AjaxResult.error((String)(String.valueOf(r.get("code")) + ":" + String.valueOf(r.get("msg"))));
    }

    @PreAuthorize(value="@ss.hasPermi('led:settle:do')")
    @Log(title="下游结算", businessType=BusinessType.UPDATE)
    @PostMapping(value={"/ledger/settle/down"})
    public AjaxResult settleDown(@RequestBody Map<String, Object> body) {
        Long ledgerId = Long.valueOf(String.valueOf(body.get("ledgerId")));
        Map r = this.ledgerSettleService.settle(ledgerId, "1", SecurityUtils.getUsername());
        return Boolean.TRUE.equals(r.get("ok")) ? this.success(r) : AjaxResult.error((String)(String.valueOf(r.get("code")) + ":" + String.valueOf(r.get("msg"))));
    }

    @PreAuthorize(value="@ss.hasPermi('led:dash:view')")
    @GetMapping(value={"/wx/dashboard/stat"})
    public AjaxResult dashboardStat() {
        return this.success(this.getWxBizMapper().dashboardStat(null));
    }

    @PreAuthorize(value="@ss.hasPermi('led:dash:view')")
    @GetMapping(value={"/wx/dashboard/orderTrend"})
    public AjaxResult orderTrend(@RequestParam(defaultValue="14") int days) {
        return this.success(this.getWxBizMapper().dashboardOrderTrend(days < 0 ? 14 : Math.min(days, 365)));
    }

    @PreAuthorize(value="@ss.hasPermi('led:dash:view')")
    @GetMapping(value={"/wx/dashboard/ledgerTrend"})
    public AjaxResult ledgerTrend(@RequestParam(defaultValue="14") int days) {
        return this.success(this.getWxBizMapper().dashboardLedgerByDate(days < 0 ? 14 : Math.min(days, 365)));
    }

    @PreAuthorize(value="@ss.hasPermi('led:dash:view')")
    @GetMapping(value={"/wx/dashboard/map"})
    public AjaxResult mapData(@RequestParam(defaultValue="wx_mp_user") String table, @RequestParam(defaultValue="city") String column) {
        return this.success(this.getWxBizMapper().dashboardMapData(table, column));
    }

    @PreAuthorize(value="@ss.hasPermi('led:dash:view')")
    @GetMapping(value={"/wx/dashboard/mpStat"})
    public AjaxResult mpStat() {
        return this.success(this.getWxBizMapper().dashboardMpStat());
    }

    @PreAuthorize(value="@ss.hasPermi('led:dash:view')")
    @GetMapping(value={"/wx/dashboard/flowTrend"})
    public AjaxResult flowTrend(@RequestParam(defaultValue="30") int days) {
        return this.success(this.getWxBizMapper().dashboardFlowTrend(days < 0 ? 30 : Math.min(days, 365)));
    }

    @PreAuthorize(value="@ss.hasPermi('led:dash:view')")
    @GetMapping(value={"/wx/dashboard/typeDist"})
    public AjaxResult typeDist() {
        return this.success(this.getWxBizMapper().dashboardTypeDist());
    }

    @PreAuthorize(value="@ss.hasPermi('led:dash:view')")
    @GetMapping(value={"/wx/dashboard/recentFlows"})
    public AjaxResult recentFlows(@RequestParam(defaultValue="8") int limit) {
        return this.success(this.getWxBizMapper().dashboardRecentFlows(limit < 1 ? 8 : Math.min(limit, 100)));
    }

    @PreAuthorize(value="@ss.hasPermi('led:dash:view')")
    @GetMapping(value={"/wx/dashboard/scanLogs"})
    public AjaxResult scanLogs(@RequestParam(defaultValue="6") int limit) {
        return this.success(this.getWxBizMapper().dashboardScanLogs(limit < 1 ? 6 : Math.min(limit, 100)));
    }

    @PreAuthorize(value="@ss.hasPermi('led:dash:view')")
    @GetMapping(value={"/wx/dashboard/todos"})
    public AjaxResult todos() {
        return this.success(this.getWxBizMapper().dashboardTodos());
    }

    @PreAuthorize(value="@ss.hasPermi('led:dash:view')")
    @GetMapping(value={"/wx/dashboard/topDistrict"})
    public AjaxResult topDistrict(@RequestParam(defaultValue="5") int limit) {
        return this.success(this.getWxBizMapper().dashboardTopDistrict(limit < 1 ? 5 : Math.min(limit, 100)));
    }

    @PreAuthorize(value="@ss.hasPermi('led:dash:view')")
    @GetMapping(value={"/wx/dashboard/notice"})
    public AjaxResult notice(@RequestParam(defaultValue="3") int limit) {
        return this.success(this.getWxBizMapper().dashboardNotice(limit < 1 ? 3 : Math.min(limit, 50)));
    }

    @PreAuthorize(value="@ss.hasPermi('led:dash:view')")
    @GetMapping(value={"/wx/dashboard/fanSource"})
    public AjaxResult fanSource() {
        return this.success(this.getWxBizMapper().dashboardFanSource());
    }

    @PreAuthorize(value="@ss.hasPermi('led:dash:view')")
    @GetMapping(value={"/wx/dashboard/userPortrait"})
    public AjaxResult userPortrait() {
        return this.success(this.getWxBizMapper().dashboardUserPortrait());
    }

    @PreAuthorize(value="@ss.hasPermi('led:dash:view')")
    @GetMapping(value={"/wx/dashboard/mpMatrix"})
    public AjaxResult mpMatrix() {
        return this.success(this.getWxBizMapper().dashboardMpMatrix());
    }

    @PreAuthorize(value="@ss.hasPermi('wx:user:edit')")
    @PostMapping(value={"/wx/user/sync"})
    public AjaxResult syncFollowers() {
        Map r = this.wxMpService.syncFollowers();
        if (r.containsKey("msg")) {
            return AjaxResult.error((String)String.valueOf(r.get("msg")));
        }
        boolean mock = Boolean.TRUE.equals(r.get("mock"));
        return this.success("同步完成" + (mock ? "(模拟模式)" : "") + "：共 " + String.valueOf(r.get("total")) + " 人，新增 " + String.valueOf(r.get("added")) + "，更新 " + String.valueOf(r.get("updated")));
    }

    @PreAuthorize(value="@ss.hasPermi('wx:user:edit')")
    @PostMapping(value={"/wx/user/syncDistFan"})
    public AjaxResult syncDistFan() {
        try {
            com.jonlink.system.wx.service.DistFanSyncService syncService =
                com.jonlink.common.utils.spring.SpringUtils.getBean(com.jonlink.system.wx.service.DistFanSyncService.class);
            syncService.syncAll();
            return this.success("业务员↔粉丝同步完成");
        } catch (Exception e) {
            return AjaxResult.error("同步失败: " + e.getMessage());
        }
    }

    @PreAuthorize(value="@ss.hasPermi('wx:batch:add')")
    @GetMapping(value={"/wx/mp/send/template"})
    public void downloadTemplate(@RequestParam String templateId, HttpServletResponse response) {
        try {
            int i;
            WxMpTemplate tp = new WxMpTemplate();
            tp.setTemplateId(templateId);
            List tpls = this.wxMpTemplateMapper.selectWxMpTemplateList(tp);
            if (tpls.isEmpty()) {
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write("模板不存在: " + templateId);
                return;
            }
            String[] kws = this.splitKeywords(((WxMpTemplate)tpls.get(0)).getKeywordOrder());
            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("发送模板");
            Row head = sheet.createRow(0);
            head.createCell(0).setCellValue("手机号");
            for (i = 0; i < kws.length; ++i) {
                head.createCell(i + 1).setCellValue(kws[i]);
            }
            sheet.setColumnWidth(0, 5120);
            for (i = 0; i < kws.length; ++i) {
                sheet.setColumnWidth(i + 1, 4608);
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=wx_send_template.xlsx");
            wb.write((OutputStream)response.getOutputStream());
            wb.close();
        }
        catch (Exception e) {
            log.error("[batch] 生成模板失败: {}", (Object)e.getMessage(), (Object)e);
            try {
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write("生成模板失败: " + e.getMessage());
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    @PreAuthorize(value="@ss.hasPermi('wx:batch:add')")
    @Log(title="批次导入", businessType=BusinessType.IMPORT)
    @PostMapping(value={"/wx/mp/send/import"})
    public AjaxResult importBatch(@RequestParam(value="file") MultipartFile file, @RequestParam String templateId) {
        if (file == null || file.isEmpty()) {
            return AjaxResult.error((String)"请上传 Excel 文件");
        }
        try {
            WxMpTemplate tp = new WxMpTemplate();
            tp.setTemplateId(templateId);
            List tpls = this.wxMpTemplateMapper.selectWxMpTemplateList(tp);
            if (tpls.isEmpty()) {
                return AjaxResult.error((String)("模板不存在: " + templateId));
            }
            String[] kws = this.splitKeywords(((WxMpTemplate)tpls.get(0)).getKeywordOrder());
            Workbook wb = WorkbookFactory.create((InputStream)file.getInputStream());
            Sheet sheet = wb.getSheetAt(0);
            String batchNo = "B" + DateUtils.dateTimeNow((String)"yyyyMMddHHmmss") + (int)(Math.random() * 900.0 + 100.0);
            ArrayList<WxMpTemplateMsg> rows = new ArrayList<WxMpTemplateMsg>();
            int rowNo = 0;
            for (int r = 1; r <= sheet.getLastRowNum(); ++r) {
                String phone;
                Cell c0;
                Row row = sheet.getRow(r);
                if (row == null || (c0 = row.getCell(0)) == null || (phone = String.valueOf(this.getCellStr(c0)).trim()).isEmpty()) continue;
                ++rowNo;
                LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
                for (int i = 0; i < kws.length; ++i) {
                    Cell c = row.getCell(i + 1);
                    data.put(kws[i], c == null ? "" : this.getCellStr(c));
                }
                WxMpTemplateMsg m = new WxMpTemplateMsg();
                m.setBatchNo(batchNo);
                m.setTransposeNo(batchNo + "_" + rowNo);
                m.setPhone(phone);
                m.setTemplateId(templateId);
                m.setKeywords(JSON.toJSONString(data));
                m.setStatus("0");
                m.setSource("2");
                m.setBizType("batch_import");
                m.setRowNo(Long.valueOf(rowNo));
                m.setCreateBy(SecurityUtils.getUsername());
                rows.add(m);
            }
            wb.close();
            if (rows.isEmpty()) {
                return AjaxResult.error((String)"Excel 无有效数据行(第1列需为手机号)");
            }
            WxMpSendBatch batch = new WxMpSendBatch();
            batch.setBatchNo(batchNo);
            batch.setTemplateId(templateId);
            batch.setTotal(Long.valueOf(rows.size()));
            batch.setPending(Long.valueOf(rows.size()));
            batch.setSuccess(Long.valueOf(0L));
            batch.setFail(Long.valueOf(0L));
            batch.setOverdue(Long.valueOf(0L));
            batch.setStatus("0");
            batch.setCreateBy(SecurityUtils.getUsername());
            batch.setRemark("Excel 导入 " + rows.size() + " 条");
            this.wxMpSendBatchMapper.insertWxMpSendBatch(batch);
            for (WxMpTemplateMsg m : rows) {
                this.wxMpTemplateMsgMapper.insertWxMpTemplateMsg(m);
            }
            LinkedHashMap<String, Object> ret = new LinkedHashMap<String, Object>();
            ret.put("batchNo", batchNo);
            ret.put("total", rows.size());
            ret.put("pending", rows.size());
            return this.success(ret);
        }
        catch (Exception e) {
            log.error("[batch] 导入失败: {}", (Object)e.getMessage(), (Object)e);
            return AjaxResult.error((String)("导入失败: " + e.getMessage()));
        }
    }

    @PreAuthorize(value="@ss.hasPermi('wx:batch:add')")
    @Log(title="批次创建", businessType=BusinessType.INSERT)
    @PostMapping(value={"/wx/mp/send/batch"})
    public AjaxResult createBatch(@RequestBody Map<String, Object> body) {
        String templateId = String.valueOf(body.getOrDefault("templateId", ""));
        String remark = body.get("remark") == null ? "" : String.valueOf(body.get("remark"));
        List<Map<String, Object>> rowList = (List<Map<String, Object>>)body.getOrDefault("rows", new ArrayList<Map<String, Object>>());
        if (StringUtils.isEmpty((String)templateId) || rowList.isEmpty()) {
            return AjaxResult.error((String)"模板与行数据不能为空");
        }
        String batchNo = "B" + DateUtils.dateTimeNow((String)"yyyyMMddHHmmss") + (int)(Math.random() * 900.0 + 100.0);
        ArrayList<WxMpTemplateMsg> rows = new ArrayList<WxMpTemplateMsg>();
        int rowNo = 0;
        for (Object rowObj : rowList) {
            Map<String, Object> row = (Map<String, Object>)rowObj;
            String phone;
            String string = phone = row.get("phone") == null ? "" : String.valueOf(row.get("phone")).trim();
            if (phone.isEmpty()) continue;
            ++rowNo;
            LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
            for (Map.Entry<String, Object> en : row.entrySet()) {
                if ("phone".equals(en.getKey()) || en.getValue() == null) continue;
                data.put((String)en.getKey(), String.valueOf(en.getValue()));
            }
            WxMpTemplateMsg m = new WxMpTemplateMsg();
            m.setBatchNo(batchNo);
            m.setTransposeNo(batchNo + "_" + rowNo);
            m.setPhone(phone);
            m.setTemplateId(templateId);
            m.setKeywords(JSON.toJSONString(data));
            m.setStatus("0");
            m.setSource("2");
            m.setBizType("batch_json");
            m.setRowNo(Long.valueOf(rowNo));
            m.setCreateBy(SecurityUtils.getUsername());
            rows.add(m);
        }
        if (rows.isEmpty()) {
            return AjaxResult.error((String)"无有效行(需含 phone)");
        }
        WxMpSendBatch batch = new WxMpSendBatch();
        batch.setBatchNo(batchNo);
        batch.setTemplateId(templateId);
        batch.setTotal(Long.valueOf(rows.size()));
        batch.setPending(Long.valueOf(rows.size()));
        batch.setSuccess(Long.valueOf(0L));
        batch.setFail(Long.valueOf(0L));
        batch.setOverdue(Long.valueOf(0L));
        batch.setStatus("0");
        batch.setCreateBy(SecurityUtils.getUsername());
        batch.setRemark(remark);
        this.wxMpSendBatchMapper.insertWxMpSendBatch(batch);
        for (WxMpTemplateMsg m : rows) {
            this.wxMpTemplateMsgMapper.insertWxMpTemplateMsg(m);
        }
        LinkedHashMap<String, Object> ret = new LinkedHashMap<String, Object>();
        ret.put("batchNo", batchNo);
        ret.put("total", rows.size());
        return this.success(ret);
    }

    @PreAuthorize(value="@ss.hasPermi('wx:batch:edit')")
    @Log(title="批次重推", businessType=BusinessType.UPDATE)
    @PostMapping(value={"/wx/mp/send/retry/{batchNo}"})
    public AjaxResult retryBatch(@PathVariable String batchNo) {
        WxMpTemplateMsg q = new WxMpTemplateMsg();
        q.setBatchNo(batchNo);
        List<WxMpTemplateMsg> list = this.wxMpTemplateMsgMapper.selectWxMpTemplateMsgList(q);
        if (list.isEmpty()) {
            return AjaxResult.error((String)("批次不存在或无可重推行: " + batchNo));
        }
        int retried = 0;
        int ok = 0;
        for (WxMpTemplateMsg m : list) {
            Map r;
            WxMpUser u;
            if (!"2".equals(m.getStatus()) && !"3".equals(m.getStatus()) && !"5".equals(m.getStatus()) || "5".equals(m.getStatus()) && (u = ((WxBizMapper)SpringUtils.getBean(WxBizMapper.class)).selectUserByPhone(m.getPhone())) == null) continue;
            ++retried;
            JSONObject data = JSON.parseObject((String)(m.getKeywords() == null ? "{}" : m.getKeywords()));
            try {
                this.wxMpTemplateMsgMapper.deleteWxMpTemplateMsgById(m.getId());
            }
            catch (Exception e) {
                log.warn("[batch] 删除原失败行失败: {}", (Object)e.getMessage());
            }
            if (!Boolean.TRUE.equals((r = this.wxMsgPushService.sendByPhone(m.getPhone(), m.getTemplateId(), (Map)data, m.getUrl(), m.getTransposeNo() + "_R", "batch_retry")).get("ok")) && !"0".equals(r.get("status")) && !"3".equals(r.get("status"))) continue;
            ++ok;
        }
        LinkedHashMap<String, Object> ret = new LinkedHashMap<String, Object>();
        ret.put("batchNo", batchNo);
        ret.put("retried", retried);
        ret.put("ok", ok);
        return this.success(ret);
    }

    private String[] splitKeywords(String keywordOrder) {
        if (StringUtils.isEmpty((String)keywordOrder)) {
            return new String[0];
        }
        String[] arr = keywordOrder.split(",");
        ArrayList<String> out = new ArrayList<String>();
        for (String s : arr) {
            if (!StringUtils.isNotEmpty((String)s.trim())) continue;
            out.add(s.trim());
        }
        return out.toArray(new String[0]);
    }

    private String getCellStr(Cell cell) {
        if (cell == null) {
            return "";
        }
        try {
            switch (cell.getCellType()) {
                case STRING: {
                    return cell.getStringCellValue();
                }
                case NUMERIC: {
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cell.getDateCellValue());
                    }
                    double v = cell.getNumericCellValue();
                    if (v == Math.floor(v) && !Double.isInfinite(v)) {
                        return String.valueOf((long)v);
                    }
                    return String.valueOf(v);
                }
                case BOOLEAN: {
                    return String.valueOf(cell.getBooleanCellValue());
                }
                case FORMULA: {
                    try {
                        return cell.getStringCellValue();
                    }
                    catch (Exception e) {
                        return String.valueOf(cell.getNumericCellValue());
                    }
                }
                default: {
                    return "";
                }
            }
        }
        catch (Exception e) {
            return "";
        }
    }

    /**
     * 从微信模板 content 字段解析关键词映射。
     * content 格式: {{first.DATA}}\n保单号：{{keyword1.DATA}}\n...
     * example 格式: first内容\nkeyword1内容\n...
     */
    private String parseKeywordMeta(String content, String example) {
        java.util.List<java.util.Map<String, String>> kwList = new java.util.ArrayList<>();
        java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\{\\{(\\w+)\\.DATA\\}\\}");
        java.util.regex.Matcher m = p.matcher(content);
        String[] exampleLines = example != null ? example.split("\\r?\\n") : new String[0];
        int exampleIdx = 0;
        while (m.find()) {
            String key = m.group(1);
            int pos = m.start();
            String name = key;
            if (pos > 0) {
                String before = content.substring(0, pos);
                int lastNewline = before.lastIndexOf('\n');
                String line = (lastNewline >= 0 ? before.substring(lastNewline + 1) : before).trim();
                name = line.replaceAll("[：:]+$", "").trim();
                if (name.contains("{{") || name.isEmpty()) name = key;
            }
            String sample = "";
            if ("first".equals(key) || "remark".equals(key)) {
                // first/remark 不加入 kwList，但推进 example 索引
                exampleIdx++;
                continue;
            }
            if (exampleIdx < exampleLines.length) {
                sample = exampleLines[exampleIdx].trim();
                if (sample.contains("：")) sample = sample.substring(sample.indexOf("：") + 1).trim();
            }
            exampleIdx++;
            java.util.Map<String, String> kw = new java.util.HashMap<>();
            kw.put("key", key);
            kw.put("name", name);
            kw.put("sample", sample);
            kwList.add(kw);
        }
        return kwList.isEmpty() ? null : JSON.toJSONString(kwList);
    }

    private WxBizMapper getWxBizMapper() {
        return (WxBizMapper)SpringUtils.getBean(WxBizMapper.class);
    }

    // ==================== Excel 导入核销订单 ====================

    /** Excel 模板列定义(用户后续补充,改这里即可) */
    private static final String[] ORDER_IMPORT_HEADERS = {"订单号", "手机号", "客户名称", "金额", "车牌号"};

    /**
     * 下载导入模板 GET /wx/order/importTemplate
     */
    @GetMapping("/wx/order/importTemplate")
    public void downloadOrderImportTemplate(HttpServletResponse response) {
        try {
            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("核销订单导入");
            Row head = sheet.createRow(0);
            for (int i = 0; i < ORDER_IMPORT_HEADERS.length; i++) {
                head.createCell(i).setCellValue(ORDER_IMPORT_HEADERS[i]);
            }
            // 示例行(可删)
            Row sample = sheet.createRow(1);
            sample.createCell(0).setCellValue("示例订单号(请删除)");
            sample.createCell(1).setCellValue("13800000000");
            sample.createCell(2).setCellValue("张三");
            sample.createCell(3).setCellValue(5000.00);
            sample.createCell(4).setCellValue("冀A12345");
            for (int i = 0; i < ORDER_IMPORT_HEADERS.length; i++) {
                sheet.setColumnWidth(i, 5120);
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=order_import_template.xlsx");
            wb.write(response.getOutputStream());
            wb.close();
        }
        catch (Exception e) {
            log.error("[order-import] 模板下载失败: {}", e.getMessage());
        }
    }

    /**
     * Excel 导入 POST /wx/order/importExcel  multipart: file
     * 返回 {success, failList:[{row, reason}]}
     */
    @PreAuthorize(value="@ss.hasPermi('wx:order:add')")
    @Log(title="订单Excel导入", businessType=BusinessType.IMPORT)
    @PostMapping("/wx/order/importExcel")
    public AjaxResult importOrderExcel(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) return AjaxResult.error("请上传 Excel 文件");
        List<Map<String, Object>> failList = new ArrayList<>();
        int success = 0;
        try (Workbook wb = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = wb.getSheetAt(0);
            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;
                String orderNo = strCell(row.getCell(0));
                String phone = strCell(row.getCell(1));
                String customer = strCell(row.getCell(2));
                String amount = strCell(row.getCell(3));
                String carNo = strCell(row.getCell(4));
                int rowNo = r + 1;
                // 校验
                if (orderNo.isEmpty()) { addFail(failList, rowNo, "订单号为空"); continue; }
                if (phone.isEmpty()) { addFail(failList, rowNo, "手机号为空"); continue; }
                if (!phone.matches("1\\d{10}")) { addFail(failList, rowNo, "手机号格式错误: " + phone); continue; }
                if (amount.isEmpty()) { addFail(failList, rowNo, "金额为空"); continue; }
                try { Double.parseDouble(amount); } catch (Exception e) {
                    addFail(failList, rowNo, "金额格式错误: " + amount); continue;
                }
                // 重复单号
                WxBizOrder exist = wxBizMapper.selectOrderByNo(orderNo);
                if (exist != null) { addFail(failList, rowNo, "订单号已存在: " + orderNo); continue; }
                WxBizOrder o = new WxBizOrder();
                o.setOrderNo(orderNo);
                o.setPhone(phone);
                o.setCustomerName(customer);
                o.setCarNo(carNo.isEmpty() ? null : carNo);
                o.setAmount(new java.math.BigDecimal(amount));
                o.setSourceType("1");
                o.setStatus("0");
                o.setVerifyStatus("0");
                o.setCreateBy(com.jonlink.common.utils.SecurityUtils.getUsername());
                o.setCreateTime(new Date());
                wxBizOrderMapper.insertWxBizOrder(o);
                success++;
            }
            Map<String, Object> r = new HashMap<>();
            r.put("success", success);
            r.put("failCount", failList.size());
            r.put("failList", failList);
            return success(r);
        }
        catch (Exception e) {
            log.error("[order-import] 失败: {}", e.getMessage(), e);
            return AjaxResult.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 导出不合格清单 GET /wx/order/exportFailList?rows=[{row,reason}]
     * 前端把导入返回的 failList 原样回传
     */
    @GetMapping("/wx/order/exportFailList")
    public void exportFailList(@RequestParam(required = false) String rowsJson, HttpServletResponse response) {
        try {
            List<Map> failList = new ArrayList<>();
            if (rowsJson != null && !rowsJson.isEmpty()) {
                failList = com.alibaba.fastjson2.JSON.parseArray(rowsJson, Map.class);
            }
            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("不合格清单");
            Row head = sheet.createRow(0);
            head.createCell(0).setCellValue("行号");
            head.createCell(1).setCellValue("原因");
            int i = 1;
            for (Map f : failList) {
                Row r = sheet.createRow(i++);
                r.createCell(0).setCellValue(String.valueOf(f.get("row")));
                r.createCell(1).setCellValue(String.valueOf(f.get("reason")));
            }
            sheet.setColumnWidth(0, 4096);
            sheet.setColumnWidth(1, 10240);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=order_import_fail.xlsx");
            wb.write(response.getOutputStream());
            wb.close();
        }
        catch (Exception e) {
            log.error("[order-import] 导出不合格清单失败: {}", e.getMessage());
        }
    }

    private String strCell(Cell cell) {
        if (cell == null) return "";
        try {
            switch (cell.getCellType()) {
                case STRING: return cell.getStringCellValue().trim();
                case NUMERIC:
                    double v = cell.getNumericCellValue();
                    if (v == Math.floor(v) && !Double.isInfinite(v)) return String.valueOf((long) v);
                    return String.valueOf(v);
                case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
                default: return "";
            }
        } catch (Exception e) { return ""; }
    }

    private void addFail(List<Map<String, Object>> list, int row, String reason) {
        Map<String, Object> f = new HashMap<>();
        f.put("row", row);
        f.put("reason", reason);
        list.add(f);
    }
}
