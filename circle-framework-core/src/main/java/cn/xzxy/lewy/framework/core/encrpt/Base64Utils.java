package cn.xzxy.lewy.framework.core.encrpt;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

/**
 * 基于JDK的Base64转码工具
 */
@Slf4j
public class Base64Utils {

    public static String encodeToString(byte[] bytes) {
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(bytes);
    }

    public static byte[] decode(String text) {
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            return decoder.decodeBuffer(text);
        } catch (IOException e) {
            log.error("Base64Utils decode fail", e);
        }
        return null;
    }
}
