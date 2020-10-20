package cn.xzxy.lewy.framework.kafka.domain.sender.aspect;

import cn.xzxy.lewy.framework.kafka.domain.sender.exception.SendException;
import cn.xzxy.lewy.framework.kafka.domain.sender.model.MessageSenderLog;
import cn.xzxy.lewy.framework.kafka.domain.sender.model.SenderEvent;
import cn.xzxy.lewy.framework.kafka.domain.sender.service.MessageThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author lewy95
 */
@Aspect
@Component
@Slf4j
public class MessageSendTriggerAspect {

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void transactional() {
    }

    @AfterReturning("transactional()")
    public void afterReturning() {
        log.debug("线程{}提交事务中", Thread.currentThread().getName());
        final List<MessageSenderLog> messageSenderLogs = MessageThreadLocal.get();
        if (!CollectionUtils.isEmpty(messageSenderLogs)) {
            this.removeThreadLocal();
            if (TransactionSynchronizationManager.isSynchronizationActive()) {
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                    public void afterCompletion(int status) {
                        if (0 == status) {
                            MessageSendTriggerAspect.this.sendMessage(messageSenderLogs);
                        } else {
                            MessageSendTriggerAspect.log.debug("线程{}提交事务失败，更新{}条消息状态CANCEL", Thread.currentThread().getName(), messageSenderLogs.size());
                            MessageSendTriggerAspect.this.applicationEventPublisher.publishEvent(SenderEvent.cancel(new SendException("事务提交失败，取消发送消息"), messageSenderLogs));
                        }

                    }
                });
            } else {
                log.warn("无法获取同步事务，直接发送消息");
                this.sendMessage(messageSenderLogs);
            }
        } else {
            log.debug("本次事务，没有需要发送的消息");
        }

    }

    void sendMessage(List<MessageSenderLog> messageSenderLogs) {
        log.debug("线程{}提交事务成功，准备发送消息：{}", Thread.currentThread().getName(), messageSenderLogs);
        this.applicationEventPublisher.publishEvent(SenderEvent.waitForSend(messageSenderLogs));
    }

    @AfterThrowing(
            value = "transactional()",
            throwing = "throwable"
    )
    public void afterThrowing(Throwable throwable) {
        List<MessageSenderLog> messageSenderLogs = MessageThreadLocal.get();
        if (!CollectionUtils.isEmpty(messageSenderLogs)) {
            this.removeThreadLocal();
            log.debug("业务方法异常，取消发送消息:{}", messageSenderLogs.size());
            this.applicationEventPublisher.publishEvent(SenderEvent.cancel(throwable, messageSenderLogs));
        } else {
            log.debug("本次事务，没有需要发送的消息");
        }

    }

    void removeThreadLocal() {
        MessageThreadLocal.remove();
    }
}
