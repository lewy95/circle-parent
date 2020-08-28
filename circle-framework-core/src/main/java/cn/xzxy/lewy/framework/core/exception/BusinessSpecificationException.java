package cn.xzxy.lewy.framework.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lewy95
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessSpecificationException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    private String message = "业务异常";
    private int status = 400;

    public BusinessSpecificationException(String message) {
        this.message = message;
    }

    public BusinessSpecificationException(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
