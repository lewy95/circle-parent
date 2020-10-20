package cn.xzxy.lewy.framework.kafka.config;

import com.mongodb.MongoClientOptions;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lewy95
 */
@Configuration
@ComponentScan({"cn.xzxy.lewy.framework.kafka"})
@EnableAsync
@EnableMongoRepositories({"cn.xzxy.lewy.framework.kafka"})
@EnableConfigurationProperties({LogServiceThreadPoolExecutorProperties.class, MongoClientOptionsProperties.class})
public class KafkaMessageConfiguration {

    @Autowired
    KafkaProperties kafkaProperties;

    @Autowired
    MongoClientOptionsProperties mongoClientOptionsProperties;

    @Autowired
    LogServiceThreadPoolExecutorProperties logServiceThreadPoolExecutorProperties;

    @Bean
    public AdminClient getAdminClient(KafkaAdmin kafkaAdmin) {
        return AdminClient.create(kafkaAdmin.getConfig());
    }

    @Bean
    public ProducerFactory<?, ?> getProducerFactory() {
        this.kafkaProperties.getProperties().put("partitioner.class", "cn.xzxy.lewy.framework.kafka.domain.sender.service.AveragePartitioner");
        DefaultKafkaProducerFactory<?, ?> factory = new DefaultKafkaProducerFactory(this.kafkaProperties.buildProducerProperties());
        String transactionIdPrefix = this.kafkaProperties.getProducer().getTransactionIdPrefix();
        if (transactionIdPrefix != null) {
            factory.setTransactionIdPrefix(transactionIdPrefix);
        }

        return factory;
    }

    @Bean({"logServiceThreadPoolExecutor"})
    public ThreadPoolTaskExecutor getThreadPoolExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(this.logServiceThreadPoolExecutorProperties.getCorePoolSize());
        threadPoolTaskExecutor.setMaxPoolSize(this.logServiceThreadPoolExecutorProperties.getMaxPoolSize());
        threadPoolTaskExecutor.setQueueCapacity(this.logServiceThreadPoolExecutorProperties.getQueueCapacity());
        threadPoolTaskExecutor.setKeepAliveSeconds(this.logServiceThreadPoolExecutorProperties.getKeepAliveSeconds());
        threadPoolTaskExecutor.setAwaitTerminationSeconds(this.logServiceThreadPoolExecutorProperties.awaitTerminationSeconds);
        threadPoolTaskExecutor.setThreadNamePrefix("logServiceThreadPoolExecutor");
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    @Bean
    @ConditionalOnMissingClass({"com.mongodb.MongoClientOptions"})
    public MongoClientOptions getMongoClientOptions() {
        MongoClientOptions.Builder builder = MongoClientOptions.builder();
        builder.connectionsPerHost(this.mongoClientOptionsProperties.getMaxConnectionsPerHost());
        builder.minConnectionsPerHost(this.mongoClientOptionsProperties.getMinConnectionsPerHost());
        builder.maxWaitTime(this.mongoClientOptionsProperties.getMaxWaitTime());
        builder.maxConnectionIdleTime(this.mongoClientOptionsProperties.getMaxConnectionIdleTime());
        builder.maxConnectionLifeTime(this.mongoClientOptionsProperties.getMaxConnectionLifeTime());
        builder.connectTimeout(this.mongoClientOptionsProperties.getConnectTimeout());
        builder.socketTimeout(this.mongoClientOptionsProperties.getSocketTimeout());
        return builder.build();
    }
}
