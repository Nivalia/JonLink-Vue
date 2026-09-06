package com.jonlink.web.controller.system;

import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jonlink.common.annotation.Log;
import com.jonlink.common.core.controller.BaseController;
import com.jonlink.common.core.domain.AjaxResult;
import com.jonlink.common.enums.BusinessType;
import com.jonlink.common.utils.SecurityUtils;
import com.jonlink.system.service.IFinClosingService;

/**
 * 期末结转 Controller
 *
 * @author jonlink
 */
@RestController
@RequestMapping("/finance/closing")
public class FinClosingController extends BaseController
{
    @Autowired
    private IFinClosingService closingService;

    /**
     * 结转预览(不生成凭证)
     */
    @PreAuthorize("@ss.hasPermi('finance:closing:query')")
    @GetMapping("/preview/{periodCode}")
    public AjaxResult preview(@PathVariable("periodCode") String periodCode)
    {
        return success(closingService.preview(periodCode));
    }

    /**
     * 执行期末结转
     */
    @PreAuthorize("@ss.hasPermi('finance:closing:execute')")
    @Log(title = "期末结转", businessType = BusinessType.INSERT)
    @PostMapping("/{periodCode}")
    public AjaxResult close(@PathVariable("periodCode") String periodCode)
    {
        Long voucherId = closingService.closePeriod(periodCode, SecurityUtils.getUsername());
        return success(voucherId);
    }

    /**
     * 反结转
     */
    @PreAuthorize("@ss.hasPermi('finance:closing:execute')")
    @Log(title = "期末结转-反结转", businessType = BusinessType.DELETE)
    @DeleteMapping("/{periodCode}")
    public AjaxResult reverse(@PathVariable("periodCode") String periodCode)
    {
        closingService.reverseClose(periodCode, SecurityUtils.getUsername());
        return success();
    }
}
