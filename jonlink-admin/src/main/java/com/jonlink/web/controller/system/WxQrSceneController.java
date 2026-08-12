package com.jonlink.web.controller.system;

import java.util.List;
import java.util.Map;
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
import com.jonlink.system.domain.WxQrScene;
import com.jonlink.system.service.IWxQrSceneService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 二维码管理Controller
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@RestController
@RequestMapping("/wx/qr")
public class WxQrSceneController extends BaseController
{
    @Autowired
    private IWxQrSceneService wxQrSceneService;

    /**
     * 查询二维码管理列表
     */
    @PreAuthorize("@ss.hasPermi('wx:qr:list')")
    @GetMapping("/list")
    public TableDataInfo list(WxQrScene wxQrScene)
    {
        startPage();
        List<WxQrScene> list = wxQrSceneService.selectWxQrSceneList(wxQrScene);
        return getDataTable(list);
    }

    /**
     * 导出二维码管理列表
     */
    @PreAuthorize("@ss.hasPermi('wx:qr:export')")
    @Log(title = "二维码管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WxQrScene wxQrScene)
    {
        List<WxQrScene> list = wxQrSceneService.selectWxQrSceneList(wxQrScene);
        ExcelUtil<WxQrScene> util = new ExcelUtil<WxQrScene>(WxQrScene.class);
        util.exportExcel(response, list, "二维码管理数据");
    }

    /**
     * 获取二维码管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('wx:qr:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wxQrSceneService.selectWxQrSceneById(id));
    }

    /**
     * 新增二维码管理
     */
    @PreAuthorize("@ss.hasPermi('wx:qr:add')")
    @Log(title = "二维码管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WxQrScene wxQrScene)
    {
        return toAjax(wxQrSceneService.insertWxQrScene(wxQrScene));
    }

    /**
     * 修改二维码管理
     */
    @PreAuthorize("@ss.hasPermi('wx:qr:edit')")
    @Log(title = "二维码管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WxQrScene wxQrScene)
    {
        return toAjax(wxQrSceneService.updateWxQrScene(wxQrScene));
    }

    /**
     * 停用/启用二维码 (R6: 停用后扫码失效)
     */
    @PreAuthorize("@ss.hasPermi('wx:qr:edit')")
    @Log(title = "二维码管理", businessType = BusinessType.UPDATE)
    @PutMapping("/status/{id}")
    public AjaxResult changeStatus(@PathVariable Long id, @RequestBody Map<String, Object> body)
    {
        String status = body.get("status") == null ? "1" : String.valueOf(body.get("status"));
        WxQrScene scene = new WxQrScene();
        scene.setId(id);
        scene.setStatus(status);
        return toAjax(wxQrSceneService.updateWxQrScene(scene));
    }

    /**
     * 删除二维码管理
     */
    @PreAuthorize("@ss.hasPermi('wx:qr:remove')")
    @Log(title = "二维码管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wxQrSceneService.deleteWxQrSceneByIds(ids));
    }
}
