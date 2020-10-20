package cn.xzxy.lewy.framework.kafka.domain.sender.service;

import cn.xzxy.lewy.framework.kafka.domain.sender.dao.SendLogDAO;
import cn.xzxy.lewy.framework.kafka.domain.sender.model.MessageSenderLog;
import cn.xzxy.lewy.framework.kafka.domain.sender.model.MessageStatus;
import cn.xzxy.lewy.framework.kafka.domain.sender.model.SenderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lewy95
 */
@Component
@Slf4j
public class MessageSenderService {

    @Autowired
    SendLogDAO sendLogDAO;
    @Autowired
    KafkaSender kafkaSender;

    @EventListener
    @Async("logServiceThreadPoolExecutor")
    public void handlerSenderEvt(SenderEvent senderEvt) {
        MessageStatus messageStatus = senderEvt.getMessageStatus();
        List<MessageSenderLog> messageSenderLogs = senderEvt.getMessageSenderLogs();
        switch (messageStatus) {
            case WAIT_FOR_SEND:
                log.info("发送中{}条消息，{}", messageSenderLogs.size(), messageSenderLogs);
                messageSenderLogs.forEach((messageSenderLog) -> {
                    this.kafkaSender.send(messageSenderLog);
                });
                break;
            case CANCEL:
                log.info("取消发送{}条消息，{}", messageSenderLogs.size(), messageSenderLogs);
                this.sendLogDAO.cancel(messageSenderLogs, senderEvt.getThrowable());
                break;
            case FAIL:
                log.error("发送失败{}条消息，{}", messageSenderLogs.size(), messageSenderLogs);
                this.sendLogDAO.fail((MessageSenderLog) messageSenderLogs.get(0), senderEvt.getThrowable());
                break;
            case SUCCESS:
                log.info("发送成功{}条消息，{}", messageSenderLogs.size(), messageSenderLogs);
                this.sendLogDAO.success((MessageSenderLog) messageSenderLogs.get(0), senderEvt.getPartition(), senderEvt.getOffset());
        }

    }
}
