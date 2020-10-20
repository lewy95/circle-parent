package cn.xzxy.lewy.framework.kafka.feature.common;

/**
 * @author lewy95
 */
public abstract class AbstractException extends RuntimeException {
    private int code;
    private String message;
    private String error;

    public <T extends ExceptionCode> AbstractException(T error) {
        this(error.getCode(), error.getMessage(), error.getMessage());
    }

    public AbstractException(int code, String message) {
        this(code, message, message, (Throwable)null);
    }

    public AbstractException(int code, String message, Throwable cause) {
        this(code, message, message, cause);
    }

    public AbstractException(int code, String message, String error) {
        this(code, message, error, (Throwable)null);
    }

    public AbstractException(int code, String message, String error, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
        this.error = error;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getError() {
        return this.error;
    }
}
