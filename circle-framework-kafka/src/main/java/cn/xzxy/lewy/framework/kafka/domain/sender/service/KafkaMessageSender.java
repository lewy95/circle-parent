package cn.xzxy.lewy.framework.kafka.domain.sender.service;

import cn.xzxy.lewy.framework.kafka.domain.sender.dao.SendLogDAO;
import cn.xzxy.lewy.framework.kafka.domain.sender.exception.SendException;
import cn.xzxy.lewy.framework.kafka.domain.sender.model.MessageSenderLog;
import cn.xzxy.lewy.framework.kafka.domain.sender.model.MessageVO;
import cn.xzxy.lewy.framework.kafka.feature.topic.TopicManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author lewy95
 */
@Component
public class KafkaMessageSender {

    @Autowired
    SendLogDAO sendLogDAO;

    @Autowired
    TopicManager topicManager;

    @Value("${circle.config.mq.sendNotExistTopic:false}")
    boolean sendNotExistTopic;

    static SendException EXP_NOT_ALLOW_SEND_NOT_EXIST_TOPIC = new SendException("不允许往不存在的主题发送消息，请手动创建或更改配置 ");
    static SendException EXP_MESSAGE_NULL = new SendException("发送的消息不能为空");

    public void sendMessage(MessageVO messageVO) throws SendException {
        if (Objects.isNull(messageVO)) {
            throw new SendException("不允许发送空消息");
        } else {
            try {
                boolean newTopic = this.topicManager.isNewTopic(messageVO.getTopic());
                if (this.sendNotExistTopic && newTopic) {
                    this.topicManager.createTopic(messageVO.getTopic());
                } else if (newTopic) {
                    throw EXP_NOT_ALLOW_SEND_NOT_EXIST_TOPIC;
                }

                List<MessageSenderLog> messageSenderLogs = this.sendLogDAO.add(Arrays.asList(messageVO));
                MessageThreadLocal.set((MessageSenderLog) messageSenderLogs.get(0));
            } catch (Exception e) {
                if (e instanceof SendException) {
                    throw e;
                } else {
                    throw new SendException("发送消息失败", e);
                }
            }
        }
    }

    public void sendMessage(MessageVO... messageVOList) throws SendException {
        try {
            if (messageVOList != null && messageVOList.length > 0) {
                List<String> topicList = new ArrayList();
                Arrays.stream(messageVOList).forEach((messageVO) -> {
                    topicList.add(messageVO.getTopic());
                });
                boolean newTopic = this.topicManager.isNewTopic(topicList);
                if (newTopic) {
                    throw EXP_NOT_ALLOW_SEND_NOT_EXIST_TOPIC;
                } else {
                    List<MessageSenderLog> messageSenderLogs = this.sendLogDAO.add(CollectionUtils.arrayToList(messageVOList));
                    MessageThreadLocal.set(messageSenderLogs);
                }
            } else {
                throw EXP_MESSAGE_NULL;
            }
        } catch (Exception var5) {
            if (var5 instanceof SendException) {
                throw var5;
            } else {
                throw new SendException("发送消息失败", var5);
            }
        }
    }
}
