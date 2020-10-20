package cn.xzxy.lewy.framework.cache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lewy95
 */
@ConfigurationProperties(prefix = "circle.config.redis.redisson")
@Data
public class RedissonConfigProperties {
    String configPath;
    String fastJsonAcceptPackage;
}
