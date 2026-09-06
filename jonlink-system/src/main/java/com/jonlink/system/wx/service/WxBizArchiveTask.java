package com.jonlink.system.wx.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.jonlink.system.domain.WxBizOrder;
import com.jonlink.system.mapper.WxBizOrderMapper;

/**
 * 核销订单归档定时任务
 * - 每日 02:00 执行:已核销(verify_status=1) + 台账已回写(down_settle_status=1) + 超 30 天
 *   → 挪入 wx_biz_order_archive(同结构),保证主表长期苗条。
 *
 * @author jonlink
 */
@Component
public class WxBizArchiveTask
{
    private static final Logger log = LoggerFactory.getLogger(WxBizArchiveTask.class);
    private static final int ARCHIVE_DAYS = 30;

    @Autowired
    private WxBizOrderMapper wxBizOrderMapper;

    /** 每日 02:00 */
    @Scheduled(cron = "0 0 2 * * ?")
    public void archiveExpired()
    {
        log.info("[archive] 开始归档 {} 天前已核销订单...", ARCHIVE_DAYS);
        try
        {
            // 1. 拉取候选:已核销 + create_time < now - 30d
            List<WxBizOrder> candidates = wxBizOrderMapper.selectArchiveCandidates(ARCHIVE_DAYS);
            if (candidates == null || candidates.isEmpty())
            {
                log.info("[archive] 无需归档");
                return;
            }
            int moved = 0, skipped = 0;
            for (WxBizOrder o : candidates)
            {
                // 2. 检查台账是否已回写(通过 policy_no 查 ledger.down_settle_status)
                int ledgerSettleStatus = wxBizOrderMapper.selectLedgerDownSettleStatusByOrderNo(o.getOrderNo());
                if (ledgerSettleStatus != 1)
                {
                    skipped++;
                    continue;
                }
                // 3. 搬移: INSERT archive(由 moveToArchive)+ DELETE 主表(同事务由 service 注解保证)
                int n = wxBizOrderMapper.moveToArchive(o.getId());
                if (n > 0) {
                    wxBizOrderMapper.deleteWxBizOrderById(o.getId());
                    moved++;
                }
            }
            log.info("[archive] 归档完成: 候选 {} 条,搬移 {} 条,跳过(台账未回写) {} 条", candidates.size(), moved, skipped);
        }
        catch (Exception e)
        {
            log.error("[archive] 归档异常: {}", e.getMessage(), e);
        }
    }
}
