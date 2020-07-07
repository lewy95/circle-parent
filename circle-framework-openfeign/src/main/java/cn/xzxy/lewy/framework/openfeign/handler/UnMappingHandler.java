package cn.xzxy.lewy.framework.openfeign.handler;


import cn.xzxy.lewy.framework.openfeign.interceptor.UnMappingInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

/**
 * 走非映射流程处理类 >>
 *
 * @author lewy95
 **/
@Slf4j
public class UnMappingHandler {

    // 不映射的flag
    public static final String UM_MAPPING_FLAG = "unmapping";


    @Order(1)
    @Bean
    public UnMappingInterceptor unMappingInterceptor() {
        return new UnMappingInterceptor();
    }

}
