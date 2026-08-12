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
import com.jonlink.system.domain.WxLedgerItem;
import com.jonlink.system.service.IWxLedgerItemService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 电子台账流水Controller
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@RestController
@RequestMapping("/ledger/wxLedgerItem")
public class WxLedgerItemController extends BaseController
{
    @Autowired
    private IWxLedgerItemService wxLedgerItemService;

    /**
     * 查询电子台账流水列表
     */
    @PreAuthorize("@ss.hasPermi('ledger:wxLedgerItem:list')")
    @GetMapping("/list")
    public TableDataInfo list(WxLedgerItem wxLedgerItem)
    {
        startPage();
        List<WxLedgerItem> list = wxLedgerItemService.selectWxLedgerItemList(wxLedgerItem);
        return getDataTable(list);
    }

    /**
     * 导出电子台账流水列表
     */
    @PreAuthorize("@ss.hasPermi('ledger:wxLedgerItem:export')")
    @Log(title = "电子台账流水", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WxLedgerItem wxLedgerItem)
    {
        List<WxLedgerItem> list = wxLedgerItemService.selectWxLedgerItemList(wxLedgerItem);
        ExcelUtil<WxLedgerItem> util = new ExcelUtil<WxLedgerItem>(WxLedgerItem.class);
        util.exportExcel(response, list, "电子台账流水数据");
    }

    /**
     * 获取电子台账流水详细信息
     */
    @PreAuthorize("@ss.hasPermi('ledger:wxLedgerItem:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wxLedgerItemService.selectWxLedgerItemById(id));
    }

    /**
     * 新增电子台账流水
     */
    @PreAuthorize("@ss.hasPermi('ledger:wxLedgerItem:add')")
    @Log(title = "电子台账流水", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WxLedgerItem wxLedgerItem)
    {
        return toAjax(wxLedgerItemService.insertWxLedgerItem(wxLedgerItem));
    }

    /**
     * 修改电子台账流水
     */
    @PreAuthorize("@ss.hasPermi('ledger:wxLedgerItem:edit')")
    @Log(title = "电子台账流水", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WxLedgerItem wxLedgerItem)
    {
        return toAjax(wxLedgerItemService.updateWxLedgerItem(wxLedgerItem));
    }

    /**
     * 删除电子台账流水
     */
    @PreAuthorize("@ss.hasPermi('ledger:wxLedgerItem:remove')")
    @Log(title = "电子台账流水", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wxLedgerItemService.deleteWxLedgerItemByIds(ids));
    }

    /**
     * 冲正 (README R12 类型6): 原流水金额取负生成反向流水, uk(biz_no+type) 防重
     */
    @PreAuthorize("@ss.hasPermi('ledger:wxLedgerItem:add')")
    @Log(title = "电子台账冲正", businessType = BusinessType.INSERT)
    @PostMapping("/reverse/{id}")
    public AjaxResult reverse(@PathVariable("id") Long id)
    {
        WxLedgerItem src = wxLedgerItemService.selectWxLedgerItemById(id);
        if (src == null)
        {
            return AjaxResult.error("流水不存在: id=" + id);
        }
        String bizNo = src.getBizNo() + "_REV";
        // 幂等: 同一原流水只冲正一次
        WxLedgerItem q = new WxLedgerItem();
        q.setBizNo(bizNo);
        q.setLedgerType("6");
        if (!wxLedgerItemService.selectWxLedgerItemList(q).isEmpty())
        {
            return AjaxResult.error("该流水已冲正过: " + bizNo);
        }
        WxLedgerItem rev = new WxLedgerItem();
        rev.setLedgerNo("REV" + System.currentTimeMillis());
        rev.setLedgerType("6");
        rev.setBizNo(bizNo);
        rev.setPhone(src.getPhone());
        rev.setOpenid(src.getOpenid());
        rev.setAmount(src.getAmount() == null ? null : src.getAmount().negate());
        rev.setPoints(src.getPoints() == null ? null : src.getPoints().negate());
        rev.setDirection("2");
        rev.setBizUser(src.getBizUser());
        rev.setStatus("1");
        rev.setOccurredTime(new java.util.Date());
        rev.setRemark("冲正原流水#" + src.getId() + "(" + src.getBizNo() + ")");
        rev.setCreateBy(getUsername());
        return toAjax(wxLedgerItemService.insertWxLedgerItem(rev));
    }
}
