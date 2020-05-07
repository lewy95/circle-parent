package cn.xzxy.lewy.mvc.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author lewy95
 **/
@Import(SwaggerConfig.class)
@Configuration
@ConditionalOnProperty(value = "circle.swagger.enabled")
@EnableConfigurationProperties(SwaggerProperties.class)
public class CircleSwaggerAutoConfiguration {
}
