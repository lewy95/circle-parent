package cn.xzxy.lewy.framework.openfeign.config;

import cn.xzxy.lewy.framework.openfeign.handler.MappingHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author lewy95
 *
 * 开启全局映射（所有API接口都走中台 + 签名的方式）
 * 开启方式: circle.openfeign.global-mapping.enabled = true
 **/
@Import(MappingHandler.class)
@Configuration
@ConditionalOnProperty(name = "circle.openfeign.global-mapping.enabled", havingValue = "true")
public class GlobalMappingConfig {
}
