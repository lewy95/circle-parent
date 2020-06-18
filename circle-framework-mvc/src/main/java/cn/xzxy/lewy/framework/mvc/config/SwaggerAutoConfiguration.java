package cn.xzxy.lewy.framework.mvc.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author lewy95
 **/
@Configuration
@Import({
        SwaggerConfiguration.class
})
public class SwaggerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SwaggerProperties swaggerProperties() {
        return new SwaggerProperties();
    }


    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "circle.swagger.enabled")
    public Docket createRestApi(SwaggerProperties swaggerProperties) {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title(swaggerProperties.getTitle())
                        .description(swaggerProperties.getDescription())
                        .version(swaggerProperties.getVersion())
                        .contact(new Contact(
                                swaggerProperties.getContact() != null ? swaggerProperties.getContact().getName() : "lewy",
                                swaggerProperties.getContact() != null ? swaggerProperties.getContact().getUrl() : "/",
                                swaggerProperties.getContact() != null ? swaggerProperties.getContact().getEmail() : "lewy95@aliyun.com"))
                        .build())
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                .paths(PathSelectors.any())
                .build();
    }
}
