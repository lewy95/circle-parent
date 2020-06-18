package cn.xzxy.lewy.framework.mvc.handler;

import cn.xzxy.lewy.framework.core.model.JsonResponseEntity;
import cn.xzxy.lewy.framework.mvc.exception.BusinessException;
import cn.xzxy.lewy.framework.mvc.exception.AbstractBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;

/**
 * @author lewy95
 **/
@Slf4j
@RestControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ExceptionConverterHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException ex,
                                                          WebRequest request) {
        log.error("businessErrorInfo:{}", ex.getMessage());
        HttpHeaders headers = new HttpHeaders();

        return buildResponseEntity(headers,
                JsonResponseEntity.buildBusinessError(ex.getMessage(), ex.getStatus()));
    }

    @ExceptionHandler(AbstractBusinessException.class)
    public JsonResponseEntity handleAssignableFromAbstractException(AbstractBusinessException ex) {
        log.error("ErrorInfo:{}", ex.getMessage());
        return JsonResponseEntity.buildBusinessError(ex.getMessage(), ex.getCode());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException runtimeException, WebRequest request) {
        log.error("runtimeExceptionInfo:{}", runtimeException.getMessage());
        HttpHeaders headers = new HttpHeaders();
        return buildResponseEntity(headers, JsonResponseEntity.buildBusinessError(runtimeException.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleSysException(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        log.error("ErrorInfo:{}", ex.getMessage());
        return buildResponseEntity(headers, JsonResponseEntity.buildSysTemError("系统异常"));
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Object> handleSQLException(Exception ex, WebRequest request) {
        log.error("ErrorInfo:{}", ex.getMessage());
        HttpHeaders headers = new HttpHeaders();
        return buildResponseEntity(headers, JsonResponseEntity.buildSysTemError("系统异常:数据库异常"));
    }

    private ResponseEntity<Object> buildResponseEntity(HttpHeaders headers, JsonResponseEntity appBaseResponse) {
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));

        return new ResponseEntity(appBaseResponse.toString(), headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        JsonResponseEntity appBaseResponse = handlerValidException(ex.getBindingResult());
        return buildResponseEntity(headers, appBaseResponse);
    }

    private JsonResponseEntity handlerValidException(BindingResult bindingResult) {
        StringBuilder stringBuilder = new StringBuilder();
        if (bindingResult.hasFieldErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                //stringBuilder.append("{").append(fieldError.getField()).append("}字段");
                stringBuilder.append(fieldError.getDefaultMessage()).append(",");
            }
        }
        if (stringBuilder.length() > 1) {
            stringBuilder.setLength(stringBuilder.length() - 1);
        }
        JsonResponseEntity appBaseResponse = JsonResponseEntity.buildBusinessError(stringBuilder.toString());
        return appBaseResponse;
    }

    @Override
    public ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("本方法不支持 {").append(ex.getMethod()).append("} 请求方法,");
        stringBuilder.append("建议使用 ").append(ArrayUtils.toString(ex.getSupportedMethods())).append(" 请求");
        return buildResponseEntity(headers, JsonResponseEntity.buildBusinessError(stringBuilder.toString()));
    }


    @Override
    public ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("本方法不支持 { ").append(ex.getContentType()).append(" } 对应的内容类型,");
        stringBuilder.append("建议使用 ").append(ArrayUtils.toString(ex.getSupportedMediaTypes())).append(" 媒体类型");
        return buildResponseEntity(headers, JsonResponseEntity.buildBusinessError(stringBuilder.toString()));
    }

    @Override
    public ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("无法接受该媒体类型，异常信息：").append(ex.getMessage());
        stringBuilder.append("建议使用 ").append(ArrayUtils.toString(ex.getSupportedMediaTypes())).append(" 媒体类型");
        return buildResponseEntity(headers, JsonResponseEntity.buildBusinessError(stringBuilder.toString()));
    }

    @Override
    public ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("缺少作为 { ").append(ex.getParameter().getNestedParameterType().getSimpleName())
                .append(" } 类型方法参数的URI模板变量 { ")
                .append(ex.getVariableName()).append(" } ");
        return buildResponseEntity(headers, JsonResponseEntity.buildBusinessError(stringBuilder.toString()));
    }

    @Override
    public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("缺少必需的参数 { ").append(ex.getParameterName())
                .append(" }，类型为 { ").append(ex.getParameterType()).append(" }");
        return buildResponseEntity(headers, JsonResponseEntity.buildBusinessError(stringBuilder.toString()));
    }

    @Override
    public ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("方法请求参数异常：").append(ex.getMessage());
        return buildResponseEntity(headers, JsonResponseEntity.buildBusinessError(stringBuilder.toString()));
    }

    @Override
    public ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("未能将类型为 { ").append(ex.getRequiredType()).append(" } 的属性值转换到属性 { ")
                .append(ex.getPropertyName()).append(" }");
        return buildResponseEntity(headers, JsonResponseEntity.buildBusinessError(stringBuilder.toString()));
    }

    @Override
    public ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("类型不匹配，异常信息：").append(ex.getMessage());
        return buildResponseEntity(headers, JsonResponseEntity.buildBusinessError(stringBuilder.toString()));
    }

    @Override
    public ResponseEntity handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(headers, JsonResponseEntity.buildBusinessError("无法从请求中读取需要的数据：" + ex.getMessage()));
    }

    @Override
    public ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("无法将数据写入请求中：").append(ex.getMessage());
        return buildResponseEntity(headers, JsonResponseEntity.buildBusinessError(stringBuilder.toString()));
    }

    @Override
    public ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("缺少部分请求参数 { ").append(ex.getRequestPartName());
        return buildResponseEntity(headers, JsonResponseEntity.buildBusinessError(stringBuilder.toString()));
    }

    @Override
    public ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("系统异常，异常信息：").append(ex.getBindingResult());
        return buildResponseEntity(headers, JsonResponseEntity.buildBusinessError(stringBuilder.toString()));
    }

    @Override
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("未找到处理程序，异常信息：").append(ex.getMessage());
        return buildResponseEntity(headers, JsonResponseEntity.buildBusinessError(stringBuilder.toString()));
    }

    @Override
    public ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("异步请求超时，异常信息：").append(ex.getMessage());
        return buildResponseEntity(headers, JsonResponseEntity.buildBusinessError(stringBuilder.toString()));
    }

    @Override
    public ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("系统异常，异常信息：").append(ex.getMessage());
        return buildResponseEntity(headers, JsonResponseEntity.buildBusinessError(stringBuilder.toString()));
    }
}
