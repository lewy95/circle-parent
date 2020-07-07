package cn.xzxy.lewy.framework.openfeign.interceptor;

import cn.xzxy.lewy.framework.openfeign.handler.MdpAccessHandler;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cn.xzxy.lewy.framework.openfeign.handler.UnMappingHandler.UM_MAPPING_FLAG;

/**
 * @author lewy95
 */
@Slf4j
@Setter
public class MdpStageBusInterceptor implements RequestInterceptor {

    // 映射路径
    private Map<String, String> map;
    // 中台应用key
    private String appKey;
    // 中台应用 id
    private String appId;
    // 中台地址
    private String midStageContextPath;

    private static List<MdpAccessHandler> MAPPING_INTERCEPTOR = new ArrayList<>(4);


    private void init() {
        if (MAPPING_INTERCEPTOR.isEmpty()) {
            // 需要按照如下顺序
            MAPPING_INTERCEPTOR.add(new MdpSignInterceptor(appKey, appId, map));
            MAPPING_INTERCEPTOR.add(new MdpPathMapping(midStageContextPath));
        }
    }

    @Override
    public void apply(RequestTemplate template) {
        Map<String, Collection<String>> headers =  template.headers();
        if(headers.containsKey(UM_MAPPING_FLAG)) {
            return;
        }
        init();
        for (MdpAccessHandler mappingInterceptor : MAPPING_INTERCEPTOR) {
            mappingInterceptor.doInterception(template);
        }
    }

}
