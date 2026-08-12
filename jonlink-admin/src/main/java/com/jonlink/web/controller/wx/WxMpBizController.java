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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @PreAuthorize(value="@ss.hasPermi('wx:order:verify')")
    @Log(title="订单核销", businessType=BusinessType.OTHER)
    @PostMapping(value={"/wx/mp/verify"})
    public AjaxResult verify(@RequestBody Map<String, Object> body) {
        String orderNo = String.valueOf(body.getOrDefault("orderNo", ""));
        String phone = body.get("phone") == null ? null : String.valueOf(body.get("phone"));
        String carNo = body.get("carNo") == null ? null : String.valueOf(body.get("carNo"));
        BigDecimal amount = body.get("amount") == null ? null : new BigDecimal(String.valueOf(body.get("amount")));
        Map r = this.wxVerifyService.verify(orderNo, phone, carNo, amount);
        return Boolean.TRUE.equals(r.get("ok")) ? this.success(r) : AjaxResult.error((String)(String.valueOf(r.get("code")) + ":" + String.valueOf(r.get("msg"))));
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
    @PostMapping(value={"/wx/template/sync"})
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
            tp.setTitle(t.getString("title"));
            tp.setContent(t.getString("content"));
            tp.setKeywordOrder(t.getString("keyword_id_list") == null ? "" : t.getString("keyword_id_list"));
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

    private WxBizMapper getWxBizMapper() {
        return (WxBizMapper)SpringUtils.getBean(WxBizMapper.class);
    }
}
