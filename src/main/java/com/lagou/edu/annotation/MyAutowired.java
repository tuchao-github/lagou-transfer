package com.lagou.edu.annotation;
import java.lang.annotation.*;
/**
 *自定义Autowired注解
 * @author Administrator
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAutowired {
    boolean value() default true;
}