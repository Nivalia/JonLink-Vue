package com.jonlink.system.service.impl;

import com.jonlink.common.exception.ServiceException;
import com.jonlink.system.mapper.FinInitBalanceMapper;
import com.jonlink.system.mapper.FinSubjectMapper;
import com.jonlink.system.service.IFinInitBalanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 期初余额导入服务实现
 *
 * @author jonlink
 */
@Service
public class FinInitBalanceServiceImpl implements IFinInitBalanceService
{
    private static final Logger log = LoggerFactory.getLogger(FinInitBalanceServiceImpl.class);

    @Autowired
    private FinInitBalanceMapper initBalanceMapper;

    @Autowired
    private FinSubjectMapper subjectMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int importInitBalance(List<Map<String, Object>> dataList, String operator)
    {
        if (dataList == null || dataList.isEmpty()) {
            throw new ServiceException("导入数据不能为空");
        }

        int successCount = 0;
        for (Map<String, Object> row : dataList) {
            String subjectCode = (String) row.get("subjectCode");
            if (subjectCode == null || subjectCode.trim().isEmpty()) {
                log.warn("跳过空科目编码行: {}", row);
                continue;
            }

            // 查找科目
            Long subjectId = subjectMapper.selectIdByCode(subjectCode.trim());
            if (subjectId == null) {
                log.warn("科目编码不存在: {}", subjectCode);
                continue;
            }

            // 解析金额
            BigDecimal initDebit = parseBigDecimal(row.get("initDebit"));
            BigDecimal initCredit = parseBigDecimal(row.get("initCredit"));

            // 构造参数
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("subjectId", subjectId);
            params.put("initDebit", initDebit);
            params.put("initCredit", initCredit);
            params.put("operator", operator);

            initBalanceMapper.insertOrUpdateInitBalance(params);
            successCount++;
        }

        log.info("期初余额导入完成: total={}, success={}, operator={}",
                dataList.size(), successCount, operator);
        return successCount;
    }

    @Override
    public List<Map<String, Object>> listInitBalance()
    {
        return initBalanceMapper.listInitBalance();
    }

    private BigDecimal parseBigDecimal(Object value)
    {
        if (value == null) return BigDecimal.ZERO;
        if (value instanceof BigDecimal) return (BigDecimal) value;
        if (value instanceof Number) return BigDecimal.valueOf(((Number) value).doubleValue());
        String str = value.toString().trim();
        if (str.isEmpty()) return BigDecimal.ZERO;
        return new BigDecimal(str);
    }
}
