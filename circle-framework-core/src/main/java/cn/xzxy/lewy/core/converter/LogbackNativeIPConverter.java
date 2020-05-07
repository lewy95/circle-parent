package cn.xzxy.lewy.core.converter;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * 彩色日志渲染类（针对IP）
 *
 * @author lewy95
 **/
public class LogbackNativeIPConverter extends ClassicConverter {
    private static final List<String> IP_CACHE = new ArrayList<>(2);

    @Override
    public String convert(ILoggingEvent event) {
        try {
            if (IP_CACHE.isEmpty()) {
                String localhost = InetAddress.getLocalHost().getHostAddress();
                IP_CACHE.add(localhost);
                return localhost;
            }
            return IP_CACHE.get(0);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "0.0.0.0";

    }
}
