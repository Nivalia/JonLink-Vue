package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.WxH5Page;

/**
 * 电子台账流水Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public interface WxH5PageMapper 
{
    /**
     * 查询电子台账流水
     * 
     * @param id 电子台账流水主键
     * @return 电子台账流水
     */
    public WxH5Page selectWxH5PageById(Long id);

    /**
     * 查询电子台账流水列表
     * 
     * @param wxLedgerItem 电子台账流水
     * @return 电子台账流水集合
     */
    public List<WxH5Page> selectWxH5PageList(WxH5Page wxLedgerItem);

    /**
     * 新增电子台账流水
     * 
     * @param wxLedgerItem 电子台账流水
     * @return 结果
     */
    public int insertWxH5Page(WxH5Page wxLedgerItem);

    /**
     * 修改电子台账流水
     * 
     * @param wxLedgerItem 电子台账流水
     * @return 结果
     */
    public int updateWxH5Page(WxH5Page wxLedgerItem);

    /**
     * 删除电子台账流水
     * 
     * @param id 电子台账流水主键
     * @return 结果
     */
    public int deleteWxH5PageById(Long id);

    /**
     * 批量删除电子台账流水
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWxH5PageByIds(Long[] ids);
}
