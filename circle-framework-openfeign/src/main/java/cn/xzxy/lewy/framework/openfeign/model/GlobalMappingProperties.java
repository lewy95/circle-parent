package cn.xzxy.lewy.framework.openfeign.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 透传请求头配置实体类
 *
 * @author lewy95
 */
@ConditionalOnProperty(name = "circle.openfeign.global-mapping.enabled", havingValue = "true")
@Component
@ConfigurationProperties(prefix = "circle.openfeign.global-mapping")
@Getter
@Setter
public class GlobalMappingProperties {

    /**
     * 开启映射
     */
    private Boolean enabled;
}


