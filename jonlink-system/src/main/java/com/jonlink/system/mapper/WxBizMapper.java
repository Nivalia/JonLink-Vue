package com.jonlink.system.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.jonlink.system.domain.WxMpUser;
import com.jonlink.system.domain.WxBizOrder;
import com.jonlink.system.domain.WxDistMember;
import com.jonlink.system.domain.WxQrScene;
import com.jonlink.system.domain.WxMpTemplate;
import com.jonlink.system.domain.WxLedgerItem;
import com.jonlink.system.domain.WxDistCommission;
import com.jonlink.system.domain.WxMpTemplateMsg;
import com.jonlink.system.domain.JonlinkInsuranceLedger;
import com.jonlink.system.domain.JonlinkProduct;
import com.jonlink.system.domain.JonlinkSettleRecord;

/**
 * 先知·智源 业务查询统一 Mapper（生成器未覆盖的业务方法）
 */
public interface WxBizMapper
{
    /** 按 openid 查粉丝 */
    WxMpUser selectUserByOpenid(@Param("openid") String openid);

    /** 按手机号查粉丝 */
    WxMpUser selectUserByPhone(@Param("phone") String phone);

    /** 更新粉丝关注状态/时间 */
    int updateUserSubscribe(WxMpUser user);

    /** 更新粉丝手机号 */
    int updateUserPhone(@Param("id") Long id, @Param("phone") String phone);

    /** 按订单号查业务订单 */
    WxBizOrder selectOrderByNo(@Param("orderNo") String orderNo);

    /** 统计某手机号当日已发送条数(限流用) */
    int countMsgTodayByPhone(@Param("phone") String phone, @Param("date") String date);

    /** 按状态统计发送记录 */
    int countMsgByStatus(@Param("status") String status);

    /** 按业务单号精确查发送记录(幂等) */
    WxMpTemplateMsg selectMsgByBizId(@Param("bizId") String bizId);

    /** 按微信 msgid 精确查发送记录(回执) */
    WxMpTemplateMsg selectMsgByMsgId(@Param("msgId") String msgId);

    /** 查超限待发记录(补发用) */
    List<WxMpTemplateMsg> selectMsgForCatchUp(@Param("status") String status, @Param("limit") int limit);

    /** 按场景值查二维码 */
    WxQrScene selectQrBySceneId(@Param("sceneId") Integer sceneId);

    /** 按 scene_str 查临时码 */
    WxQrScene selectQrBySceneStr(@Param("sceneStr") String sceneStr);

    /** 按 user_id 查分销员 */
    WxDistMember selectDistByUserId(@Param("userId") Long userId);

    /** 按模板ID查模板 */
    WxMpTemplate selectTemplateByTplId(@Param("templateId") String templateId);

    /** 台账幂等检查 */
    WxLedgerItem selectLedgerByBizNoType(@Param("bizNo") String bizNo, @Param("ledgerType") String ledgerType);

    /** 插入分销佣金 */
    int insertDistCommission(WxDistCommission commission);

    /** 插入电子台账流水 */
    int insertLedgerItem(WxLedgerItem item);

    /** 插入扫码日志 */
    int insertScanLog(com.jonlink.system.domain.WxQrScanLog log);

    /** 产品联动查询(按id带出冗余字段) */
    JonlinkProduct selectProductJoin(@Param("id") Long id);

    /** 台账页列表联动(带出产品/结算信息, 看板与列表共用) */
    List<Map<String, Object>> selectLedgerJoin(Map<String, Object> params);

    /** 结算幂等检查 */
    JonlinkSettleRecord selectSettleByDirectionLedger(@Param("direction") String direction, @Param("ledgerId") Long ledgerId);

    /** 看板: 总览统计 */
    Map<String, Object> dashboardStat(Map<String, Object> params);

    /** 看板: 订单核销趋势(近N天) */
    List<Map<String, Object>> dashboardOrderTrend(@Param("days") int days);

    /** 看板: 区县分布数据 */
    List<Map<String, Object>> dashboardMapData(@Param("table") String table, @Param("column") String column);

    /** 看板: 台账金额汇总(按日期) */
    List<Map<String, Object>> dashboardLedgerByDate(@Param("days") int days);

    /** 大屏: 公众号总览统计(今日/本月维度) */
    Map<String, Object> dashboardMpStat();

    /** 大屏: 流水趋势(近N天按日) */
    List<Map<String, Object>> dashboardFlowTrend(@Param("days") int days);

    /** 大屏: 台账类型分布 */
    List<Map<String, Object>> dashboardTypeDist();

    /** 大屏: 最近流水 */
    List<Map<String, Object>> dashboardRecentFlows(@Param("limit") int limit);

    /** 大屏: 待办提醒 */
    Map<String, Object> dashboardTodos();

    /** 大屏: 最新扫码动态 */
    List<Map<String, Object>> dashboardScanLogs(@Param("limit") int limit);

    /** 首页看板: 区县粉丝 TOP */
    List<Map<String, Object>> dashboardTopDistrict(@Param("limit") int limit);

    /** 首页看板: 最新系统公告 */
    List<Map<String, Object>> dashboardNotice(@Param("limit") int limit);

    /** H5: 粉丝基础信息 */
    Map<String, Object> h5FanInfo(@Param("openid") String openid);

    /** H5: 粉丝订单统计 */
    Map<String, Object> h5OrderStats(@Param("openid") String openid);

    /** H5: 粉丝订单趋势(近N天) */
    List<Map<String, Object>> h5OrderTrend(@Param("openid") String openid, @Param("days") int days);

    /** H5: 粉丝订单状态分布 */
    List<Map<String, Object>> h5OrderStatusDist(@Param("openid") String openid);

    /** H5: 粉丝最近订单 */
    List<Map<String, Object>> h5RecentOrders(@Param("openid") String openid, @Param("limit") int limit);

    /** H5: 粉丝服务提醒(推送记录) */
    List<Map<String, Object>> h5Reminders(@Param("openid") String openid, @Param("limit") int limit);

    /** 台账日汇总写入 */
    int upsertLedgerSummary(@Param("ledgerDate") String ledgerDate, @Param("ledgerType") String ledgerType,
            @Param("count") int count, @Param("amount") java.math.BigDecimal amount);

    /** 汇总昨日流水 */
    List<Map<String, Object>> summaryGroupByType(@Param("date") String date);

    /** 更新台账结算状态 */
    int updateLedgerSettle(@Param("id") Long id, @Param("direction") String direction,
            @Param("status") String status, @Param("settleNo") String settleNo);

    /** 按保单号查台账(核销回写用) */
    JonlinkInsuranceLedger selectLedgerByPolicyNo(@Param("policyNo") String policyNo);

    /** 更新订单结算状态 */
    int updateOrderSettled(@Param("id") Long id);

    /** 更新订单核销状态 */
    int updateOrderVerify(WxBizOrder order);

    /** 渠道绑定: 按 scene 更新粉丝分销归属 */
    int updateUserDistributor(@Param("id") Long id, @Param("distributorId") Long distributorId,
            @Param("bindTime") String bindTime, @Param("bindSource") String bindSource);

    /** 粉丝活跃度更新(activity_count+1 / last_activity_time / activity_level 重算) */
    int updateUserActivity(@Param("id") Long id, @Param("activityLevel") String activityLevel,
            @Param("now") String now);

    /** 查询分销员下级列表 */
    List<WxDistMember> selectDistChildren(@Param("parentId") Long parentId);

    /** 分销员积分累加 */
    int addDistPoints(@Param("userId") Long userId, @Param("points") java.math.BigDecimal points);

    /** 同步微信模板: 删除本地全部(重建) */
    int deleteAllTemplate();

    /** 插入模板 */
    int insertTemplate(WxMpTemplate template);

    /** 按 order_no 查订单 */
    WxBizOrder selectWxBizOrderByOrderNo(@Param("orderNo") String orderNo);

    /** 粉丝来源统计 */
    List<Map<String, Object>> dashboardFanSource();

    /** 用户画像(性别) */
    Map<String, Object> dashboardUserPortrait();

    /** 公众号矩阵 */
    List<Map<String, Object>> dashboardMpMatrix();
}
