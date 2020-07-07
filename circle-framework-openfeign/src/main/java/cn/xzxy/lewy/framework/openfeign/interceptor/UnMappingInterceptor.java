package cn.xzxy.lewy.framework.openfeign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

import static cn.xzxy.lewy.framework.openfeign.handler.UnMappingHandler.UM_MAPPING_FLAG;

/**
 * 非映射流程拦截器
 *
 * @author lewy95
 **/
@Slf4j
public class UnMappingInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        // 给openFeign的RequestTemplate添加请求头信息
        template.header(UM_MAPPING_FLAG, UM_MAPPING_FLAG);
    }
}
