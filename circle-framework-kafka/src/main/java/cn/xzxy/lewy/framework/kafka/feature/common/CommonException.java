package cn.xzxy.lewy.framework.kafka.feature.common;

/**
 * @author lewy95
 */
public class CommonException extends AbstractException {
    public <T extends ExceptionCode> CommonException(T error) {
        super(error);
    }

    public CommonException(int code, String message) {
        super(code, message);
    }

    public CommonException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public CommonException(int code, String message, String error) {
        super(code, message, error);
    }

    public CommonException(int code, String message, String error, Throwable cause) {
        super(code, message, error, cause);
    }
}
