package cn.xzxy.lewy.framework.kafka.domain.receiver.model;

/**
 * @author lewy95
 */
public enum MessageStatus {
    WAIT_FOR_HANDLER,
    SUCCESS,
    FAIL;

    private MessageStatus() {
    }
}
