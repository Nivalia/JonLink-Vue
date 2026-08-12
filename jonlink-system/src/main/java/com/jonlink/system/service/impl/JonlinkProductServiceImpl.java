package com.jonlink.system.service.impl;

import java.util.List;
import com.jonlink.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jonlink.system.mapper.JonlinkProductMapper;
import com.jonlink.system.domain.JonlinkProduct;
import com.jonlink.system.service.IJonlinkProductService;

/**
 * 产品管理Service业务层处理
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@Service
public class JonlinkProductServiceImpl implements IJonlinkProductService 
{
    @Autowired
    private JonlinkProductMapper jonlinkProductMapper;

    /**
     * 查询产品管理
     * 
     * @param id 产品管理主键
     * @return 产品管理
     */
    @Override
    public JonlinkProduct selectJonlinkProductById(Long id)
    {
        return jonlinkProductMapper.selectJonlinkProductById(id);
    }

    /**
     * 查询产品管理列表
     * 
     * @param jonlinkProduct 产品管理
     * @return 产品管理
     */
    @Override
    public List<JonlinkProduct> selectJonlinkProductList(JonlinkProduct jonlinkProduct)
    {
        return jonlinkProductMapper.selectJonlinkProductList(jonlinkProduct);
    }

    /**
     * 新增产品管理
     * 
     * @param jonlinkProduct 产品管理
     * @return 结果
     */
    @Override
    public int insertJonlinkProduct(JonlinkProduct jonlinkProduct)
    {
        jonlinkProduct.setCreateTime(DateUtils.getNowDate());
        return jonlinkProductMapper.insertJonlinkProduct(jonlinkProduct);
    }

    /**
     * 修改产品管理
     * 
     * @param jonlinkProduct 产品管理
     * @return 结果
     */
    @Override
    public int updateJonlinkProduct(JonlinkProduct jonlinkProduct)
    {
        jonlinkProduct.setUpdateTime(DateUtils.getNowDate());
        return jonlinkProductMapper.updateJonlinkProduct(jonlinkProduct);
    }

    /**
     * 批量删除产品管理
     * 
     * @param ids 需要删除的产品管理主键
     * @return 结果
     */
    @Override
    public int deleteJonlinkProductByIds(Long[] ids)
    {
        return jonlinkProductMapper.deleteJonlinkProductByIds(ids);
    }

    /**
     * 删除产品管理信息
     * 
     * @param id 产品管理主键
     * @return 结果
     */
    @Override
    public int deleteJonlinkProductById(Long id)
    {
        return jonlinkProductMapper.deleteJonlinkProductById(id);
    }
}
