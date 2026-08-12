package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.WxLedgerItemMapper;
import com.jonlink.system.domain.WxLedgerItem;
import com.jonlink.system.service.IWxLedgerItemService;

/**
 * 电子台账流水Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@Service
public class WxLedgerItemServiceImpl implements IWxLedgerItemService 
{
    @Autowired
    private WxLedgerItemMapper wxLedgerItemMapper;

    /**
     * 查询电子台账流水
     * 
     * @param id 电子台账流水主键
     * @return 电子台账流水
     */
    @Override
    public WxLedgerItem selectWxLedgerItemById(Long id)
    {
        return wxLedgerItemMapper.selectWxLedgerItemById(id);
    }

    /**
     * 查询电子台账流水列表
     * 
     * @param wxLedgerItem 电子台账流水
     * @return 电子台账流水
     */
    @Override
    public List<WxLedgerItem> selectWxLedgerItemList(WxLedgerItem wxLedgerItem)
    {
        return wxLedgerItemMapper.selectWxLedgerItemList(wxLedgerItem);
    }

    /**
     * 新增电子台账流水
     * 
     * @param wxLedgerItem 电子台账流水
     * @return 结果
     */
    @Override
    public int insertWxLedgerItem(WxLedgerItem wxLedgerItem)
    {
        wxLedgerItem.setCreateTime(DateUtils.getNowDate());
        return wxLedgerItemMapper.insertWxLedgerItem(wxLedgerItem);
    }

    /**
     * 修改电子台账流水
     * 
     * @param wxLedgerItem 电子台账流水
     * @return 结果
     */
    @Override
    public int updateWxLedgerItem(WxLedgerItem wxLedgerItem)
    {
        wxLedgerItem.setUpdateTime(DateUtils.getNowDate());
        return wxLedgerItemMapper.updateWxLedgerItem(wxLedgerItem);
    }

    /**
     * 批量删除电子台账流水
     * 
     * @param ids 需要删除的电子台账流水主键
     * @return 结果
     */
    @Override
    public int deleteWxLedgerItemByIds(Long[] ids)
    {
        return wxLedgerItemMapper.deleteWxLedgerItemByIds(ids);
    }

    /**
     * 删除电子台账流水信息
     * 
     * @param id 电子台账流水主键
     * @return 结果
     */
    @Override
    public int deleteWxLedgerItemById(Long id)
    {
        return wxLedgerItemMapper.deleteWxLedgerItemById(id);
    }
}
