package com.jonlink.system.mapper;

import java.util.List;
import com.jonlink.system.domain.JonlinkProduct;

/**
 * 产品管理Mapper接口
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public interface JonlinkProductMapper 
{
    /**
     * 查询产品管理
     * 
     * @param id 产品管理主键
     * @return 产品管理
     */
    public JonlinkProduct selectJonlinkProductById(Long id);

    /**
     * 查询产品管理列表
     * 
     * @param jonlinkProduct 产品管理
     * @return 产品管理集合
     */
    public List<JonlinkProduct> selectJonlinkProductList(JonlinkProduct jonlinkProduct);

    /**
     * 新增产品管理
     * 
     * @param jonlinkProduct 产品管理
     * @return 结果
     */
    public int insertJonlinkProduct(JonlinkProduct jonlinkProduct);

    /**
     * 修改产品管理
     * 
     * @param jonlinkProduct 产品管理
     * @return 结果
     */
    public int updateJonlinkProduct(JonlinkProduct jonlinkProduct);

    /**
     * 删除产品管理
     * 
     * @param id 产品管理主键
     * @return 结果
     */
    public int deleteJonlinkProductById(Long id);

    /**
     * 批量删除产品管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteJonlinkProductByIds(Long[] ids);
}
