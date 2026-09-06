package com.jonlink.web.controller.system;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jonlink.common.annotation.Log;
import com.jonlink.common.core.controller.BaseController;
import com.jonlink.common.core.domain.AjaxResult;
import com.jonlink.common.enums.BusinessType;
import com.jonlink.system.domain.FinBankAccount;
import com.jonlink.system.service.IFinBankAccountService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 银行账户Controller
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@RestController
@RequestMapping("/finance/account")
public class FinBankAccountController extends BaseController
{
    @Autowired
    private IFinBankAccountService finBankAccountService;

    /**
     * 查询银行账户列表
     */
    @PreAuthorize("@ss.hasPermi('finance:account:list')")
    @GetMapping("/list")
    public TableDataInfo list(FinBankAccount finBankAccount)
    {
        startPage();
        List<FinBankAccount> list = finBankAccountService.selectFinBankAccountList(finBankAccount);
        return getDataTable(list);
    }

    /**
     * 导出银行账户列表
     */
    @PreAuthorize("@ss.hasPermi('finance:account:export')")
    @Log(title = "银行账户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinBankAccount finBankAccount)
    {
        List<FinBankAccount> list = finBankAccountService.selectFinBankAccountList(finBankAccount);
        ExcelUtil<FinBankAccount> util = new ExcelUtil<FinBankAccount>(FinBankAccount.class);
        util.exportExcel(response, list, "银行账户数据");
    }

    /**
     * 获取银行账户详细信息
     */
    @PreAuthorize("@ss.hasPermi('finance:account:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(finBankAccountService.selectFinBankAccountById(id));
    }

    /**
     * 新增银行账户
     */
    @PreAuthorize("@ss.hasPermi('finance:account:add')")
    @Log(title = "银行账户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FinBankAccount finBankAccount)
    {
        return toAjax(finBankAccountService.insertFinBankAccount(finBankAccount));
    }

    /**
     * 修改银行账户
     */
    @PreAuthorize("@ss.hasPermi('finance:account:edit')")
    @Log(title = "银行账户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FinBankAccount finBankAccount)
    {
        return toAjax(finBankAccountService.updateFinBankAccount(finBankAccount));
    }

    /**
     * 删除银行账户
     */
    @PreAuthorize("@ss.hasPermi('finance:account:remove')")
    @Log(title = "银行账户", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(finBankAccountService.deleteFinBankAccountByIds(ids));
    }
}
