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
import com.jonlink.system.domain.FinSubject;
import com.jonlink.system.service.IFinSubjectService;
import com.jonlink.common.utils.poi.ExcelUtil;

/**
 * 会计科目Controller
 * 
 * @author jonlink
 * @date 2026-08-19
 */
@RestController
@RequestMapping("/finance/subject")
public class FinSubjectController extends BaseController
{
    @Autowired
    private IFinSubjectService finSubjectService;

    /**
     * 查询会计科目列表
     */
    @PreAuthorize("@ss.hasPermi('finance:subject:list')")
    @GetMapping("/list")
    public AjaxResult list(FinSubject finSubject)
    {
        List<FinSubject> list = finSubjectService.selectFinSubjectList(finSubject);
        return success(list);
    }

    /**
     * 导出会计科目列表
     */
    @PreAuthorize("@ss.hasPermi('finance:subject:export')")
    @Log(title = "会计科目", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinSubject finSubject)
    {
        List<FinSubject> list = finSubjectService.selectFinSubjectList(finSubject);
        ExcelUtil<FinSubject> util = new ExcelUtil<FinSubject>(FinSubject.class);
        util.exportExcel(response, list, "会计科目数据");
    }

    /**
     * 获取会计科目详细信息
     */
    @PreAuthorize("@ss.hasPermi('finance:subject:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(finSubjectService.selectFinSubjectById(id));
    }

    /**
     * 新增会计科目
     */
    @PreAuthorize("@ss.hasPermi('finance:subject:add')")
    @Log(title = "会计科目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FinSubject finSubject)
    {
        return toAjax(finSubjectService.insertFinSubject(finSubject));
    }

    /**
     * 修改会计科目
     */
    @PreAuthorize("@ss.hasPermi('finance:subject:edit')")
    @Log(title = "会计科目", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FinSubject finSubject)
    {
        return toAjax(finSubjectService.updateFinSubject(finSubject));
    }

    /**
     * 删除会计科目
     */
    @PreAuthorize("@ss.hasPermi('finance:subject:remove')")
    @Log(title = "会计科目", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(finSubjectService.deleteFinSubjectByIds(ids));
    }
}
