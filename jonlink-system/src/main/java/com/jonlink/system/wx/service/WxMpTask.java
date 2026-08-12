package com.jonlink.system.wx.service;

import com.jonlink.common.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 先知·智源 定时任务
 * - 每日 00:00 补发超限待发模板消息 (R2 次日补发)
 * - 每日 00:05 生成昨日电子台账日汇总
 *
 * @author jonlink
 */
@Component
public class WxMpTask
{
    private static final Logger log = LoggerFactory.getLogger(WxMpTask.class);

    @Autowired
    private WxMsgPushService wxMsgPushService;
    @Autowired
    private WxLedgerService wxLedgerService;

    /** 每日 00:00 补发昨日超限待发 */
    @Scheduled(cron = "0 0 0 * * ?")
    public void catchUpDaily()
    {
        log.info("[task] 开始每日补发...");
        try
        {
            int done = wxMsgPushService.catchUp();
            log.info("[task] 每日补发完成: {}", done);
        }
        catch (Exception e)
        {
            log.error("[task] 每日补发异常: {}", e.getMessage());
        }
    }

    /** 每日 00:05 台账日汇总(昨日) */
    @Scheduled(cron = "0 5 0 * * ?")
    public void ledgerSummaryDaily()
    {
        log.info("[task] 开始台账日汇总...");
        try
        {
            String yesterday = DateUtils.getDate();
            int n = wxLedgerService.dailySummary(yesterday);
            log.info("[task] 台账日汇总完成: {} 组", n);
        }
        catch (Exception e)
        {
            log.error("[task] 台账日汇总异常: {}", e.getMessage());
        }
    }
}
