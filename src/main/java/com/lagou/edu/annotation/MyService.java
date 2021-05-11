package com.lagou.edu.annotation;
import java.lang.annotation.*;
/**
 *自定义Service注解
 * @author Administrator
 */
@Retention(RetentionPolicy.RUNTIME)//注解在什么范围内有效,RUNTIME:在运行时有效,这样注解处理器可以通过反射，获取到该注解的属性值，从而去做一些运行时的逻辑处理
@Target(ElementType.TYPE)//描述所修饰的对象范围，TYPE:用于描述类、接口(包括注解类型) 或enum声明
public @interface MyService {
    String value() default "";
}
