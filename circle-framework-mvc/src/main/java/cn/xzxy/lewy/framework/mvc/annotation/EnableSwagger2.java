package cn.xzxy.lewy.framework.mvc.annotation;

import cn.xzxy.lewy.framework.mvc.config.SwaggerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 重写Swagger2启动注解，其他项目要使用Swagger2时只要使用该注解就可以了
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({SwaggerAutoConfiguration.class})
public @interface EnableSwagger2 {

}
