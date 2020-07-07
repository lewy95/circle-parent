package cn.xzxy.lewy.framework.openfeign.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lewy95
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties("circle.openfeign.sign")
public class OpenFeignSignProperties {
    private String appId;
    private String appKey;
    private String midPath;
}