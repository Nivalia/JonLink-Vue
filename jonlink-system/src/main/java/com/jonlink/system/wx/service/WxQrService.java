package com.jonlink.system.wx.service;

import java.util.Date;
import java.util.Map;
import com.jonlink.common.utils.DateUtils;
import com.jonlink.common.utils.StringUtils;
import com.jonlink.system.domain.WxDistMember;
import com.jonlink.system.domain.WxMpUser;
import com.jonlink.system.domain.WxQrScene;
import com.jonlink.system.mapper.WxBizMapper;
import com.jonlink.system.mapper.WxQrSceneMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 二维码服务 (R6/R7/R11)。
 * - 分销码: 永久码 scene=user_id (R7), 已存在则复用
 * - 活动/通知/公告: 临时码(30天内过期), scene_str=act_xxx
 * - 扫码: 日志入库 + 粉丝分销归属(首次绑定, R11)
 *
 * @author jonlink
 */
@Service
public class WxQrService
{
    private static final Logger log = LoggerFactory.getLogger(WxQrService.class);

    @Autowired
    private WxBizMapper wxBizMapper;
    @Autowired
    private WxQrSceneMapper wxQrSceneMapper;
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private WxLedgerService wxLedgerService;

    /**
     * 获取/创建分销永久码(按分销员 user_id)
     */
    public WxQrScene getOrCreateDistQr(Long userId)
    {
        WxDistMember dist = wxBizMapper.selectDistByUserId(userId);
        if (dist == null)
        {
            throw new RuntimeException("分销员档案不存在: user_id=" + userId);
        }
        // 已有永久码直接复用
        WxQrScene q = new WxQrScene();
        q.setUserId(userId);
        q.setQrType("0");
        java.util.List<WxQrScene> list = wxQrSceneMapper.selectWxQrSceneList(q);
        if (!list.isEmpty() && StringUtils.isNotEmpty(list.get(0).getTicket()))
        {
            return list.get(0);
        }
        // 创建: scene_id = user_id (R7)
        Long sceneId = userId;
        Map<String, Object> r = wxMpService.createPermanentQr(sceneId.intValue());
        if (!Boolean.TRUE.equals(r.get("ok")))
        {
            throw new RuntimeException("创建二维码失败: " + r.get("err"));
        }
        WxQrScene scene = new WxQrScene();
        scene.setSceneId(sceneId);
        scene.setUserId(userId);
        scene.setUserName(dist.getUserName());
        scene.setQrType("0");
        scene.setTicket(String.valueOf(r.get("ticket")));
        scene.setQrUrl(String.valueOf(r.get("qrUrl")));
        scene.setBizType("0");
        scene.setStatus("1");
        scene.setCreateBy("SYSTEM");
        wxQrSceneMapper.insertWxQrScene(scene);
        log.info("[qr] 分销永久码已创建: scene={} user={}", sceneId, dist.getUserName());
        return scene;
    }

    /**
     * 创建临时码(活动/通知/公告)
     */
    public WxQrScene createTempQr(String bizType, String sceneStr, int expireSeconds, String landingUrl)
    {
        Map<String, Object> r = wxMpService.createTempQr(sceneStr, expireSeconds);
        if (!Boolean.TRUE.equals(r.get("ok")))
        {
            throw new RuntimeException("创建临时二维码失败: " + r.get("err"));
        }
        WxQrScene scene = new WxQrScene();
        scene.setSceneId((long) (System.currentTimeMillis() % 100000000));
        scene.setQrType("1");
        scene.setTicket(String.valueOf(r.get("ticket")));
        scene.setQrUrl(String.valueOf(r.get("qrUrl")));
        scene.setBizType(bizType);
        scene.setSceneStr(sceneStr);
        scene.setExpireTime(new Date(System.currentTimeMillis() + expireSeconds * 1000L));
        scene.setLandingUrl(landingUrl);
        scene.setStatus("1");
        scene.setCreateBy("SYSTEM");
        wxQrSceneMapper.insertWxQrScene(scene);
        log.info("[qr] 临时码已创建: sceneStr={} type={}", sceneStr, bizType);
        return scene;
    }

    /**
     * 处理扫码事件(回调触发): 写日志 + 绑定分销归属。
     * R11: 归属首次绑定(无归属才绑), 换绑人工。
     *
     * @param sceneId 永久码 scene 值(分销) 或 null
     * @param sceneStr 临时码 scene_str(活动) 或 null
     * @param openid 扫码用户 openid
     * @param isNewFollow 是否新关注
     */
    @Transactional(rollbackFor = Exception.class)
    public void handleScan(Integer sceneId, String sceneStr, String openid, boolean isNewFollow)
    {
        WxQrScene scene = null;
        if (sceneId != null)
        {
            scene = wxBizMapper.selectQrBySceneId(sceneId);
        }
        else if (StringUtils.isNotEmpty(sceneStr))
        {
            scene = wxBizMapper.selectQrBySceneStr(sceneStr);
        }
        // 0. 状态/过期校验 (R6): 停用码/过期码仍记日志(转化统计), 但跳过绑定与流水; 真实环境在此发模板消息提示
        boolean usable = true;
        if (scene != null && !"1".equals(scene.getStatus()))
        {
            usable = false;
            log.info("[qr] 二维码已停用, 跳过绑定: scene={}", scene.getSceneId());
        }
        if (scene != null && scene.getExpireTime() != null && scene.getExpireTime().before(new Date()))
        {
            usable = false;
            log.info("[qr] 二维码已过期(活动结束), 跳过绑定: scene={} expire={}", scene.getSceneId(), scene.getExpireTime());
        }
        // 1. 扫码日志
        try
        {
            com.jonlink.system.domain.WxQrScanLog scanLog = new com.jonlink.system.domain.WxQrScanLog();
            scanLog.setSceneId(scene == null ? (sceneId == null ? 0 : sceneId) : scene.getSceneId());
            scanLog.setBizType(scene == null ? "0" : scene.getBizType());
            scanLog.setSceneStr(sceneStr);
            scanLog.setOpenid(openid);
            scanLog.setIsNewFollow(isNewFollow ? "1" : "0");
            scanLog.setScanTime(new Date());
            wxBizMapper.insertScanLog(scanLog);
        }
        catch (Exception e)
        {
            log.warn("[qr] 扫码日志写入失败: {}", e.getMessage());
        }
        // 2. 分销归属绑定 (仅永久分销码 且 码可用)
        if (usable && scene != null && "0".equals(scene.getBizType()) && scene.getUserId() != null && scene.getUserId() > 0)
        {
            WxMpUser user = wxBizMapper.selectUserByOpenid(openid);
            if (user != null)
            {
                // R11: 首次绑定(无归属), 换绑人工
                if (user.getDistributorId() == null)
                {
                    wxBizMapper.updateUserDistributor(user.getId(), scene.getUserId(),
                            DateUtils.getTime(), "0");
                    // 电子台账绑定流水
                    wxLedgerService.write(WxLedgerService.TYPE_BIND, "BIND_" + openid, null, openid,
                            java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO, "2", "扫码绑定分销归属");
                    log.info("[qr] 粉丝绑定分销: openid={} distributor={}", openid, scene.getUserId());
                }
                else
                {
                    log.info("[qr] 粉丝已有归属, 跳过绑定: openid={} dist={}", openid, user.getDistributorId());
                }
            }
        }
        // 3. 电子台账扫码流水 (停用/过期码不写业务流水)
        if (usable)
        {
            wxLedgerService.write(WxLedgerService.TYPE_SCAN, "SCAN_" + (sceneId == null ? sceneStr : sceneId),
                    null, openid, java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO, "2", "二维码扫码");
        }
    }
}
