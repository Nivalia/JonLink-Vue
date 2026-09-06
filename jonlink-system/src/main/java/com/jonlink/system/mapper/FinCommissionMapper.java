package com.jonlink.system.mapper;

import java.util.List;
import java.util.Map;
import com.jonlink.system.domain.FinCommission;

/**
 * 佣金结算Mapper接口
 */
public interface FinCommissionMapper
{
    public FinCommission selectFinCommissionById(Long id);

    public List<FinCommission> selectFinCommissionList(FinCommission commission);

    public int insertFinCommission(FinCommission commission);

    public int updateFinCommission(FinCommission commission);

    public int deleteFinCommissionById(Long id);

    public int deleteFinCommissionByIds(Long[] ids);

    /** 按保单号查询 */
    public List<FinCommission> selectByPolicyNo(String policyNo);

    /** 汇总佣金 */
    public Map<String, Object> summary(String startDate, String endDate, String direction);
}
