package com.jonlink.system.service;

import java.util.Map;

/**
 * 期末结转服务接口
 * - 损益类科目余额结转到"本年利润"
 * - 收入类科目: 借:收入科目 贷:本年利润
 * - 支出类科目: 借:本年利润 贷:支出科目
 *
 * @author jonlink
 */
public interface IFinClosingService
{
    /**
     * 期末结转
     * @param periodCode 期间编码(yyyyMM)
     * @param operator 操作人
     * @return 生成的凭证ID
     */
    Long closePeriod(String periodCode, String operator);

    /**
     * 反结转(删除结转凭证)
     * @param periodCode 期间编码
     * @param operator 操作人
     */
    void reverseClose(String periodCode, String operator);

    /**
     * 查询结转预览(不实际生成凭证)
     * @param periodCode 期间编码
     * @return 结转明细
     */
    Map<String, Object> preview(String periodCode);
}
