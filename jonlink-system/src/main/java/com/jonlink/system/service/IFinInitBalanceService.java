package com.jonlink.system.service;

import java.util.List;
import java.util.Map;

/**
 * 期初余额导入服务接口
 * - 导入Excel期初余额数据
 * - 查询所有科目期初余额
 *
 * @author jonlink
 */
public interface IFinInitBalanceService
{
    /**
     * 导入期初余额(Excel数据)
     * @param dataList Excel解析后的数据列表，每行包含 subjectCode, subjectName, initDebit, initCredit 等
     * @param operator 操作人
     * @return 成功导入的记录数
     */
    int importInitBalance(List<Map<String, Object>> dataList, String operator);

    /**
     * 查询所有科目的期初余额
     * @return 期初余额列表
     */
    List<Map<String, Object>> listInitBalance();
}
