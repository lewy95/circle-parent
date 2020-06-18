package cn.xzxy.lewy.framework.logback.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lewy95
 */
@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "circle.log")
public class CircleLogbackConfigProperties {

    private String applicationName;

    private String moduleName;

    private String level;

    private String filePath;
}