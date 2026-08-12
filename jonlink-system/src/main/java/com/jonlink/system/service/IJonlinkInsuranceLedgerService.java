package com.jonlink.system.service;

import java.util.List;
import com.jonlink.system.domain.JonlinkInsuranceLedger;

/**
 * 保险台账Service接口
 * 
 * @author jonlink
 * @date 2026-08-08
 */
public interface IJonlinkInsuranceLedgerService 
{
    /**
     * 查询保险台账
     * 
     * @param id 保险台账主键
     * @return 保险台账
     */
    public JonlinkInsuranceLedger selectJonlinkInsuranceLedgerById(Long id);

    /**
     * 查询保险台账列表
     * 
     * @param jonlinkInsuranceLedger 保险台账
     * @return 保险台账集合
     */
    public List<JonlinkInsuranceLedger> selectJonlinkInsuranceLedgerList(JonlinkInsuranceLedger jonlinkInsuranceLedger);

    /**
     * 新增保险台账
     * 
     * @param jonlinkInsuranceLedger 保险台账
     * @return 结果
     */
    public int insertJonlinkInsuranceLedger(JonlinkInsuranceLedger jonlinkInsuranceLedger);

    /**
     * 修改保险台账
     * 
     * @param jonlinkInsuranceLedger 保险台账
     * @return 结果
     */
    public int updateJonlinkInsuranceLedger(JonlinkInsuranceLedger jonlinkInsuranceLedger);

    /**
     * 批量删除保险台账
     * 
     * @param ids 需要删除的保险台账主键集合
     * @return 结果
     */
    public int deleteJonlinkInsuranceLedgerByIds(Long[] ids);

    /**
     * 删除保险台账信息
     * 
     * @param id 保险台账主键
     * @return 结果
     */
    public int deleteJonlinkInsuranceLedgerById(Long id);
}
