package cn.xzxy.lewy.framework.openfeign.interceptor;

import cn.xzxy.lewy.framework.openfeign.handler.MdpAccessHandler;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 中台路径校验拦截器
 *
 * @author lewy95
 **/
@Slf4j
public class MdpPathMapping implements MdpAccessHandler {
    private static final String HTTP_SCHEMA = "http://";
    private static final String HTTPS_SCHEMA = "https://";
    private String accessDomain;

   public MdpPathMapping(String domain, String contextPath) {
        if (StringUtils.isNotBlank(contextPath)) {
            contextPath = "/";
        }
        StringBuilder accessDomainBuilder = new StringBuilder();
        if (!checkSchema(domain)) {
            accessDomainBuilder.append(HTTP_SCHEMA);
        }
        accessDomain = accessDomainBuilder.append(domain).append(contextPath).toString();
    }

  public MdpPathMapping(String accessDomain) {

        if (!checkSchema(accessDomain)) {
            throw new IllegalArgumentException("错误的URL格式" + accessDomain);
        }
        this.accessDomain = accessDomain;
    }

    boolean checkSchema(String accessDomain) {
        if (accessDomain != null) {
            return accessDomain.contains(HTTP_SCHEMA) || accessDomain.contains(HTTPS_SCHEMA);
        } else {
            throw new IllegalArgumentException("访问路径不能为空: [" + null + "]");
        }
    }

    @Override
    public void doInterception(RequestTemplate requestTemplate) {
        if (log.isDebugEnabled()) {
            log.debug("raw path [{}]", requestTemplate.path());
        }
        requestTemplate.target(accessDomain);
        requestTemplate.uri("/");
        if (log.isDebugEnabled()) {
            log.debug("after mapping path [{}]", requestTemplate.path());
        }
    }
}
