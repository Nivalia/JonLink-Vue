package com.jonlink.system.wx.service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.jonlink.common.utils.StringUtils;

/**
 * H5 防伪 ticket 服务。
 * <p>
 * 模板消息 url_rule 中的 {ticket} 占位符在推送时替换为一次性防伪凭证：
 * 绑定 openid 存 redis，24h 过期；H5 页面携带 ticket 换取该粉丝个人数据
 * （openid 隔离——转发 ticket 也只能看到原粉丝自己的数据，查不到他人）。
 * <p>
 * 注意：使用 StringRedisTemplate（纯字符串），不走 JonLink 默认 fastjson
 * RedisTemplate（其 value 序列化要求 JSON 格式，纯字符串会反序列化失败）。
 */
@Service
public class TicketService
{
    private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    /** redis key 前缀 */
    private static final String KEY_PREFIX = "h5:ticket:";

    /** 有效期 24h */
    private static final int EXPIRE_HOURS = 24;

    @Autowired
    private StringRedisTemplate redis;

    /**
     * 生成 ticket 并绑定 openid（重复调用对同一 openid 复用已有 ticket）。
     *
     * @param openid 粉丝 openid
     * @return ticket 字符串
     */
    public String create(String openid)
    {
        if (StringUtils.isEmpty(openid))
        {
            return null;
        }
        // 已存在则复用（同一粉丝多消息共用 24h 有效期内同一凭证）
        String existing = redis.opsForValue().get(KEY_PREFIX + openid);
        if (StringUtils.isNotEmpty(existing))
        {
            return existing;
        }
        String ticket = UUID.randomUUID().toString().replace("-", "");
        redis.opsForValue().set(KEY_PREFIX + openid, ticket, EXPIRE_HOURS, TimeUnit.HOURS);
        redis.opsForValue().set(KEY_PREFIX + ticket, openid, EXPIRE_HOURS, TimeUnit.HOURS);
        log.info("[ticket] created openid={} ticket={}", openid, ticket);
        return ticket;
    }

    /**
     * 校验 ticket 并返回绑定的 openid；无效返回 null。
     */
    public String resolve(String ticket)
    {
        if (StringUtils.isEmpty(ticket))
        {
            return null;
        }
        return redis.opsForValue().get(KEY_PREFIX + ticket);
    }

    /**
     * 主动作废（注销/换绑等场景可调用）。
     */
    public void invalidate(String ticket)
    {
        if (StringUtils.isEmpty(ticket))
        {
            return;
        }
        String openid = redis.opsForValue().get(KEY_PREFIX + ticket);
        redis.delete(KEY_PREFIX + ticket);
        if (StringUtils.isNotEmpty(openid))
        {
            redis.delete(KEY_PREFIX + openid);
        }
    }
}
