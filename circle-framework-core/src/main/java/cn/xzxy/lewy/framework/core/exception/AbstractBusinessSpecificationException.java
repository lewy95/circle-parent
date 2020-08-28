package cn.xzxy.lewy.framework.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lewy95
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractBusinessSpecificationException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String message = "业务异常";
    private int code = 400;

    public AbstractBusinessSpecificationException() {
    }

    public AbstractBusinessSpecificationException(String message) {
        super(message);
    }

    public AbstractBusinessSpecificationException(int code, String message) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public AbstractBusinessSpecificationException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }
}
