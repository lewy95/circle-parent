package cn.xzxy.lewy.framework.mvc.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;

/**
 * @author lewy95
 **/
@Configuration
@ConditionalOnProperty(name = "circle.swagger.enabled")
@Import({
        Swagger2DocumentationConfiguration.class})
public class SwaggerConfiguration {

}