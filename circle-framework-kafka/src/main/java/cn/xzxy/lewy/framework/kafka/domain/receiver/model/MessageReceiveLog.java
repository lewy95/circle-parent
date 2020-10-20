package cn.xzxy.lewy.framework.kafka.domain.receiver.model;

import cn.xzxy.lewy.framework.kafka.domain.log.model.core.MessageMetadata;
import cn.xzxy.lewy.framework.kafka.domain.log.model.core.RawMessage;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * @author lewy95
 */
@Data
public class MessageReceiveLog {

    @Id
    String id;
    /**
     * 应用名称
     */
    String appName;
    /**
     * 接收时间
     */
    Date receiveTime;
    /**
     * 处理完成时间 业务完成回调
     */
    Date handledTime;
    /**
     * 消息状态
     */
    MessageStatus messageStatus;
    /**
     * 错误信息
     */
    String failMsg;
    /**
     * 接收的消息
     */
    RawMessage message;
    /**
     * 消息元数据
     */
    MessageMetadata messageMetadata;
    /**
     * 重试次数
     */
    Integer retry;

    public MessageReceiveLog preHandler() {
        this.receiveTime = new Date();
        this.messageStatus = MessageStatus.WAIT_FOR_HANDLER;
        return this;
    }

    public MessageReceiveLog success(int retry) {
        this.retry = retry;
        return this.success();
    }

    public MessageReceiveLog success() {
        this.messageStatus = MessageStatus.SUCCESS;
        this.handledTime = new Date();
        return this;
    }

    public MessageReceiveLog fail(Throwable throwable, int retry) {
        this.retry = retry;
        return this.fail(throwable);
    }

    public MessageReceiveLog fail(Throwable throwable) {
        this.messageStatus = MessageStatus.FAIL;
        this.failMsg = throwable.getMessage();
        this.handledTime = new Date();
        return this;
    }

    public MessageReceiveLog(String appName, String topic, String key, RawMessage message) {
        this.appName = appName;
        this.message = message;
        this.messageMetadata = new MessageMetadata(topic, key);
    }
}
