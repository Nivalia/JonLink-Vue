package com.jonlink.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.jonlink.system.domain.WxBizOrder;

/**
 * 核销管理Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public interface WxBizOrderMapper 
{
    /**
     * 查询核销管理
     * 
     * @param id 核销管理主键
     * @return 核销管理
     */
    public WxBizOrder selectWxBizOrderById(Long id);

    /**
     * 查询核销管理列表
     * 
     * @param wxBizOrder 核销管理
     * @return 核销管理集合
     */
    public List<WxBizOrder> selectWxBizOrderList(WxBizOrder wxBizOrder);

    /**
     * 新增核销管理
     * 
     * @param wxBizOrder 核销管理
     * @return 结果
     */
    public int insertWxBizOrder(WxBizOrder wxBizOrder);

    /**
     * 修改核销管理
     * 
     * @param wxBizOrder 核销管理
     * @return 结果
     */
    public int updateWxBizOrder(WxBizOrder wxBizOrder);

    /**
     * 删除核销管理
     * 
     * @param id 核销管理主键
     * @return 结果
     */
    public int deleteWxBizOrderById(Long id);

    /**
     * 批量删除核销管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWxBizOrderByIds(Long[] ids);

    /** 归档候选: verify_status=1 + create_time < now - N天 */
    List<WxBizOrder> selectArchiveCandidates(@Param("days") int days);

    /** 查台账下游结费状态(归档判断): 0=未结, 1=已结, -1=台账无此单 */
    int selectLedgerDownSettleStatusByOrderNo(@Param("orderNo") String orderNo);

    /** 搬移: INSERT archive + DELETE 主表(由 xml 实现) */
    int moveToArchive(@Param("id") Long id);
}
