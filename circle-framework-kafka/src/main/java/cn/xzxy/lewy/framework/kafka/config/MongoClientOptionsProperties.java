package cn.xzxy.lewy.framework.kafka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author lewy95
 */
@ConfigurationProperties(
        prefix = "circle.config.kafka.mongo"
)
@EnableConfigurationProperties(MongoClientOptionsProperties.class)
public class MongoClientOptionsProperties {
    private Integer minConnectionsPerHost = 10;
    private Integer maxConnectionsPerHost = 20;
    private Integer maxWaitTime = 120000;
    private Integer maxConnectionIdleTime = 60000;
    private Integer maxConnectionLifeTime = 0;
    private Integer connectTimeout = 10000;
    private Integer socketTimeout = 0;

    public Integer getMinConnectionsPerHost() {
        return this.minConnectionsPerHost;
    }

    public Integer getMaxConnectionsPerHost() {
        return this.maxConnectionsPerHost;
    }

    public Integer getMaxWaitTime() {
        return this.maxWaitTime;
    }

    public Integer getMaxConnectionIdleTime() {
        return this.maxConnectionIdleTime;
    }

    public Integer getMaxConnectionLifeTime() {
        return this.maxConnectionLifeTime;
    }

    public Integer getConnectTimeout() {
        return this.connectTimeout;
    }

    public Integer getSocketTimeout() {
        return this.socketTimeout;
    }

    public void setMinConnectionsPerHost(Integer minConnectionsPerHost) {
        this.minConnectionsPerHost = minConnectionsPerHost;
    }

    public void setMaxConnectionsPerHost(Integer maxConnectionsPerHost) {
        this.maxConnectionsPerHost = maxConnectionsPerHost;
    }

    public void setMaxWaitTime(Integer maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    public void setMaxConnectionIdleTime(Integer maxConnectionIdleTime) {
        this.maxConnectionIdleTime = maxConnectionIdleTime;
    }

    public void setMaxConnectionLifeTime(Integer maxConnectionLifeTime) {
        this.maxConnectionLifeTime = maxConnectionLifeTime;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
    }
}