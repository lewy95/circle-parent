package cn.xzxy.lewy.framework.openfeign.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author lewy95
 */
@ConditionalOnProperty(value = "circle.openfeign.mapping.enabled", havingValue = "true")
@Component
@ConfigurationProperties(prefix = "circle.openfeign.mapping")
@PropertySource(value = {"classpath:mappers.properties"}, encoding = "UTF-8")
@Setter
@Getter
public class MappingProperties {

    /**
     * 映射
     */
    private Map<String, String> map;
    /**
     * 开启映射
     */
    private Boolean enabled;

}

