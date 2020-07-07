package cn.xzxy.lewy.framework.openfeign.handler;

import cn.xzxy.lewy.framework.openfeign.interceptor.MdpStageBusInterceptor;
import cn.xzxy.lewy.framework.openfeign.model.MappingProperties;
import cn.xzxy.lewy.framework.openfeign.model.OpenFeignSignProperties;
import feign.RequestInterceptor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;

/**
 * 走映射流程处理类 >>> 装配mappingInterceptor
 *
 * @author lewy95
 */
@Slf4j
@Setter
@Getter
public class MappingHandler {

    @Resource
    private MappingProperties mappingProperties;

    @Resource
    private OpenFeignSignProperties openFeignSignProperties;

    @Order(9)
    @Bean
    public RequestInterceptor mappingInterceptor() {
        log.info("装配 mappingInterceptor ...");
        MdpStageBusInterceptor midStageBusInterceptor = new MdpStageBusInterceptor();
        midStageBusInterceptor.setAppId(getOpenFeignSignProperties().getAppId());
        midStageBusInterceptor.setAppKey(getOpenFeignSignProperties().getAppKey());
        midStageBusInterceptor.setMidStageContextPath(getOpenFeignSignProperties().getMidPath());
        midStageBusInterceptor.setMap(mappingProperties.getMap());
        return midStageBusInterceptor;
    }


}
