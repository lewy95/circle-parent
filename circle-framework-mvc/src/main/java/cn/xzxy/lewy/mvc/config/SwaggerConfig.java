package cn.xzxy.lewy.mvc.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;

/**
 * @author lewy95
 **/
@Slf4j
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Resource
    private SwaggerProperties swaggerProperties;

    @ConditionalOnProperty(value = "circle.swagger.enabled")
    @Bean
    public Docket createRestApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                .paths(PathSelectors.any())
                .build();

        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .version(swaggerProperties.getVersion())
                .contact(new Contact(
                        swaggerProperties.getContact() != null ? swaggerProperties.getContact().getName() : "lewy",
                        swaggerProperties.getContact() != null ? swaggerProperties.getContact().getUrl() : "/",
                        swaggerProperties.getContact() != null ? swaggerProperties.getContact().getEmail() : "lewy95@aliyun.com"))
                .build();
    }
}
