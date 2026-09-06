package com.jonlink.common.utils.security;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import com.jonlink.common.utils.StringUtils;

/**
 * AES/GCM/NoPadding 加解密工具 (推荐) + 旧 AES/CBC/PKCS5Padding 兼容
 *
 * [AUDIT-2026-09-04] 原实现使用 AES/CBC + 固定 IV,易受 Padding Oracle 攻击;改为 AES/GCM
 * - GCM 同时提供机密性 + 完整性(无 padding oracle)
 * - IV 每次随机 12 字节,前缀编码到密文前
 * - 旧 CBC 密文(以 "AES:" 开头)用 legacyDecrypt 解密,保证历史数据可读
 * - 新数据用 encrypt() 加密,自动 GCM
 *
 * 密文格式:
 *   新(GCM): "AES-GCM:" + Base64(IV 12B || ciphertext || tag 16B)
 *   旧(CBC): "AES:" + Base64(ciphertext)
 *
 * key 16/24/32 字节(对应 AES-128/192/256)
 */
public class AesUtils
{
    private static final String ALGO = "AES";
    private static final String TRANSFORM_GCM = "AES/GCM/NoPadding";
    private static final String TRANSFORM_CBC = "AES/CBC/PKCS5Padding";
    private static final String PREFIX_GCM = "AES-GCM:";
    private static final String PREFIX_CBC = "AES:";
    private static final int GCM_TAG_BITS = 128;
    private static final int GCM_IV_BYTES = 12;
    /** 启动时由 WxAesKeyInitializer 注入 */
    private static volatile byte[] keyBytes;
    private static volatile byte[] ivBytes;

    /** 由启动器注入 key(Base64 字符串) */
    public static void init(String base64Key)
    {
        if (base64Key == null || base64Key.isEmpty())
        {
            throw new IllegalArgumentException("wx.aes.key 不能为空");
        }
        byte[] k = Base64.getDecoder().decode(base64Key);
        if (k.length != 16 && k.length != 24 && k.length != 32)
        {
            throw new IllegalArgumentException("wx.aes.key 长度必须为 16/24/32 字节(当前 " + k.length + ")");
        }
        keyBytes = k;
        ivBytes = new byte[k.length];
        System.arraycopy(k, 0, ivBytes, 0, k.length);
    }

    /** 生成 32 字节(AES-256)随机 key 并 Base64 编码 */
    public static String generateBase64Key()
    {
        byte[] k = new byte[32];
        new SecureRandom().nextBytes(k);
        return Base64.getEncoder().encodeToString(k);
    }

    public static boolean isAvailable()
    {
        return keyBytes != null && ivBytes != null;
    }

    /** 加密(默认 GCM): 明文 -> "AES-GCM:<base64>" */
    public static String encrypt(String plain)
    {
        if (StringUtils.isEmpty(plain))
        {
            return plain;
        }
        if (plain.startsWith(PREFIX_GCM) || plain.startsWith(PREFIX_CBC))
        {
            return plain;
        }
        ensureKey();
        try
        {
            byte[] iv = new byte[GCM_IV_BYTES];
            new SecureRandom().nextBytes(iv);
            Cipher cipher = Cipher.getInstance(TRANSFORM_GCM);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes, ALGO), new GCMParameterSpec(GCM_TAG_BITS, iv));
            byte[] ct = cipher.doFinal(plain.getBytes(StandardCharsets.UTF_8));
            // iv || ciphertext
            byte[] out = new byte[iv.length + ct.length];
            System.arraycopy(iv, 0, out, 0, iv.length);
            System.arraycopy(ct, 0, out, iv.length, ct.length);
            return PREFIX_GCM + Base64.getEncoder().encodeToString(out);
        }
        catch (Exception e)
        {
            throw new RuntimeException("AES-GCM 加密失败: " + e.getMessage(), e);
        }
    }

    /** 解密(自动识别 GCM/CBC): "AES-GCM:..." 或 "AES:..." -> 明文;非密文原样返回 */
    public static String decrypt(String cipherText)
    {
        if (StringUtils.isEmpty(cipherText))
        {
            return cipherText;
        }
        if (cipherText.startsWith(PREFIX_GCM))
        {
            return decryptGcm(cipherText.substring(PREFIX_GCM.length()));
        }
        if (cipherText.startsWith(PREFIX_CBC))
        {
            return legacyDecryptCbc(cipherText.substring(PREFIX_CBC.length()));
        }
        return cipherText;
    }

    private static String decryptGcm(String body)
    {
        ensureKey();
        try
        {
            byte[] all = Base64.getDecoder().decode(body);
            if (all.length < GCM_IV_BYTES + 16)
            {
                throw new IllegalArgumentException("GCM 密文长度过短");
            }
            byte[] iv = new byte[GCM_IV_BYTES];
            System.arraycopy(all, 0, iv, 0, GCM_IV_BYTES);
            byte[] ct = new byte[all.length - GCM_IV_BYTES];
            System.arraycopy(all, GCM_IV_BYTES, ct, 0, ct.length);
            Cipher cipher = Cipher.getInstance(TRANSFORM_GCM);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, ALGO), new GCMParameterSpec(GCM_TAG_BITS, iv));
            return new String(cipher.doFinal(ct), StandardCharsets.UTF_8);
        }
        catch (Exception e)
        {
            throw new RuntimeException("AES-GCM 解密失败: " + e.getMessage(), e);
        }
    }

    /** 兼容: 旧 CBC 密文,新 key 也可能能解(只要 IV 仍可复用 — 用 keyBytes 派生 IV) */
    private static String legacyDecryptCbc(String body)
    {
        ensureKey();
        try
        {
            byte[] enc = Base64.getDecoder().decode(body);
            Cipher cipher = Cipher.getInstance(TRANSFORM_CBC);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, ALGO), new IvParameterSpec(ivBytes));
            return new String(cipher.doFinal(enc), StandardCharsets.UTF_8);
        }
        catch (Exception e)
        {
            throw new RuntimeException("AES-CBC 兼容解密失败(可能需要旧 key): " + e.getMessage(), e);
        }
    }

    /** 脱敏: 完整密文/明文 -> 仅后 4 位;长度不足 4 全部 ****;空值原样返回 */
    public static String mask(String value)
    {
        if (StringUtils.isEmpty(value))
        {
            return value;
        }
        String plain = decrypt(value);
        if (plain.length() <= 4)
        {
            return "****";
        }
        return "****" + plain.substring(plain.length() - 4);
    }

    private static void ensureKey()
    {
        if (keyBytes == null || ivBytes == null)
        {
            throw new IllegalStateException("AesUtils 未初始化,请在 application.yml 配置 wx.aes.key");
        }
    }
}
