package cn.xzxy.lewy.framework.kafka.domain.sender.model;

import jodd.exception.ExceptionUtil;
import lombok.Data;

/**
 * @author lewy95
 */
@Data
public class MessageStatusItem {

    MessageStatus messageStatus;
    Long date;
    String error = "";

    public MessageStatusItem(MessageStatus messageStatus, Throwable throwable) {
        this.messageStatus = messageStatus;
        this.date = System.currentTimeMillis();
        if (throwable != null) {
            this.error = ExceptionUtil.getRootCause(throwable).getMessage();
        }
    }

}
