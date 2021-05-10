package com.lagou.edu.annotation;
import java.lang.annotation.*;
/**
 *自定义Autowired注解
 * @author Administrator
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyAutowired {
    boolean value() default true;
}