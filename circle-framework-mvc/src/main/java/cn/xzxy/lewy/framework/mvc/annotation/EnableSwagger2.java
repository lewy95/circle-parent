package cn.xzxy.lewy.framework.mvc.annotation;

import cn.xzxy.lewy.framework.mvc.config.SwaggerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 重写Swagger2启动注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({SwaggerAutoConfiguration.class})
public @interface EnableSwagger2 {

}
