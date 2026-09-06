package com.jonlink.web.controller.system;

import java.util.ArrayList;
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
import com.jonlink.system.domain.WxH5Page;
import com.jonlink.system.domain.WxMpTemplate;
import com.jonlink.system.service.IWxH5PageService;
import com.jonlink.system.service.IWxMpTemplateService;
import com.jonlink.common.utils.poi.ExcelUtil;
import com.jonlink.common.core.page.TableDataInfo;

/**
 * 模板管理Controller
 * 
 * @author jonlink
 * @date 2026-08-08
 */
@RestController
@RequestMapping("/wx/template")
public class WxMpTemplateController extends BaseController
{
    @Autowired
    private IWxMpTemplateService wxMpTemplateService;

    @Autowired
    private IWxH5PageService wxH5PageService;

    /**
     * 查询模板管理列表
     */
    @PreAuthorize("@ss.hasPermi('wx:template:list')")
    @GetMapping("/list")
    public TableDataInfo list(WxMpTemplate wxMpTemplate)
    {
        startPage();
        List<WxMpTemplate> list = wxMpTemplateService.selectWxMpTemplateList(wxMpTemplate);
        return getDataTable(list);
    }

    /**
     * 导出模板管理列表
     */
    @PreAuthorize("@ss.hasPermi('wx:template:export')")
    @Log(title = "模板管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WxMpTemplate wxMpTemplate)
    {
        List<WxMpTemplate> list = wxMpTemplateService.selectWxMpTemplateList(wxMpTemplate);
        ExcelUtil<WxMpTemplate> util = new ExcelUtil<WxMpTemplate>(WxMpTemplate.class);
        util.exportExcel(response, list, "模板管理数据");
    }

    /**
     * 获取模板管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('wx:template:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wxMpTemplateService.selectWxMpTemplateById(id));
    }

    /**
     * 新增模板管理
     */
    @PreAuthorize("@ss.hasPermi('wx:template:add')")
    @Log(title = "模板管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WxMpTemplate wxMpTemplate)
    {
        return toAjax(wxMpTemplateService.insertWxMpTemplate(wxMpTemplate));
    }

    /**
     * 修改模板管理
     */
    @PreAuthorize("@ss.hasPermi('wx:template:edit')")
    @Log(title = "模板管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WxMpTemplate wxMpTemplate)
    {
        return toAjax(wxMpTemplateService.updateWxMpTemplate(wxMpTemplate));
    }

    /**
     * 删除模板管理
     */
    @PreAuthorize("@ss.hasPermi('wx:template:remove')")
    @Log(title = "模板管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wxMpTemplateService.deleteWxMpTemplateByIds(ids));
    }

    /**
     * 同步模板: 当前为 mock 模式(无真公众号),行为 = 遍历本地模板,对 keyword_meta 为空的
     * 从 content 里的 {{keywordN.DATA}} 占位顺序自动生成默认中文名,供编辑页继续完善。
     */
    @PreAuthorize("@ss.hasPermi('wx:template:edit')")
    @PostMapping("/sync")
    public AjaxResult sync()
    {
        List<WxMpTemplate> all = wxMpTemplateService.selectWxMpTemplateList(new WxMpTemplate());
        int updated = 0;
        for (WxMpTemplate t : all)
        {
            if (t.getKeywordMeta() == null || t.getKeywordMeta().isEmpty())
            {
                String meta = buildDefaultKeywordMeta(t);
                t.setKeywordMeta(meta);
                wxMpTemplateService.updateWxMpTemplate(t);
                updated++;
            }
        }
        return success("同步完成,补全 " + updated + " 条模板的关键词元数据");
    }

    /**
     * 模板可选的 H5 页面下拉(关联链接配置用)
     */
    @GetMapping("/h5PageOptions")
    public AjaxResult h5PageOptions()
    {
        WxH5Page q = new WxH5Page();
        q.setStatus("1");
        List<WxH5Page> pages = wxH5PageService.selectWxH5PageList(q);
        List<java.util.Map<String, Object>> opts = new ArrayList<>();
        for (WxH5Page p : pages)
        {
            java.util.Map<String, Object> o = new java.util.HashMap<>();
            o.put("id", p.getId());
            o.put("pageName", p.getPageName());
            o.put("pagePath", p.getPagePath());
            opts.add(o);
        }
        return success(opts);
    }

    private String buildDefaultKeywordMeta(WxMpTemplate t)
    {
        String content = t.getContent() == null ? "" : t.getContent();
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("\\{\\{(keyword\\d+)\\.DATA\\}\\}").matcher(content);
        java.util.List<String> keys = new java.util.ArrayList<>();
        while (m.find())
        {
            if (!keys.contains(m.group(1)))
            {
                keys.add(m.group(1));
            }
        }
        if (keys.isEmpty())
        {
            return "[]";
        }
        String[] defaultNames = {"标题", "内容", "备注", "时间", "金额", "状态"};
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < keys.size(); i++)
        {
            if (i > 0)
            {
                sb.append(",");
            }
            sb.append("{\"key\":\"").append(keys.get(i)).append("\"");
            sb.append(",\"name\":\"").append(i < defaultNames.length ? defaultNames[i] : "关键词" + (i + 1)).append("\"");
            sb.append(",\"sample\":\"\"");
            sb.append(",\"bizField\":\"\"}");
        }
        sb.append("]");
        return sb.toString();
    }
}
