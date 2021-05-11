package com.lagou.edu.factory;

import com.alibaba.druid.util.StringUtils;
import com.lagou.edu.annotation.MyAutowired;
import com.lagou.edu.annotation.MyService;
import com.lagou.edu.annotation.MyTransactional;
import org.reflections.Reflections;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 工厂类，生产对象（使用反射技术）
 */
public class BeanFactory {
    /**
     * 任务一：扫描包，通过反射技术实例化对象并且存储待用（map集合）
     * 任务二：对外提供获取实例对象的接口（根据id获取）
     */
    /**
     * 存储对象
      */
    private static Map<String,Object> map = new HashMap<>();


    static {
        try{
            //任务一、扫描包，通过反射技术实例化对象并且存储待用（map集合）

            //通过反射技术，扫描包并获取反射对象集合
            Reflections edus = new Reflections("com.lagou.edu");
            Set<Class<?>> clazzs = edus.getTypesAnnotatedWith(MyService.class);
            //遍历对象集合
            for (Class<?> clazz:clazzs) {
                // 获取实例化对象
                Object object = clazz.newInstance();
                MyService service = clazz.getAnnotation(MyService.class);

                //判断MyService注解上是否有自定义对象ID
                if(StringUtils.isEmpty(service.value())){
                    //由于getName获取的是全限定类名，所以要分割去掉前面包名部分
                    String[] names = clazz.getName().split("\\.");
                    map.put(names[names.length-1], object);
                }else{
                    map.put(service.value(), object);
                }
            }
            //维护对象之间依赖关系
            for(Map.Entry<String, Object> entrySet: map.entrySet()) {
                Object obj = entrySet.getValue();
                Class clazz = obj.getClass();
                //获取每个类的所有属性
                Field[] fields = clazz.getDeclaredFields();
                //遍历属性，确认是否有使用Autowired注解，有使用注解则需要完成注入
                for (Field field : fields) {
                    //判断是否使用注解的参数，有使用注解则注入
                    if (field.isAnnotationPresent(MyAutowired.class)
                            && field.getAnnotation(MyAutowired.class).value()) {
                        String[] names = field.getType().getName().split("\\.");
                        String name = names[names.length - 1];
                        Method[] methods = clazz.getMethods();
                        for (int j = 0; j < methods.length; j++) {
                            Method method = methods[j];
                            // 该方法就是 setAccountDao(AccountDao accountDao)
                            if (method.getName().equalsIgnoreCase("set" + name)) {
                                method.invoke(obj, map.get(name));
                            }
                        }
                    }
                }
                //判断当前类是否有Transactional注解，若有则使用代理对象
                if(clazz.isAnnotationPresent(MyTransactional.class)){
                    //获取代理工厂
                    ProxyFactory proxyFactory = (ProxyFactory) BeanFactory.getBean("proxyFactory");
                    //获取类c实现的所有接口
                    Class[] face = clazz.getInterfaces();
                    //判断对象是否实现接口
                    if(face!=null&&face.length>0){
                        //实现使用JDK
                        obj = proxyFactory.getJdkProxy(obj);
                    }else{
                        //没实现使用CGLIB
                        obj = proxyFactory.getCglibProxy(obj);
                    }
                }
                // 把处理之后的object重新放到map中
                map.put(entrySet.getKey(),obj);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 任务二：对外提供获取实例对象的接口（根据id获取）
     */
    public static  Object getBean(String id) {
        return map.get(id);
    }

}
