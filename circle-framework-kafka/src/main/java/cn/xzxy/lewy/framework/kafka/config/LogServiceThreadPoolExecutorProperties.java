package cn.xzxy.lewy.framework.kafka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author lewy95
 */
@ConfigurationProperties(
        prefix = "circle.config.kafka.log.executor"
)
@EnableConfigurationProperties(LogServiceThreadPoolExecutorProperties.class)
public class LogServiceThreadPoolExecutorProperties {
    Integer corePoolSize = 50;
    Integer maxPoolSize = 500;
    Integer queueCapacity = 1000;
    Integer keepAliveSeconds = 60;
    Integer awaitTerminationSeconds = 60;


    public Integer getCorePoolSize() {
        return this.corePoolSize;
    }

    public Integer getMaxPoolSize() {
        return this.maxPoolSize;
    }

    public Integer getQueueCapacity() {
        return this.queueCapacity;
    }

    public Integer getKeepAliveSeconds() {
        return this.keepAliveSeconds;
    }

    public Integer getAwaitTerminationSeconds() {
        return this.awaitTerminationSeconds;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public void setQueueCapacity(Integer queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public void setKeepAliveSeconds(Integer keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public void setAwaitTerminationSeconds(Integer awaitTerminationSeconds) {
        this.awaitTerminationSeconds = awaitTerminationSeconds;
    }
}
