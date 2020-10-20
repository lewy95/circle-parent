package cn.xzxy.lewy.framework.kafka.domain.sender.service;

import cn.xzxy.lewy.framework.kafka.domain.sender.dao.SendLogDAO;
import cn.xzxy.lewy.framework.kafka.domain.sender.model.MessageSenderLog;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author lewy95
 */
@Component
@Slf4j
public class RetrySendTask {

    private static final String MESSAGE_RETRY_TASK_LOCK = "MESSAGE_RETRY_TASK_LOCK-";
    @Autowired
    RedissonClient redissonClient;
    @Autowired
    KafkaSender kafkaSender;
    @Autowired
    SendLogDAO sendLogDAO;
    int base = 1;
    int max = 60;

    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("开始检查发送失败的消息");
        (new Thread(() -> {
            this.start();
        })).start();
    }

    public void start() {
        while (true) {
            List<MessageSenderLog> messageSenderLogs = this.sendLogDAO.queryFailMessage();
            if (!Objects.isNull(messageSenderLogs) && !messageSenderLogs.isEmpty()) {
                this.base = 1;
                int size = messageSenderLogs.size();
                log.info("查询到{}条失败状态的log，开始重试发送", size);
                messageSenderLogs.forEach((messageSenderLog) -> {
                    this.retry(messageSenderLog);
                });
            }

            try {
                Thread.sleep((long) (1000 * this.base));
                log.debug("没有查询到失败状态的log，检查线程休眠{}秒", this.base);
                this.base = Math.min(this.base * 2, this.max);
            } catch (InterruptedException var3) {
                var3.printStackTrace();
            }
        }
    }

    public void retry(MessageSenderLog messageSenderLog) {
        String id = messageSenderLog.getId();
        String key = "MESSAGE_RETRY_TASK_LOCK-" + id;
        RLock lock = this.redissonClient.getLock(key);
        log.info("消息{}准备开始重试发送...", id);
        if (lock.tryLock()) {
            log.info("消息{}加锁...", id);

            try {
                this.kafkaSender.send(messageSenderLog);
            } catch (Throwable var9) {
                log.error("消息{}重试发送失败", id);
                throw var9;
            } finally {
                if (lock.isLocked()) {
                    lock.unlock();
                }

            }
        } else {
            log.info("消息{}已经由其他实例处理，本实例将忽略该消息", id);
        }

    }
}
