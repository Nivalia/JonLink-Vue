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
import com.jonlink.system.domain.JonlinkProduct;
import com.jonlink.system.service.IJonlinkProductService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 产品管理Controller
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@RestController
@RequestMapping("/ledger/product")
public class JonlinkProductController extends BaseController
{
    @Autowired
    private IJonlinkProductService jonlinkProductService;
    @Autowired
    private com.jonlink.system.mapper.JonlinkProductMapper jonlinkProductMapper;

    /**
     * 产品上架/下架 (led:product:shelf)
     */
    @PreAuthorize("@ss.hasPermi('ledger:product:shelf')")
    @Log(title = "产品上下架", businessType = BusinessType.UPDATE)
    @PutMapping("/shelf")
    public AjaxResult shelf(@RequestBody com.jonlink.system.domain.JonlinkProduct product)
    {
        if (product.getId() == null)
        {
            return AjaxResult.error("产品ID不能为空");
        }
        com.jonlink.system.domain.JonlinkProduct p = jonlinkProductMapper.selectJonlinkProductById(product.getId());
        if (p == null)
        {
            return AjaxResult.error("产品不存在");
        }
        p.setShelfStatus(product.getShelfStatus());
        p.setUpdateBy(com.jonlink.common.utils.SecurityUtils.getUsername());
        int n = jonlinkProductMapper.updateJonlinkProduct(p);
        return n > 0 ? success("操作成功") : AjaxResult.error("操作失败");
    }

    /**
     * 查询产品管理列表
     */
    @PreAuthorize("@ss.hasPermi('ledger:product:list')")
    @GetMapping("/list")
    public TableDataInfo list(JonlinkProduct jonlinkProduct)
    {
        startPage();
        List<JonlinkProduct> list = jonlinkProductService.selectJonlinkProductList(jonlinkProduct);
        return getDataTable(list);
    }

    /**
     * 导出产品管理列表
     */
    @PreAuthorize("@ss.hasPermi('ledger:product:export')")
    @Log(title = "产品管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JonlinkProduct jonlinkProduct)
    {
        List<JonlinkProduct> list = jonlinkProductService.selectJonlinkProductList(jonlinkProduct);
        ExcelUtil<JonlinkProduct> util = new ExcelUtil<JonlinkProduct>(JonlinkProduct.class);
        util.exportExcel(response, list, "产品管理数据");
    }

    /**
     * 获取产品管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('ledger:product:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(jonlinkProductService.selectJonlinkProductById(id));
    }

    /**
     * 新增产品管理
     */
    @PreAuthorize("@ss.hasPermi('ledger:product:add')")
    @Log(title = "产品管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JonlinkProduct jonlinkProduct)
    {
        return toAjax(jonlinkProductService.insertJonlinkProduct(jonlinkProduct));
    }

    /**
     * 修改产品管理
     */
    @PreAuthorize("@ss.hasPermi('ledger:product:edit')")
    @Log(title = "产品管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JonlinkProduct jonlinkProduct)
    {
        return toAjax(jonlinkProductService.updateJonlinkProduct(jonlinkProduct));
    }

    /**
     * 删除产品管理
     */
    @PreAuthorize("@ss.hasPermi('ledger:product:remove')")
    @Log(title = "产品管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(jonlinkProductService.deleteJonlinkProductByIds(ids));
    }
}
