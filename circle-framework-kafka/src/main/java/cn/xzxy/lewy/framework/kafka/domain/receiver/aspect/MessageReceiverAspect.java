package cn.xzxy.lewy.framework.kafka.domain.receiver.aspect;

import cn.xzxy.lewy.framework.kafka.domain.receiver.exception.MessageHandlerException;
import cn.xzxy.lewy.framework.kafka.domain.receiver.model.MessageReceiveLog;
import cn.xzxy.lewy.framework.kafka.domain.receiver.service.KafkaMessageReceiver;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.ThreadContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author lewy95
 */
@Aspect
@Component
@Slf4j
public class MessageReceiverAspect {

    @Autowired
    KafkaMessageReceiver mqMessageReceiver;

    @Autowired
    RedissonClient redissonClient;

    @Value("${circle.config.mq.messageIdLockPrefix:circle:mq:messageIDLock:}")
    String messageIdLockPrefix;
    @Value("${circle.config.mq.retryCount:3}")
    Integer retryCount;
    @Value("${circle.config.mq.retryTime:1000}")
    Long retryTime;
    @Value("${spring.application.name}")
    String appName;

    @Pointcut("@annotation(org.springframework.kafka.annotation.KafkaListener)")
    public void KafkaListenerAspect() {
    }

    @Around("KafkaListenerAspect()")
    public void doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] args = proceedingJoinPoint.getArgs();
        ConsumerRecord consumerRecord = (ConsumerRecord) args[0];
        log.debug("接收消息 {}", consumerRecord);
        Acknowledgment acknowledgment = (Acknowledgment) args[1];
        MessageReceiveLog messageReceiveLog = this.mqMessageReceiver.getMessageReceiveLog(consumerRecord);
        ThreadContext.put("TraceId", messageReceiveLog.getMessage().getMessageId());
        String messageId = messageReceiveLog.getMessage().getMessageId();
        String lockKey = this.messageIdLockPrefix + ":" + this.appName + ":" + messageId;
        RLock lock = this.redissonClient.getLock(lockKey);
        if (lock.tryLock()) {
            log.debug("消息 {} 加锁，进行业务处理", messageId);
            if (acknowledgment == null) {
                log.warn("无法获取手动ack，无法使用重试机制");

                try {
                    proceedingJoinPoint.proceed();
                    this.mqMessageReceiver.success(messageReceiveLog);
                } catch (Throwable throwable) {
                    this.mqMessageReceiver.error(messageReceiveLog, throwable);
                    throw throwable;
                } finally {
                    if (lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }

                }
            } else {
                Throwable t = null;
                log.debug("获取手动ack，使用重试机制");
                int i = 0;

                while (i < this.retryCount) {
                    try {
                        t = null;
                        proceedingJoinPoint.proceed();
                        this.mqMessageReceiver.success(messageReceiveLog, i);
                        break;
                    } catch (Throwable throwable) {
                        t = throwable;
                        if (!(throwable instanceof MessageHandlerException)) {
                            log.error("抛出预期的业务异常，不进行业务重试，异常信息", throwable);
                            break;
                        }

                        log.error("抛出非预期的业务异常，进行业务重试，当前次数{}，异常信息: {}", i + 1, throwable);
                        Thread.sleep(this.retryTime);
                        ++i;
                    }
                }

                acknowledgment.acknowledge();

                try {
                    if (lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                } catch (Exception exception) {
                    log.error("redis释放锁失败", exception);
                }

                if (Objects.isNull(t)) {
                    log.info("业务重试{}次之后成功", i);
                } else {
                    log.info("业务重试{}次失败，暂不处理", i);
                    this.mqMessageReceiver.error(messageReceiveLog, t, i);
                }
            }
        } else {
            log.info("{}其他实例正在处理本消息，本实例将忽略该消息", messageId);
        }

    }
}
