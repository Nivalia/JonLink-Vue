package com.jonlink.web.controller.system;

import java.util.List;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jonlink.common.annotation.Log;
import com.jonlink.common.core.controller.BaseController;
import com.jonlink.common.core.domain.AjaxResult;
import com.jonlink.common.enums.BusinessType;
import com.jonlink.common.utils.SecurityUtils;
import com.jonlink.system.service.IFinInitBalanceService;

/**
 * 期初余额导入 Controller
 *
 * @author jonlink
 */
@RestController
@RequestMapping("/finance/initBalance")
public class FinInitBalanceController extends BaseController
{
    @Autowired
    private IFinInitBalanceService initBalanceService;

    /**
     * 查询所有科目的期初余额
     */
    @PreAuthorize("@ss.hasPermi('finance:initBalance:list')")
    @GetMapping("/list")
    public AjaxResult list()
    {
        List<Map<String, Object>> list = initBalanceService.listInitBalance();
        return success(list);
    }

    /**
     * 导入期初余额(Excel数据)
     */
    @PreAuthorize("@ss.hasPermi('finance:initBalance:import')")
    @Log(title = "期初余额导入", businessType = BusinessType.IMPORT)
    @PostMapping("/import")
    public AjaxResult importInitBalance(@RequestBody List<Map<String, Object>> dataList)
    {
        int count = initBalanceService.importInitBalance(dataList, SecurityUtils.getUsername());
        return success("导入成功，共导入 " + count + " 条记录");
    }
}
