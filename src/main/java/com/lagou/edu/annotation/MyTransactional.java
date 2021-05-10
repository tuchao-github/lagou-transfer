package com.lagou.edu.annotation;
import java.lang.annotation.*;
/**
 * 自定义Transactional注解
 * @author Administrator
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyTransactional {
    String value() default "TransactionManager";
}