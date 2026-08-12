package com.jonlink.web.controller.wx;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.jonlink.common.core.domain.AjaxResult;
import com.jonlink.system.domain.SysConfig;

/**
 * R4 域名验证代理 (README §5.3 R4)
 *
 * 微信后台"服务器配置"域名验证会请求 /MP_verify_xxxx.txt。
 * 这里用通配路由代理：文件名在 sys_config 配置
 *   - mp.verify.file  = MP_verify_xxxx.txt (文件名)
 *   - mp.verify.content = 验证内容(微信后台给出的字符串)
 * 未配置时返回 404 (AjaxResult 由全局异常处理, 此处直接写 body)。
 */
@RestController
public class MpVerifyController
{
    private static final Logger log = LoggerFactory.getLogger(MpVerifyController.class);

    @Autowired
    private com.jonlink.system.mapper.SysConfigMapper sysConfigMapper;

    @GetMapping(value = "/MP_verify_{file}.txt")
    public String verify(@PathVariable("file") String file)
    {
        String content = getConfig("mp.verify.content");
        String expectFile = getConfig("mp.verify.file");
        if (content == null || content.isEmpty())
        {
            log.warn("[mp-verify] 未配置 mp.verify.content, 返回空");
            return "";
        }
        // 若配置了文件名且不匹配 → 返回空(微信验证失败)
        if (expectFile != null && !expectFile.isEmpty())
        {
            String expect = expectFile.replace("MP_verify_", "").replace(".txt", "");
            if (!expect.equals(file))
            {
                return "";
            }
        }
        return content;
    }

    private String getConfig(String key)
    {
        try
        {
            SysConfig q = new SysConfig();
            q.setConfigKey(key);
            List<SysConfig> list = sysConfigMapper.selectConfigList(q);
            if (list != null && !list.isEmpty() && list.get(0).getConfigValue() != null)
            {
                return list.get(0).getConfigValue();
            }
        }
        catch (Exception e)
        {
            log.warn("[mp-verify] 读取配置失败 key={}: {}", key, e.getMessage());
        }
        return null;
    }
}
