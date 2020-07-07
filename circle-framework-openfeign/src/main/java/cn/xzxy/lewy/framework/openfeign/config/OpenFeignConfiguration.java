package cn.xzxy.lewy.framework.openfeign.config;

import cn.xzxy.lewy.framework.openfeign.model.MappingProperties;
import cn.xzxy.lewy.framework.openfeign.model.OpenFeignSignProperties;
import feign.Logger;
import feign.slf4j.Slf4jLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author lewy95
 **/
@Slf4j
@ConditionalOnProperty(value = "circle.openfeign.mapping.enabled", havingValue = "true")
@Import({GlobalMappingConfig.class, HeaderPenetrateConfigure.class})
@Configuration
@EnableConfigurationProperties({MappingProperties.class, OpenFeignSignProperties.class})
@EnableFeignClients(basePackages = "cn.xzxy.lewy")
public class OpenFeignConfiguration {

    @Bean
    public Logger logger() {
        log.info("装配 openfeign logger ...");
        return new Slf4jLogger();
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        log.info("装配 openfeign feignLoggerLevel ...");
        if (log.isDebugEnabled()) {
            return Logger.Level.FULL;
        }
        return Logger.Level.NONE;
    }
}
