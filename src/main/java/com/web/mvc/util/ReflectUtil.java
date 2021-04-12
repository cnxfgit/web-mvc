package com.web.mvc.util;

import com.web.mvc.content.BeanContent;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectUtil {

    // bean容器
    private static BeanContent beanContent = BeanContent.getInstance();

    public static Object invoke(Method method, String beanName, Object... args){
        Object object = null;
        try {
            object = method.invoke(beanContent.getBean(beanName),args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static void setField(Field field, Object instance, Object value){
        try {
            field.set(instance,value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static List<Method> methods(Method[] methods){
        List<Method> list = new ArrayList();
        for (Method method:methods) {
            list.add(method);
        }
        return list;
    }

    public static Object newInstance(Class clazz){
        Object obj = null;
        try {
            obj = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
