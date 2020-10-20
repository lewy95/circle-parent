package cn.xzxy.lewy.framework.kafka.domain.sender.exception;

/**
 * @author lewy95
 */
public class SendException extends Exception{

    public SendException(String message, Throwable cause) {
        super(message, cause);
    }

    public SendException(String message) {
        super(message);
    }
}
