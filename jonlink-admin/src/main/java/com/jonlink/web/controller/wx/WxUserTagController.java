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
import com.jonlink.system.domain.WxUserTag;
import com.jonlink.system.service.IWxUserTagService;

/**
 * 粉丝标签关联Controller
 * 
 * @author jonlink
 * @date 2026-09-05
 */
@RestController
@RequestMapping("/wx/userTag")
public class WxUserTagController extends BaseController
{
    @Autowired
    private IWxUserTagService wxUserTagService;

    /**
     * 查询粉丝标签关联列表
     */
    @PreAuthorize("@ss.hasPermi('wx:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(WxUserTag wxUserTag)
    {
        startPage();
        List<WxUserTag> list = wxUserTagService.selectWxUserTagList(wxUserTag);
        return getDataTable(list);
    }

    /**
     * 获取粉丝标签关联详细信息
     */
    @PreAuthorize("@ss.hasPermi('wx:user:list')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wxUserTagService.selectWxUserTagById(id));
    }

    /**
     * 获取粉丝的所有标签
     */
    @PreAuthorize("@ss.hasPermi('wx:user:list')")
    @GetMapping(value = "/user/{userId}")
    public AjaxResult getTagsByUserId(@PathVariable("userId") Long userId)
    {
        return success(wxUserTagService.selectTagsByUserId(userId));
    }

    /**
     * 新增粉丝标签关联
     */
    @PreAuthorize("@ss.hasPermi('wx:user:edit')")
    @Log(title = "粉丝标签", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WxUserTag wxUserTag)
    {
        return toAjax(wxUserTagService.insertWxUserTag(wxUserTag));
    }

    /**
     * 修改粉丝标签关联
     */
    @PreAuthorize("@ss.hasPermi('wx:user:edit')")
    @Log(title = "粉丝标签", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WxUserTag wxUserTag)
    {
        return toAjax(wxUserTagService.updateWxUserTag(wxUserTag));
    }

    /**
     * 删除粉丝标签关联
     */
    @PreAuthorize("@ss.hasPermi('wx:user:edit')")
    @Log(title = "粉丝标签", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wxUserTagService.deleteWxUserTagByIds(ids));
    }

    /**
     * 保存粉丝标签（先删后增）
     */
    @PreAuthorize("@ss.hasPermi('wx:user:edit')")
    @Log(title = "粉丝标签", businessType = BusinessType.UPDATE)
    @PostMapping("/save/{userId}")
    public AjaxResult saveUserTags(@PathVariable("userId") Long userId, @RequestBody List<Long> tagIds)
    {
        return toAjax(wxUserTagService.saveUserTags(userId, tagIds));
    }
}
