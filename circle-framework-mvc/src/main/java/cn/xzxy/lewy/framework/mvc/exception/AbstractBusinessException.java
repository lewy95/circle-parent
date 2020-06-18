package cn.xzxy.lewy.framework.mvc.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lewy95
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AbstractBusinessException extends RuntimeException {

    private static final long serialVersionUID = 6081353454465480460L;

    private String message = "业务异常";
    private int code = 400;

    public AbstractBusinessException() {
    }

    public AbstractBusinessException(String message) {
        super(message);
    }

    public AbstractBusinessException(int code, String message) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public AbstractBusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }
}
