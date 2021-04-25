package com.web.mvc.framework.init;

import com.web.mvc.framework.annotation.Value;
import com.web.mvc.framework.annotation.bean.Autowired;
import com.web.mvc.framework.annotation.bean.Bean;
import com.web.mvc.framework.annotation.component.Component;
import com.web.mvc.framework.annotation.component.Controller;
import com.web.mvc.framework.annotation.component.RestController;
import com.web.mvc.framework.annotation.component.Service;
import com.web.mvc.framework.constant.PropertiesConstant;
import com.web.mvc.framework.content.BeanContent;
import com.web.mvc.framework.content.PropertiesContent;
import com.web.mvc.framework.log.Log;
import com.web.mvc.framework.log.LogFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class InitBean {

    private Log logger = LogFactory.getSimpleLog(DefaultProperties.class);
    // 配置文件
    private PropertiesContent propertiesContent = PropertiesContent.getInstance();
    // 扫描指定包下的类名
    private List<String> classNames = new ArrayList<>();
    // bean容器
    private BeanContent beanContent = BeanContent.getInstance();

    public void init() {
        String packagePath = propertiesContent.getProp(PropertiesConstant.SCAN_PACKAGE);
        try {
            String path = this.getClass().getResource("").toString();//判断是否jar包启动
            logger.info(path);
            if (path.substring(0, 3).equals("jar")) {
                scanClassWithJar(path, packagePath);
            } else scanClass(packagePath);
            logger.info("扫描包成功!");
        } catch (Exception e) {
            e.printStackTrace();
            logger.err("扫描包失败!");
        }
        initInstance();// 初始化bean
        dependencyInjection();// 依赖注入
        valueInjection();// @value注入
        beanInjection();// @Bean
    }

    private void beanInjection(){
        try {
            for (String className : classNames) {
                Class clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(Component.class)){
                    Method[] methods = clazz.getMethods();
                    for (Method method:methods) {
                        if (method.isAnnotationPresent(Bean.class)){
                            Object obj = method.invoke(beanContent.getBean(clazz.getSimpleName()));
                            beanContent.setBean(obj.getClass().getSimpleName(),obj);
                        }
                    }
                }
            }
            logger.info("@Bean注入成功!");
        }catch (Exception e){
            e.printStackTrace();
            logger.err("@Bean注入失败!");
        }

    }

    private void valueInjection() {
        for (Map.Entry<String, Object> entry : beanContent.getEntrySet()) {
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                // 判断属性有没有需要被注入的
                if (!field.isAnnotationPresent(Value.class)) continue;
                field.setAccessible(true); // 强制授权
                Value value = field.getAnnotation(Value.class);
                try {
                    field.set(entry.getValue(), propertiesContent.getProp(value.value()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    logger.err("@Value注入失败!");
                }
            }
        }
        logger.info("@Value注入成功!");
    }


    private void dependencyInjection() {
        for (Map.Entry<String, Object> entry : beanContent.getEntrySet()) {
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                // 判断属性有没有需要被注入的
                if (!field.isAnnotationPresent(Autowired.class)) continue;
                field.setAccessible(true); // 强制授权
                try {
                    field.set(entry.getValue(), beanContent.getBean(field.getType().getSimpleName()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    logger.err("bean注入失败!");
                }
            }
        }
        logger.info("bean注入成功!");
    }

    private void initInstance() {
        try {
            for (String className : classNames) {
                Class clazz = Class.forName(className);
                Object instance = null;
                // 类型做key，实例作为value
                if (clazz.isAnnotationPresent(Controller.class) ||
                        clazz.isAnnotationPresent(RestController.class) ||
                        clazz.isAnnotationPresent(Service.class) ||
                        clazz.isAnnotationPresent(Component.class)) {
                    instance = clazz.newInstance();
                    beanContent.setBean(clazz.getSimpleName(), instance);
                }
            }
            logger.info("初始化bean成功!");
        } catch (Exception e) {
            e.printStackTrace();
            logger.err("初始化bean失败!");
        }
    }

    private void scanClass(String packagePath) {
        String path = this.getClass().getClassLoader().getResource("" + packagePath.replaceAll("\\.", "/")).getPath();
        URL url = null;
        try {// 有些系统需要指定字符集，否则会乱码失效
            path = "file:" + URLDecoder.decode(path, propertiesContent.getProp(PropertiesConstant.ENCODING));
            url = new URL(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (File file : new File(url.getFile()).listFiles()) {
            if (file.isDirectory()) {
                scanClass(packagePath + "." + file.getName());
            } else if (file.getName().contains(".class")) {
                classNames.add((packagePath + "." + file.getName().replaceAll(".class", "")));
            }
        }
    }

    // jar包形式的扫包
    private void scanClassWithJar(String jarPath, String packagePath) {

        String[] jarInfo = jarPath.split("!");
        String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
        packagePath = packagePath.replaceAll("\\.", "/");
        try {
            JarFile jarFile = new JarFile(jarFilePath);
            Enumeration<JarEntry> entries = jarFile.entries();// 获取jar包中所有文件
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String entryName = jarEntry.getName();
                if (entryName.endsWith(".class")) {
                    if (entryName.startsWith(packagePath)) {
                        entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                        classNames.add(entryName);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
