package com.jonlink.web.controller.wx;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.jonlink.common.annotation.Anonymous;
import com.jonlink.common.core.controller.BaseController;
import com.jonlink.common.core.domain.AjaxResult;
import com.jonlink.common.utils.StringUtils;
import com.jonlink.system.wx.service.TicketService;

/**
 * 公众号 H5 端业务接口（粉丝个人中心使用，免登录）。
 *
 * <p>身份验证方案：H5 页面请求必须携带 {@code ticket}（由模板消息推送时
 * 通过 TicketService.create(openid) 生成、24h 过期、openid 隔离）。
 * 后端用 TicketService.resolve(ticket) 取出 openid，作为本次请求的粉丝身份。
 * 直接传 {@code openid=xxx} 不再有效，防止越权。</p>
 *
 * <p>当前阶段为 mock 数据让 H5 端 UI 跑起来，后续替换为 wx_biz_order / wx_ledger_item 真实查询。</p>
 *
 * @author jonlink
 */
@Anonymous
@RestController
@RequestMapping("/wx/h5")
public class WxH5Controller extends BaseController
{
    @Autowired
    private TicketService ticketService;

    /**
     * 校验 ticket 拿 openid；失败返回 null（调用方直接 return error）。
     * 缺 ticket / ticket 无效 / ticket 过期都算失败。
     */
    private String resolveOpenid(String ticket)
    {
        if (StringUtils.isEmpty(ticket))
        {
            return null;
        }
        return ticketService.resolve(ticket);
    }

    /** 我的订单（mock） */
    @GetMapping("/order/list")
    public AjaxResult orderList(@RequestParam(required = false) String ticket,
                                @RequestParam(required = false) Integer limit) {
        String openid = resolveOpenid(ticket);
        if (StringUtils.isEmpty(openid))
        {
            return error("未授权或凭证已过期，请从公众号菜单进入");
        }
        int n = (limit == null || limit < 0) ? 20 : Math.min(limit, 6);
        List<Map<String, Object>> rows = new ArrayList<>();
        String[] types = {"车险", "意健险", "驾意保", "车意险"};
        String[] statuses = {"1", "2", "0", "2"};
        // 用 nanoTime + 随机数避免同一秒内多次调用 orderNo 重复
        long base = System.nanoTime() / 1_000_000L;
        int seed = ThreadLocalRandom.current().nextInt(1000, 9999);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (int i = 0; i < n; i++) {
            Map<String, Object> r = new HashMap<>();
            r.put("id", 1000 + i);
            r.put("orderNo", "ORD" + base + (seed + i));
            r.put("productName", types[i % types.length] + "订单");
            r.put("amount", 300 + i * 150);
            r.put("status", statuses[i % statuses.length]);
            r.put("createTime", sdf.format(new Date(System.currentTimeMillis() - i * 86400000L)));
            rows.add(r);
        }
        return success(rows);
    }

    /** 我的积分流水（mock） */
    @GetMapping("/points/list")
    public AjaxResult pointsList(@RequestParam(required = false) String ticket,
                                 @RequestParam(required = false) Integer limit) {
        String openid = resolveOpenid(ticket);
        if (StringUtils.isEmpty(openid))
        {
            return error("未授权或凭证已过期，请从公众号菜单进入");
        }
        int n = (limit == null || limit < 0) ? 30 : Math.min(limit, 8);
        List<Map<String, Object>> rows = new ArrayList<>();
        String[] titles = {"车险核销奖励", "活动奖励", "推广分销", "签到积分", "兑换消费"};
        long base = System.nanoTime() / 1_000_000L;
        int seed = ThreadLocalRandom.current().nextInt(1000, 9999);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        for (int i = 0; i < n; i++) {
            Map<String, Object> r = new HashMap<>();
            r.put("id", i + 1);
            r.put("title", titles[i % titles.length]);
            r.put("points", (i % 3 == 2) ? 50 + i * 10 : 100 + i * 30);
            r.put("direction", (i % 4 == 3) ? "1" : "0");
            r.put("time", sdf.format(new Date(System.currentTimeMillis() - i * 43200000L)));
            r.put("orderNo", "PNT" + base + (seed + i));
            rows.add(r);
        }
        return success(rows);
    }
}
