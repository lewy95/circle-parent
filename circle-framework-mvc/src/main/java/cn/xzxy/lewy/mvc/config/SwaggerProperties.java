package cn.xzxy.lewy.mvc.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lewy95
 **/
@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "circle.swagger")
public class SwaggerProperties {

    private Boolean enabled = false;
    private String title;
    private String basePackage;
    private String version;
    private String description;
    private Contact contact;

    @Setter
    @Getter
    public static class Contact {
        private String name;
        private String url;
        private String email;

    }
}
