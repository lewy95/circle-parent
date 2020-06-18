package cn.xzxy.lewy.framework.core.encrpt;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * 提供AES算法
 */
@Slf4j
public class AES {

    /**
     * AES初始化秘钥长度
     **/
    private static final int STANDRAD_AES_KEY_SIZE = 32;

    public static final String AES_KEY = "aesKey";

    public static AES getInstance() {
        return new AES();
    }

    public AES() {
    }

    /**
     * 加密（字节数组）
     */
    public byte[] encrypt(byte[] key, byte[] input) throws Exception {
        Cipher cipher = Cipher.getInstance(CipherConstPool.CIPHER_AES_NAME);
        SecretKeySpec keySpec = new SecretKeySpec(key, CipherConstPool.CIPHER_AES);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(input);
    }

    /**
     * 加密（字符串）
     */
    public String encrypt(String key, String input) throws Exception {
        log.info("encrypt parameters key: " + key + ",input: " + input);
        if (StringUtils.isBlank(input) || StringUtils.isBlank(key)) {
            throw new RuntimeException("encrypt error");
        }
        byte[] inputs = input.getBytes(StandardCharsets.UTF_8);
        byte[] keys = key.getBytes(StandardCharsets.UTF_8);
        byte[] encrypt = this.encrypt(keys, inputs);
        return Base64Utils.encodeToString(encrypt);
    }

    /**
     * 加密
     *
     * @param plainText   需要加密的明文字符
     * @param key         加密用的Key
     * @param ivParameter 偏移量
     */
    public static String encrypt(String plainText, String key, String ivParameter) throws Exception {
        if (key == null || plainText == null) {
            return null;
        }
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = key.getBytes();
        // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(plainText.getBytes("utf-8"));
        // 此处使用BASE64做转码。
        return new BASE64Encoder().encode(encrypted);
    }

    /**
     * 解密（字节数组）
     */
    public byte[] decrypt(byte[] key, byte[] input) throws Exception {
        Cipher cipher = Cipher.getInstance(CipherConstPool.CIPHER_AES_NAME);
        SecretKeySpec keySpec = new SecretKeySpec(key, CipherConstPool.CIPHER_AES);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(input);
    }

    /**
     * 解密（字符串）
     */
    public String decrypt(String key, String input) throws Exception {
        log.info("decrypt parameters key: " + key + ",input: " + input);
        if (StringUtils.isBlank(input) || StringUtils.isBlank(key)) {
            throw new RuntimeException("decrypt error");
        }
        byte[] inputs = Base64Utils.decode(input);
        byte[] keys = key.getBytes(StandardCharsets.UTF_8);
        byte[] decrypt = this.decrypt(keys, inputs);
        return new String(decrypt, StandardCharsets.UTF_8);
    }

    /**
     * 解密
     *
     * @param cipherText  需要解密的字符
     * @param key         加密用的Key
     * @param ivParameter 偏移量
     */
    public static String decrypt(String cipherText, String key, String ivParameter) throws Exception {
        try {
            byte[] raw = key.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            // 先用base64解密
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(cipherText);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 初始化AES生成设备秘钥
     *
     * @param appId
     * @param deviceId
     */
    public String initKey(String appId, String deviceId) {
        log.info("initKey parameters appId: " + appId + ",deviceId: " + deviceId);
        StringBuffer metaKey = new StringBuffer();
        metaKey.append(appId).append(deviceId);
        if (metaKey.length() > STANDRAD_AES_KEY_SIZE) {
            log.info("generator aes key: " + metaKey);
            return metaKey.substring(0, STANDRAD_AES_KEY_SIZE);
        } else if (metaKey.length() < STANDRAD_AES_KEY_SIZE) {
            int length = STANDRAD_AES_KEY_SIZE - metaKey.length();
            for (int i = 0; i < length; i++) {
                if (String.valueOf(i).length() > 1) {
                    metaKey.append(String.valueOf(i).substring(1));
                } else {
                    metaKey.append(String.valueOf(i).substring(0));
                }
            }
        }
        log.info("generator key: " + metaKey);
        return metaKey.toString();
    }

    /**
     * 获取工作秘钥
     */
    public String workKey() {
        log.info("enter workKey... ");
        StringBuffer metaKey = new StringBuffer();
        for (int i = 0; i < STANDRAD_AES_KEY_SIZE; i++) {
            Random ran = new Random();
            int index = ran.nextInt(CharsetConstPool.SEED_POOL.length);
            metaKey.append(CharsetConstPool.SEED_POOL[index]);
        }
        log.info("generator key: " + metaKey);
        return metaKey.toString();
    }

    /**
     * 变换AES工作秘钥
     */
    public String getWorkKey(String message) {
        log.info("enter getWorkKey,parameters: " + message);
        StringBuffer metaKey = new StringBuffer();
        metaKey.append(message);
        if (metaKey.length() > STANDRAD_AES_KEY_SIZE) {
            String key = metaKey.substring(0, STANDRAD_AES_KEY_SIZE);
            log.info("generator key: " + key);
            return key;
        } else if (metaKey.length() < STANDRAD_AES_KEY_SIZE) {
            int length = STANDRAD_AES_KEY_SIZE - metaKey.length();
            for (int i = 0; i < length; i++) {
                if (String.valueOf(i).length() > 1) {
                    metaKey.append(String.valueOf(i).substring(1));
                } else {
                    metaKey.append(String.valueOf(i).substring(0));
                }
            }
            log.info("generator key: " + metaKey);
            return metaKey.toString();
        }
        log.info("generator key: " + metaKey);
        return metaKey.toString();
    }

}
