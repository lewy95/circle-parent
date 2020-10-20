package cn.xzxy.lewy.framework.kafka.domain.sender.dao;

import cn.xzxy.lewy.framework.kafka.domain.sender.exception.SendException;
import cn.xzxy.lewy.framework.kafka.domain.sender.model.MessageSenderLog;
import cn.xzxy.lewy.framework.kafka.domain.sender.model.MessageStatus;
import cn.xzxy.lewy.framework.kafka.domain.sender.model.MessageVO;
import cn.xzxy.lewy.framework.kafka.domain.sender.service.MessageFactory;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lewy95
 */
@Component
@Slf4j
public class SendLogDAO {

    @Autowired
    MessageSenderLogRepository messageSenderLogRepository;

    @Autowired
    MessageFactory messageFactory;

    public List<MessageSenderLog> add(List<MessageVO> messageVOS) throws SendException {
        try {
            List<MessageSenderLog> messageSenderLogs = new ArrayList(messageVOS.size());
            messageVOS.stream().forEach((messageVO) -> {
                MessageSenderLog messageSenderLog = this.messageFactory.getMessageSenderLog(messageVO);
                messageSenderLogs.add(messageSenderLog.preSend());
            });
            this.messageSenderLogRepository.saveAll(messageSenderLogs);
            log.debug("保存发送初始化日志成功");
            return messageSenderLogs;
        } catch (Exception var3) {
            throw new SendException("保存发送初始化日志失败", var3);
        }
    }

    public void cancel(List<MessageSenderLog> messageSenderLogs, Throwable throwable) {
        try {
            messageSenderLogs.stream().forEach((messageSenderLog) -> {
                messageSenderLog.cancel(throwable);
            });
            this.messageSenderLogRepository.saveAll(messageSenderLogs);
            log.debug("保存取消发送日志成功:{}", messageSenderLogs);
        } catch (Exception var4) {
            log.error("保存取消发送日志失败{}", JSON.toJSON(messageSenderLogs));
        }

    }

    public void fail(MessageSenderLog messageSenderLog, Throwable throwable) {
        try {
            messageSenderLog.fail(throwable);
            this.messageSenderLogRepository.save(messageSenderLog);
            log.debug("保存发送错误日志成功:{}", messageSenderLog.getMessage().getMessageId());
        } catch (Exception var4) {
            log.error("保存发送错误日志失败{}", JSON.toJSON(messageSenderLog));
        }

    }

    public void success(MessageSenderLog messageSenderLog, int partition, long offset) {
        try {
            messageSenderLog.success(partition, offset);
            this.messageSenderLogRepository.save(messageSenderLog);
            log.debug("保存发送成功日志成功:{}", messageSenderLog.getMessage().getMessageId());
        } catch (Exception var6) {
            log.error("保存发送成功日志失败{}", JSON.toJSON(messageSenderLog));
        }

    }

    public List<MessageSenderLog> queryFailMessage() {
        return this.messageSenderLogRepository.findByMessageStatusOrderBySendTimeAsc(MessageStatus.FAIL);
    }
}
