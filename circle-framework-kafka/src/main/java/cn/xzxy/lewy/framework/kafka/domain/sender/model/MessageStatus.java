package cn.xzxy.lewy.framework.kafka.domain.sender.model;

/**
 * @author lewy95
 */
public enum  MessageStatus {

    WAIT_FOR_SEND,
    SUCCESS,
    FAIL,
    CANCEL;

    private MessageStatus() {
    }
}
