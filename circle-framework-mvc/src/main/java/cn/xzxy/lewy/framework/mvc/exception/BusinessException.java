package cn.xzxy.lewy.framework.mvc.exception;

/**
 * @author lewy95
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1631417040312856798L;

    private String message = "业务异常";
    private int status = 400;

    public BusinessException(String message) {
        this.message = message;
    }

    // 这个构造方法的作用：有时候为了区别异常来源，要求不同业务抛出异常时需指定一个独立的状态码
    // 比如 new BusinessException(10001, "未找到订单")
    // 比如 new BusinessException(10002, "订单状态不为待付款")
    public BusinessException(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

