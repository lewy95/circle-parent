package cn.xzxy.lewy.framework.core.model;

import cn.xzxy.lewy.framework.core.utils.FastJsonUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * @author lewy95
 **/
@AllArgsConstructor
@NoArgsConstructor
public class JsonResponseEntity<T> implements Serializable {
    private static final long serialVersionUID = -7159180777326370540L;

    /**
     * 状态（预留，暂不用）
     */
    public static final String SUCCESS = HttpStatus.OK.toString();
    public static final String INTERNAL_SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR.toString();
    public static final String BAD_REQUEST = HttpStatus.BAD_REQUEST.toString();
    public static final String UNAUTHORIZED = HttpStatus.UNAUTHORIZED.toString();
    public static final String FORBIDDEN = HttpStatus.FORBIDDEN.toString();

    @Getter
    @Setter
    private String message = HttpStatus.OK.getReasonPhrase();
    @Getter
    @Setter
    private int code = HttpStatus.OK.value();
    @Getter
    @Setter
    private T data;
    @Getter
    @Setter
    private String error;

    /**
     *
     * @param httpStatus  httpStatus
     * @return result
     */
    public JsonResponseEntity setHttpStatus(HttpStatus httpStatus) {
        Assert.notNull(httpStatus, "httpStatus can not be null");
        this.setMessage(httpStatus.getReasonPhrase());
        this.setCode(httpStatus.value());
        return this;
    }

    /**
     *
     * @return JsonResponseEntity
     */
    public static <T>JsonResponseEntity<T> builder() {
        return new JsonResponseEntity<T>();
    }

    /**
     * 返回响应 OK
     *
     * @param data  data
     * @return JsonResponseEntity
     */
    public static <T>JsonResponseEntity<T> buildOK(T data) {
        return new JsonResponseEntity<T>().setResponse(data);
    }

    /**
     * 返回响应 OK
     *
     * @param message  message
     * @param data  data
     * @return result
     */
    public static <T> JsonResponseEntity<T> buildOK(String message, T data) {
        return buildOK(data).setMessage(message);
    }


    /**
     * 抛出系统异常响应
     *
     * @return JsonResponseEntity
     */
    public static JsonResponseEntity buildSysTemError() {
        return builder().setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 抛出系统异常响应
     *
     * @return JsonResponseEntity
     */
    public static JsonResponseEntity buildSysTemError(String message) {
        return buildSysTemError().setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR).setMessage(message);
    }

    /**
     * 抛出业务异常响应
     *
     * @return JsonResponseEntity
     */
    public static <T> JsonResponseEntity<T> buildBusinessError() {
        return builder().setHttpStatus(HttpStatus.BAD_REQUEST);
    }

    /**
     * 抛出业务异常响应
     *
     * @return JsonResponseEntity
     */
    public static JsonResponseEntity buildBusinessError(String message) {
        return buildBusinessError().setMessage(message);
    }

    public static JsonResponseEntity buildBusinessError(String message, int status) {
        return buildBusinessError().setMessage(message).setCode(status);
    }


    public JsonResponseEntity setMessage(String message) {
        this.message = message;
        return this;
    }

    public JsonResponseEntity setCode(int code) {
        this.code = code;
        return this;
    }

    public JsonResponseEntity<T> setResponse(T data) {
        this.data = data;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public boolean isSuccessful() {
        return 200 == code;
    }

    @Override
    public String toString() {
        return FastJsonUtil.to(this);
    }
}
