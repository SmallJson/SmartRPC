package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务框架提供注解，类上被标记为该类，该类将当成服务发布
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface smartService {
    /**
     * 服务名称，
     * @return
     */
    public String name();

    /**
     * 服务版本号
     */
    public int version() default  1;
}
