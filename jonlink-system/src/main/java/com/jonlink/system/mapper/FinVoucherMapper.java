package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.FinVoucher;

/**
 * 记账凭证Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-19
 */
public interface FinVoucherMapper 
{
    /**
     * 查询记账凭证
     * 
     * @param id 记账凭证主键
     * @return 记账凭证
     */
    public FinVoucher selectFinVoucherById(Long id);

    /**
     * 查询记账凭证列表
     * 
     * @param finVoucher 记账凭证
     * @return 记账凭证集合
     */
    public List<FinVoucher> selectFinVoucherList(FinVoucher finVoucher);

    /**
     * 新增记账凭证
     * 
     * @param finVoucher 记账凭证
     * @return 结果
     */
    public int insertFinVoucher(FinVoucher finVoucher);

    /**
     * 修改记账凭证
     * 
     * @param finVoucher 记账凭证
     * @return 结果
     */
    public int updateFinVoucher(FinVoucher finVoucher);

    /**
     * 删除记账凭证
     * 
     * @param id 记账凭证主键
     * @return 结果
     */
    public int deleteFinVoucherById(Long id);

    /**
     * 批量删除记账凭证
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinVoucherByIds(Long[] ids);

    /**
     * 查询指定前缀的最大流水号(MAX(SUBSTRING(voucher_no, -4)))
     * prefix 例: "记-202608-" → 返回该期间内最大的 NNNN 数字(无则 null)
     */
    public Integer selectMaxVoucherNoSeq(String prefix);

    /** 报表:期间内按 subject_id 聚合 debit/credit */
    public java.util.List<java.util.Map<String, Object>> sumEntriesBySubjectInPeriod(String periodCode);

    /** 报表:单科目单期间发生额 */
    public java.math.BigDecimal sumBySubjectPeriod(Long subjectId, String periodCode);

    /** 报表:单科目单期间借方/贷方分别聚合 */
    public java.util.Map<String, Object> sumDebitCreditBySubjectPeriod(Long subjectId, String periodCode);

    /** 报表:单科目期末余额(借方 - 贷方 或 贷方 - 借方) */
    public java.math.BigDecimal getSubjectBalance(Long subjectId, String periodCode);

    /** 报表:台账业绩按渠道/险种/月份聚合 */
    public java.util.List<java.util.Map<String, Object>> sumLedgerPerformance(String startDate, String endDate);
}
