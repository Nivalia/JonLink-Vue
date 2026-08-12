package com.jonlink.web.controller.wx;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jonlink.common.annotation.Log;
import com.jonlink.common.core.controller.BaseController;
import com.jonlink.common.core.domain.AjaxResult;
import com.jonlink.common.core.page.TableDataInfo;
import com.jonlink.common.enums.BusinessType;
import com.jonlink.system.domain.WxTag;
import com.jonlink.system.wx.service.IWxTagService;

/**
 * 公众号粉丝标签 Controller (README §12 菜单 2003)
 * 
 * @author jonlink
 */
@RestController
@RequestMapping("/wx/tag")
public class WxTagController extends BaseController
{
    @Autowired
    private IWxTagService wxTagService;

    /**
     * 查询标签列表
     */
    @PreAuthorize("@ss.hasPermi('wx:tag:list')")
    @GetMapping("/list")
    public TableDataInfo list(WxTag wxTag)
    {
        startPage();
        List<WxTag> list = wxTagService.selectWxTagList(wxTag);
        return getDataTable(list);
    }

    /**
     * 获取标签详细
     */
    @PreAuthorize("@ss.hasPermi('wx:tag:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wxTagService.selectWxTagById(id));
    }

    /**
     * 新增标签
     */
    @PreAuthorize("@ss.hasPermi('wx:tag:add')")
    @Log(title = "粉丝标签", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WxTag wxTag)
    {
        return toAjax(wxTagService.insertWxTag(wxTag));
    }

    /**
     * 修改标签
     */
    @PreAuthorize("@ss.hasPermi('wx:tag:edit')")
    @Log(title = "粉丝标签", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WxTag wxTag)
    {
        return toAjax(wxTagService.updateWxTag(wxTag));
    }

    /**
     * 删除标签
     */
    @PreAuthorize("@ss.hasPermi('wx:tag:remove')")
    @Log(title = "粉丝标签", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wxTagService.deleteWxTagByIds(ids));
    }
}
