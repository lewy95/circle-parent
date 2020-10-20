package cn.xzxy.lewy.framework.kafka.domain.sender.model;

import cn.xzxy.lewy.framework.kafka.domain.log.model.core.MessageMetadata;
import cn.xzxy.lewy.framework.kafka.domain.log.model.core.RawMessage;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lewy95
 */
@Data
public class MessageSenderLog {

    @Id
    String id;
    /**
     * 应用名称
     */
    String appName;

    /**
     * 发送时间
     */
    Date sendTime;
    /**
     * 确认时间 kafka回调时间
     */
    Date confirmTime;
    /**
     * 消息状态
     */
    MessageStatus messageStatus;
    /**
     * 发送错误信息
     */
    String failMsg;
    /**
     * 发送的消息
     */
    RawMessage message;
    /**
     * 消息元数据
     */
    MessageMetadata messageMetadata;
    List<MessageStatusItem> messageStatusItems = new ArrayList<>();

    public MessageSenderLog preSend() {
        this.changeStatas(MessageStatus.WAIT_FOR_SEND, (Throwable) null);
        this.sendTime = new Date();
        return this;
    }

    public MessageSenderLog success(int partition, long offset) {
        this.changeStatas(MessageStatus.SUCCESS, (Throwable) null);
        this.confirmTime = new Date();
        this.messageMetadata.setPartition(partition);
        this.messageMetadata.setOffset(offset);
        return this;
    }

    public MessageSenderLog fail(Throwable throwable) {
        this.changeStatas(MessageStatus.FAIL, throwable);
        this.failMsg = throwable.getMessage();
        this.confirmTime = new Date();
        return this;
    }

    public MessageSenderLog cancel(Throwable throwable) {
        this.changeStatas(MessageStatus.CANCEL, throwable);
        this.failMsg = throwable.getMessage();
        this.confirmTime = new Date();
        return this;
    }

    public MessageSenderLog(String appName, String topic, String key, RawMessage message) {
        this.appName = appName;
        this.message = message;
        this.messageMetadata = new MessageMetadata(topic, key);
    }

    private void changeStatas(MessageStatus messageStatus, Throwable throwable) {
        this.messageStatus = messageStatus;
        this.messageStatusItems.add(new MessageStatusItem(messageStatus, throwable));
    }
}
