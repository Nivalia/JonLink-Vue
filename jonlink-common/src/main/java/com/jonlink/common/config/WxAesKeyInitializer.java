package com.jonlink.common.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import com.jonlink.common.utils.security.AesUtils;

/**
 * 公众号 AES key 启动初始化器
 * - application.yml -> wx.aes.key(Base64 字符串)为空时自动生成 16 字节随机 key 并 warn 提示修改;
 * - key 长度错误启动失败(避免静默错误)。
 *
 * @author jonlink
 */
@Configuration
@ConfigurationProperties(prefix = "wx.aes")
public class WxAesKeyInitializer
{
    private static final Logger log = LoggerFactory.getLogger(WxAesKeyInitializer.class);

    /** Base64 编码的 AES key(16/24/32 字节) */
    private String key;

    @PostConstruct
    public void init()
    {
        if (key == null || key.trim().isEmpty())
        {
            String generated = AesUtils.generateBase64Key();
            log.warn("[wx-aes] wx.aes.key 未配置,已自动生成随机 key(仅本次启动有效,重启会重新生成): {}", generated);
            log.warn("[wx-aes] 请将以下 key 写入 application.yml -> wx.aes.key 以保持持久化(否则已加密字段重启后无法解密):");
            log.warn("wx.aes.key: {}", generated);
            AesUtils.init(generated);
            return;
        }
        try
        {
            AesUtils.init(key.trim());
            log.info("[wx-aes] AES key 已加载(Base64 长度 {} 字节)", key.trim().length());
        }
        catch (Exception e)
        {
            log.error("[wx-aes] AES key 加载失败: {}", e.getMessage());
            throw e;
        }
    }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
}
