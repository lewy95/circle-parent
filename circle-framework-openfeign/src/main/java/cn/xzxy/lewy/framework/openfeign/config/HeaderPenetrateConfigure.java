package cn.xzxy.lewy.framework.openfeign.config;

import cn.xzxy.lewy.framework.openfeign.handler.HeaderPenetratedInterceptor;
import cn.xzxy.lewy.framework.openfeign.model.PenetrableHeaderProperties;
import feign.RequestInterceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 设置请求头透传配置类
 *
 * @author lewy95
 */
@Configuration
@EnableConfigurationProperties(PenetrableHeaderProperties.class)
public class HeaderPenetrateConfigure {

    @Bean
    @Order()
    public RequestInterceptor headerPenetratedInterceptor(PenetrableHeaderProperties headerProperties) {
        return new HeaderPenetratedInterceptor(headerProperties);
    }

}
