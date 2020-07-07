package cn.xzxy.lewy.framework.openfeign.http;

import cn.xzxy.lewy.framework.openfeign.model.PenetrableHeaderProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author lewy95
 */
public class HttpHeadersContextHolder {
    private static final ThreadLocal<HttpHeaders> HTTP_HEADERS_HOLDER = new NamedThreadLocal<>("circle HttpHeaders");

    /**
     * 请求和转发的ip
     */
    private static final String[] ALLOW_HEADS = new String[]{
            "X-Real-IP", "x-forwarded-for",
            "authorization", "Authorization",
            "token", "Token",
            "tgt", "Tgt"
    };

    static void set(HttpHeaders httpHeaders) {
        HTTP_HEADERS_HOLDER.set(httpHeaders);
    }

    @Nullable
    public static HttpHeaders get() {
        return HTTP_HEADERS_HOLDER.get();
    }

    /**
     * 透传请求头
     */
    public static Map<String, String> toHeaders(PenetrableHeaderProperties headerProperties) {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        Map<String, String> headers = new HashMap<>(ALLOW_HEADS.length);
        List<String> allowHeadsList = new ArrayList<>(Arrays.asList(ALLOW_HEADS));
        // 传递请求头
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            List<String> allowed = headerProperties.getAllowed();
            String pattern = headerProperties.getPattern();
            while (headerNames.hasMoreElements()) {
                String key = headerNames.nextElement();
                // 只支持配置的 header
                if (allowHeadsList.contains(key) || allowed.contains(key) || PatternMatchUtils.simpleMatch(pattern, key)) {
                    String values = request.getHeader(key);
                    // header value 不为空的 传递
                    if (StringUtils.isNotBlank(values)) {
                        headers.put(key, values);
                    }
                }
            }
        }
        return headers.isEmpty() ? null : headers;
    }

    /**
     * 获取 HttpServletRequest
     */
    private static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return (requestAttributes == null) ? null : ((ServletRequestAttributes) requestAttributes).getRequest();
    }
}
