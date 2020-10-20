package cn.xzxy.lewy.framework.kafka.domain.receiver.service;

import cn.xzxy.lewy.framework.kafka.domain.log.model.core.RawMessage;
import cn.xzxy.lewy.framework.kafka.domain.receiver.dao.MessageReceiveLogRepository;
import cn.xzxy.lewy.framework.kafka.domain.receiver.model.MessageReceiveLog;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author lewy95
 */
@Service
@Slf4j
public class KafkaMessageReceiver {

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    MessageReceiveLogRepository logRepository;

    public MessageReceiveLog getMessageReceiveLog(ConsumerRecord consumerRecord) {
        JSONObject msg = JSON.parseObject((String) consumerRecord.value());
        JSONObject value = JSON.parseObject(msg.getString("value"));
        RawMessage rawMessage = new RawMessage(msg.getString("messageId"), msg.getString("traceId"), value);
        MessageReceiveLog messageReceiveLog = (new MessageReceiveLog(this.appName, consumerRecord.topic(), (String) consumerRecord.key(), rawMessage)).preHandler();
        messageReceiveLog.getMessageMetadata().setPartition(consumerRecord.partition());
        messageReceiveLog.getMessageMetadata().setOffset(consumerRecord.offset());
        return messageReceiveLog;
    }

    public void success(MessageReceiveLog messageReceiveLog, int retry) {
        this.innerSuccess(messageReceiveLog.success(retry));
    }

    public void success(MessageReceiveLog messageReceiveLog) {
        this.innerSuccess(messageReceiveLog.success());
    }

    private void innerSuccess(MessageReceiveLog messageReceiveLog) {
        try {
            this.logRepository.save(messageReceiveLog);
            log.debug("修改消息状态success成功");
        } catch (Exception var3) {
            log.error("业务处理成功，修改消息日志状态success失败，当前消息：{}", messageReceiveLog, var3);
        }

    }

    public void error(MessageReceiveLog messageReceiveLog, Throwable t, int retry) {
        this.innerError(messageReceiveLog.fail(t, retry));
    }

    public void error(MessageReceiveLog messageReceiveLog, Throwable t) {
        this.innerError(messageReceiveLog.fail(t));
    }

    private void innerError(MessageReceiveLog messageReceiveLog) {
        try {
            this.logRepository.save(messageReceiveLog);
            log.debug("修改消息状态fail成功");
        } catch (Exception e) {
            log.error("业务处理失败，修改消息日志状态fail失败，当前消息：{}", messageReceiveLog, e);
        }
    }
}