package cn.xzxy.lewy.framework.kafka.domain.sender.service;

import cn.xzxy.lewy.framework.kafka.domain.sender.model.MessageSenderLog;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * @author lewy95
 */
@Slf4j
public class MessageThreadLocal {

    static final ThreadLocal<List<MessageSenderLog>> threadLocal = new ThreadLocal<>();

    public MessageThreadLocal() {
    }

    public static void set(MessageSenderLog messageSenderLog) {
        log.debug("线程{}设置消息{}", Thread.currentThread().getName(), JSON.toJSONString(messageSenderLog));
        threadLocal.set(Arrays.asList(messageSenderLog));
    }

    public static void set(List<MessageSenderLog> messageSenderLog) {
        log.debug("线程{}设置消息{}", Thread.currentThread().getName(), JSON.toJSONString(messageSenderLog));
        threadLocal.set(messageSenderLog);
    }

    public static void remove() {
        threadLocal.remove();
    }

    public static List<MessageSenderLog> get() {
        List<MessageSenderLog> messageSenderLogs = (List) threadLocal.get();
        log.debug("线程{}获取消息{}", Thread.currentThread().getName(), JSON.toJSONString(messageSenderLogs));
        return messageSenderLogs;
    }
}
