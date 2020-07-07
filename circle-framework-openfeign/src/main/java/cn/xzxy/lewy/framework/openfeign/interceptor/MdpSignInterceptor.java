package cn.xzxy.lewy.framework.openfeign.interceptor;

import cn.xzxy.lewy.framework.core.encrpt.AES;
import cn.xzxy.lewy.framework.openfeign.handler.MdpAccessHandler;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * 中台签名验证拦截器
 *
 * @author lewy95
 **/
@Slf4j
public class MdpSignInterceptor implements MdpAccessHandler {
    public MdpSignInterceptor(String appKey, String appId, Map<String, String> mappingMap) {
        this.appId = appId;
        this.appKey = appKey;
        this.mappingMap = mappingMap;
        Assert.hasText(appId, "[appId]不能为空,请确认[appId]是否配置成功");
        Assert.hasText(appId, "[appKey]不能为空,请确认[appKey]是否配置成功");
        Assert.notEmpty(mappingMap, "映射map为空,无法开启映射");
    }

    /**
     * 中台 应用key
     */
    private String appKey;
    /**
     * 中台 应用 id
     */
    private String appId;

    private Map<String, String> mappingMap;


    private String sign(String appKey, String appId, String appName) throws Exception {
        String securityKey = appKey.replaceAll("-", "");
        AES aes = new AES();
        String encryptContent = aes.encrypt(securityKey, appId + appName + getUnixCurrentTime());
        String result = StringUtils.replace(encryptContent, "\r", "");
        result = StringUtils.replace(result, "\n", "");
        return result;
    }

    private String getUnixCurrentTime() {
        return String.valueOf(System.currentTimeMillis() / 1000L);
    }

    @Override
    public void doInterception(RequestTemplate requestTemplate) {

        String apiId = this.mappingMap.get(requestTemplate.path().replaceAll("/", ""));
        if (apiId == null) {
            log.warn("映射路径[{}}]没有映射id(服务中台提供的appId),请及时更新配置!!!", requestTemplate.url());
            return;
        }
        try {
            String signature = sign(this.appKey, this.appId, apiId);
            requestTemplate.header("signature", signature);
            requestTemplate.header("appid", this.appId);
            requestTemplate.header("apiname", apiId);
        } catch (Exception e) {
            log.error("error {}", e.getMessage());
            String javaVersion = System.getProperty("java.version");
            throw new RuntimeException("签名失败,您本地JDK版本为[" + javaVersion + "]," +
                    "请检查您的JDK中的JCE是否符合要求,或者检查配置:[appId,appKey]等");
        }

    }
}
