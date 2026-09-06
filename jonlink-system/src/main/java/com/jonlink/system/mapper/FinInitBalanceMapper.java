package com.jonlink.system.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * 期初余额Mapper接口
 *
 * @author jonlink
 */
public interface FinInitBalanceMapper
{
    /**
     * 查询所有末级科目的期初余额
     * @return 期初余额列表
     */
    List<Map<String, Object>> listInitBalance();

    /**
     * 按科目编码查询期初余额
     * @param subjectCode 科目编码
     * @return 期初余额
     */
    Map<String, Object> selectBySubjectCode(@Param("subjectCode") String subjectCode);

    /**
     * 新增或更新期初余额
     * @param params 包含 subjectId, initDebit, initCredit, operator 等
     * @return 影响行数
     */
    int insertOrUpdateInitBalance(Map<String, Object> params);

    /**
     * 删除所有期初余额(重新导入时使用)
     * @return 影响行数
     */
    int deleteAllInitBalance();
}
