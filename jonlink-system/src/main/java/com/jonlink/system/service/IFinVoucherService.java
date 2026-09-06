package com.jonlink.system.service;

import java.util.List;
import java.util.Map;
import com.jonlink.system.domain.FinVoucher;
import com.jonlink.system.domain.FinVoucherEntry;

/**
 * 记账凭证Service接口
 *
 * @author jonlink
 */
public interface IFinVoucherService
{
    /**
     * 查询记账凭证
     */
    public FinVoucher selectFinVoucherById(Long id);

    /**
     * 查询记账凭证列表
     */
    public List<FinVoucher> selectFinVoucherList(FinVoucher finVoucher);

    /**
     * 新增记账凭证(单头,无分录)
     */
    public int insertFinVoucher(FinVoucher finVoucher);

    /**
     * 修改记账凭证
     */
    public int updateFinVoucher(FinVoucher finVoucher);

    /**
     * 批量删除记账凭证
     */
    public int deleteFinVoucherByIds(Long[] ids);

    /**
     * 删除记账凭证信息
     */
    public int deleteFinVoucherById(Long id);

    // ===== M1 新增业务方法 =====

    /**
     * 多借多贷完整录入:生成 voucher_no + 校验借贷平衡 + 校验末级科目 + 一次性 insert 头与分录
     *
     * @param voucher       头(含分录 list)
     * @return 新生成 voucher 的 id
     * @throws RuntimeException 校验失败/借贷不平衡/非末级科目
     */
    public Long saveWithEntries(FinVoucher voucher);

    /**
     * 查询凭证的全部分录
     */
    public List<FinVoucherEntry> selectEntriesByVoucherId(Long voucherId);

    /**
     * 审核(0草稿 → 1已审核)
     * 仅 voucher_status='0' 的可审核
     */
    public void audit(Long id, String operator);

    /**
     * 反审核(1已审核 → 0草稿)
     * 不可跨过账后反审核
     */
    public void cancelAudit(Long id, String operator);

    /**
     * 过账(1已审核 → 2已过账)
     * 同时校验借贷平衡
     */
    public void post(Long id, String operator);

    /**
     * 反过账(2已过账 → 1已审核) -- 仅未结账期间可用
     */
    public void unpost(Long id, String operator);

    /**
     * 作废(任意状态 → 3已作废)
     * 期间已结账 → 拒绝
     */
    public void voidVoucher(Long id, String operator, String reason);

    /**
     * 生成下一个凭证号: 记-yyyyMM-NNNN(期间内流水)
     */
    public String generateVoucherNo(String periodCode);

    /**
     * 完整查询(含分录),用于打印预览
     */
    public Map<String, Object> getFullVoucher(Long id);
}