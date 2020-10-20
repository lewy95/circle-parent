package cn.xzxy.lewy.framework.kafka.domain.sender.service;

import cn.xzxy.lewy.framework.kafka.domain.log.model.core.RawMessage;
import cn.xzxy.lewy.framework.kafka.domain.sender.model.MessageSenderLog;
import cn.xzxy.lewy.framework.kafka.domain.sender.model.MessageVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author lewy95
 */
@Component
public class MessageFactory {

    @Value("${spring.application.name}")
    String appName;

    public RawMessage getKafkaMessage(MessageVO messageVO) {
        RawMessage rawMessage = new RawMessage(messageVO.getTraceId(), messageVO.getValue());
        return rawMessage;
    }

    public MessageSenderLog getMessageSenderLog(MessageVO messageVO) {
        RawMessage rawMessage = this.getKafkaMessage(messageVO);
        return new MessageSenderLog(this.appName, messageVO.getTopic(), messageVO.getKey(), rawMessage);
    }
}
