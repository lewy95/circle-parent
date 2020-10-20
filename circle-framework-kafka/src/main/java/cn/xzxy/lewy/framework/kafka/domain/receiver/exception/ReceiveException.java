package cn.xzxy.lewy.framework.kafka.domain.receiver.exception;

/**
 * @author lewy95
 */
public class ReceiveException extends Exception {
    public ReceiveException(String message) {
        super(message);
    }

    public ReceiveException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReceiveException(Throwable cause) {
        super(cause);
    }
}
