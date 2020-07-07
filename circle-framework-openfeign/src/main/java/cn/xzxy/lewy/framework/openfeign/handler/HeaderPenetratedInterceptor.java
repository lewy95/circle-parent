package cn.xzxy.lewy.framework.openfeign.handler;

import cn.xzxy.lewy.framework.openfeign.http.HttpHeadersContextHolder;
import cn.xzxy.lewy.framework.openfeign.model.PenetrableHeaderProperties;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 设置请求头透传配置类
 *
 * @author lewy95
 */
@Slf4j
public class HeaderPenetratedInterceptor implements RequestInterceptor {

    private PenetrableHeaderProperties headerProperties;

    public HeaderPenetratedInterceptor(PenetrableHeaderProperties headerProperties) {
        this.headerProperties = headerProperties;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Map<String, String> allowedHeaders = HttpHeadersContextHolder.toHeaders(headerProperties);
        if (MapUtils.isNotEmpty(allowedHeaders)) {
            allowedHeaders.forEach(requestTemplate::header);
        }
        if (log.isDebugEnabled()) {
            Map<String, Collection<String>> headers = requestTemplate.headers();
            headers.forEach((k, v) -> log.debug("header: {} -> {}", k, CollectionUtils.isEmpty(v) ? null : v.stream().findFirst()));
        }

    }
}
