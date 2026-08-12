package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.WxH5PageMapper;
import com.jonlink.system.domain.WxH5Page;
import com.jonlink.system.service.IWxH5PageService;

/**
 * 电子台账流水Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@Service
public class WxH5PageServiceImpl implements IWxH5PageService 
{
    @Autowired
    private WxH5PageMapper wxH5PageMapper;

    /**
     * 查询电子台账流水
     * 
     * @param id 电子台账流水主键
     * @return 电子台账流水
     */
    @Override
    public WxH5Page selectWxH5PageById(Long id)
    {
        return wxH5PageMapper.selectWxH5PageById(id);
    }

    /**
     * 查询电子台账流水列表
     * 
     * @param wxLedgerItem 电子台账流水
     * @return 电子台账流水
     */
    @Override
    public List<WxH5Page> selectWxH5PageList(WxH5Page wxLedgerItem)
    {
        return wxH5PageMapper.selectWxH5PageList(wxLedgerItem);
    }

    /**
     * 新增电子台账流水
     * 
     * @param wxLedgerItem 电子台账流水
     * @return 结果
     */
    @Override
    public int insertWxH5Page(WxH5Page wxLedgerItem)
    {
        wxLedgerItem.setCreateTime(DateUtils.getNowDate());
        return wxH5PageMapper.insertWxH5Page(wxLedgerItem);
    }

    /**
     * 修改电子台账流水
     * 
     * @param wxLedgerItem 电子台账流水
     * @return 结果
     */
    @Override
    public int updateWxH5Page(WxH5Page wxLedgerItem)
    {
        wxLedgerItem.setUpdateTime(DateUtils.getNowDate());
        return wxH5PageMapper.updateWxH5Page(wxLedgerItem);
    }

    /**
     * 批量删除电子台账流水
     * 
     * @param ids 需要删除的电子台账流水主键
     * @return 结果
     */
    @Override
    public int deleteWxH5PageByIds(Long[] ids)
    {
        return wxH5PageMapper.deleteWxH5PageByIds(ids);
    }

    /**
     * 删除电子台账流水信息
     * 
     * @param id 电子台账流水主键
     * @return 结果
     */
    @Override
    public int deleteWxH5PageById(Long id)
    {
        return wxH5PageMapper.deleteWxH5PageById(id);
    }
}
