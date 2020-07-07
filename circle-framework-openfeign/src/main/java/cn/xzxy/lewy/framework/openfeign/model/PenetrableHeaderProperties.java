package cn.xzxy.lewy.framework.openfeign.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * 透传请求头配置实体类
 *
 * @author lewy95
 */
@Data
@ConfigurationProperties(prefix = "circle.openfeign.headers")
public class PenetrableHeaderProperties {

    /**
     * RestTemplate 和 Feign 透传到下层的 Headers 名称表达式
     */
    @Nullable
    private String pattern = "X-*";

    /**
     * RestTemplate 和 Feign 透传到下层的 Headers 名称列表
     */
    private List<String> allowed = Arrays.asList("X-Real-IP", "x-forwarded-for", "authorization", "Authorization", "token", "Token", "tgt", "Tgt");
}
