package cn.xzxy.lewy.framework.kafka.domain.receiver.exception;

/**
 * @author lewy95
 */
public class MessageHandlerException extends RuntimeException {
    public MessageHandlerException(String message) {
        super(message);
    }

    public MessageHandlerException(String message, Throwable cause) {
        super(message, cause);
    }
}
