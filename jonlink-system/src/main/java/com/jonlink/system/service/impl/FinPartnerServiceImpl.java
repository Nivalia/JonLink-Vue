package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.exception.ServiceException;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.FinPartnerMapper;
import com.jonlink.system.domain.FinPartner;
import com.jonlink.system.service.IFinPartnerService;

/**
 * 往来单位Service业务层处理
 *
 * @author jonlink
 */
@Service
public class FinPartnerServiceImpl implements IFinPartnerService
{
    @Autowired
    private FinPartnerMapper finPartnerMapper;

    @Override
    public FinPartner selectFinPartnerById(Long id)
    {
        return finPartnerMapper.selectFinPartnerById(id);
    }

    @Override
    public List<FinPartner> selectFinPartnerList(FinPartner finPartner)
    {
        return finPartnerMapper.selectFinPartnerList(finPartner);
    }

    @Override
    public int insertFinPartner(FinPartner finPartner)
    {
        finPartner.setCreateTime(DateUtils.getNowDate());
        return finPartnerMapper.insertFinPartner(finPartner);
    }

    @Override
    public int updateFinPartner(FinPartner finPartner)
    {
        finPartner.setUpdateTime(DateUtils.getNowDate());
        return finPartnerMapper.updateFinPartner(finPartner);
    }

    @Override
    public int deleteFinPartnerByIds(Long[] ids)
    {
        return finPartnerMapper.deleteFinPartnerByIds(ids);
    }

    @Override
    public int deleteFinPartnerById(Long id)
    {
        return finPartnerMapper.deleteFinPartnerById(id);
    }

    /**
     * 按 name 精确查
     */
    @Override
    public FinPartner selectByName(String partnerName)
    {
        if (partnerName == null) return null;
        FinPartner q = new FinPartner();
        q.setPartnerName(partnerName);
        List<FinPartner> list = finPartnerMapper.selectFinPartnerList(q);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 自动建档:按 name 匹配存在则返回 id,不存在新建并返回新 id
     */
    @Override
    public Long upsertByName(String partnerName, String partnerType, String refTable, Long refId, String operator)
    {
        if (partnerName == null || partnerName.trim().isEmpty())
            throw new ServiceException("partnerName 不能为空");
        FinPartner exist = selectByName(partnerName.trim());
        if (exist != null) return exist.getId();
        FinPartner p = new FinPartner();
        p.setPartnerName(partnerName.trim());
        p.setPartnerType(partnerType == null ? "0" : partnerType);
        p.setRefTable(refTable);
        p.setRefId(refId);
        p.setStatus("1");
        p.setCreateBy(operator);
        finPartnerMapper.insertFinPartner(p);
        return p.getId();
    }
}