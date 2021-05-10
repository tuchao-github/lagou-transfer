package com.lagou.edu.annotation;
import java.lang.annotation.*;
/**
 *自定义Service注解
 * @author Administrator
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MyService {
    String value() default "";
}
