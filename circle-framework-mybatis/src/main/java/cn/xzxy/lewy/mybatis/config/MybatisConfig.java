package cn.xzxy.lewy.mybatis.config;

import cn.xzxy.lewy.mybatis.interceptor.SqlExecuteCostInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lewy95
 **/
@Slf4j
@Configuration
public class MybatisConfig {
    @Bean
    ConfigurationCustomizer mybatisConfigurationCustomizer() {
        log.info("装配 ConfigurationCustomizer ");
        return configuration -> configuration.addInterceptor(new SqlExecuteCostInterceptor());
    }
}
