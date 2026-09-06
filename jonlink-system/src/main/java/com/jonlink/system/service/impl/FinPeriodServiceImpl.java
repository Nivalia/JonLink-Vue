package com.jonlink.system.service.impl;

import java.util.List;
import java.util.Map;
import com.jonlink.common.exception.ServiceException;
import com.jonlink.common.utils.DateUtils;
import com.jonlink.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jonlink.system.mapper.FinPeriodMapper;
import com.jonlink.system.mapper.FinVoucherMapper;
import com.jonlink.system.domain.FinPeriod;
import com.jonlink.system.domain.FinVoucher;
import com.jonlink.system.service.IFinPeriodService;

/**
 * 会计期间Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@Service
public class FinPeriodServiceImpl implements IFinPeriodService
{
    @Autowired
    private FinPeriodMapper finPeriodMapper;

    @Autowired
    private FinVoucherMapper finVoucherMapper;

    /**
     * 查询会计期间
     * 
     * @param id 会计期间主键
     * @return 会计期间
     */
    @Override
    public FinPeriod selectFinPeriodById(Long id)
    {
        return finPeriodMapper.selectFinPeriodById(id);
    }

    /**
     * 查询会计期间列表
     * 
     * @param finPeriod 会计期间
     * @return 会计期间
     */
    @Override
    public List<FinPeriod> selectFinPeriodList(FinPeriod finPeriod)
    {
        return finPeriodMapper.selectFinPeriodList(finPeriod);
    }

    /**
     * 新增会计期间
     * 
     * @param finPeriod 会计期间
     * @return 结果
     */
    @Override
    public int insertFinPeriod(FinPeriod finPeriod)
    {
        finPeriod.setCreateTime(DateUtils.getNowDate());
        return finPeriodMapper.insertFinPeriod(finPeriod);
    }

    /**
     * 修改会计期间
     * 
     * @param finPeriod 会计期间
     * @return 结果
     */
    @Override
    public int updateFinPeriod(FinPeriod finPeriod)
    {
        finPeriod.setUpdateTime(DateUtils.getNowDate());
        return finPeriodMapper.updateFinPeriod(finPeriod);
    }

    /**
     * 批量删除会计期间
     * 
     * @param ids 需要删除的会计期间主键
     * @return 结果
     */
    @Override
    public int deleteFinPeriodByIds(Long[] ids)
    {
        return finPeriodMapper.deleteFinPeriodByIds(ids);
    }

    /**
     * 删除会计期间信息
     * 
     * @param id 会计期间主键
     * @return 结果
     */
    @Override
    public int deleteFinPeriodById(Long id)
    {
        return finPeriodMapper.deleteFinPeriodById(id);
    }

    // ===== M1 =====

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closePeriod(Long id, String operator) {
        FinPeriod p = finPeriodMapper.selectFinPeriodById(id);
        if (p == null) throw new ServiceException("期间不存在");
        if ("1".equals(p.getStatus())) throw new ServiceException("期间已结账,无需重复结账");

        // 校验该期间所有凭证 status 必须为 2(已过账)
        // 0草稿/1已审核/3已作废 都视为未"全部过账"
        // 用 FinVoucherMapper 反查(简易: 直接 select 草稿/审核数)
        FinVoucher q = new FinVoucher();
        q.setPeriodCode(p.getPeriodCode());
        List<FinVoucher> list = finVoucherMapper.selectFinVoucherList(q);
        int draft = 0, audited = 0, voided = 0;
        for (FinVoucher v : list) {
            switch (v.getStatus()) {
                case "0": draft++; break;
                case "1": audited++; break;
                case "3": voided++; break;
                default: break;
            }
        }
        if (draft > 0) throw new ServiceException("期间内仍有 " + draft + " 张草稿凭证,请先审核/删除");
        if (audited > 0) throw new ServiceException("期间内仍有 " + audited + " 张已审核未过账凭证,请先过账");
        // voided 不计入(作废等同于未发生)

        FinPeriod upd = new FinPeriod();
        upd.setId(id);
        upd.setStatus("1");
        upd.setCloseUser(operator);
        upd.setCloseTime(DateUtils.getNowDate());
        upd.setUpdateBy(operator);
        upd.setUpdateTime(DateUtils.getNowDate());
        finPeriodMapper.updateFinPeriod(upd);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reopenPeriod(Long id, String operator) {
        FinPeriod p = finPeriodMapper.selectFinPeriodById(id);
        if (p == null) throw new ServiceException("期间不存在");
        if (!"1".equals(p.getStatus())) throw new ServiceException("期间未结账,无需反结账");
        FinPeriod upd = new FinPeriod();
        upd.setId(id);
        upd.setStatus("0");
        upd.setCloseUser(null);
        upd.setCloseTime(null);
        upd.setUpdateBy(operator);
        upd.setUpdateTime(DateUtils.getNowDate());
        finPeriodMapper.updateFinPeriod(upd);
    }
}
