package com.web.mvc.init;

import com.web.mvc.annotation.bean.Autowired;
import com.web.mvc.annotation.component.Component;
import com.web.mvc.annotation.component.Controller;
import com.web.mvc.annotation.component.RestController;
import com.web.mvc.annotation.component.Service;
import com.web.mvc.constant.PropertiesConstant;
import com.web.mvc.content.BeanContent;
import com.web.mvc.content.PropertiesContent;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class InitBean {

    private static Logger logger = Logger.getLogger(DefaultProperties.class.getName());
    // 配置文件
    private PropertiesContent propertiesContent = PropertiesContent.getInstance();
    // 扫描指定包下的类名
    private List<String> classNames = new ArrayList<>();
    // bean容器
    private BeanContent beanContent = BeanContent.getInstance();

    public void init(){
        String packagePath = propertiesContent.getProp(PropertiesConstant.SCAN_PACKAGE);
        try {
            scanClass(packagePath);
            logger.warning("==>扫描包成功!");
        }catch (Exception e){
            e.printStackTrace();
            logger.warning("==>扫描包失败!");
        }
        initInstance();
        dependencyInjection();
    }

    private void dependencyInjection(){
        for (Map.Entry<String, Object> entry: beanContent.getEntrySet()){
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields){
                // 判断属性有没有需要被注入的
                if (!field.isAnnotationPresent(Autowired.class)) continue;
                field.setAccessible(true); // 强制授权
                try {
                    field.set(entry.getValue(), beanContent.getBean(field.getType().getSimpleName()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    logger.info("==>bean注入失败!");
                }
            }
        }
        logger.info("==>bean注入成功!");
    }

    private void initInstance(){
        try{
            for (String className: classNames) {
                Class clazz = Class.forName(className);
                Object instance = null;
                // 类型做key，实例作为value
                if (clazz.isAnnotationPresent(Controller.class)||
                    clazz.isAnnotationPresent(RestController.class)||
                    clazz.isAnnotationPresent(Service.class)||
                    clazz.isAnnotationPresent(Component.class)) {
                    instance = clazz.newInstance();
                    beanContent.setBean(clazz.getSimpleName(),instance);
                }
            }
            logger.info("==>初始化bean成功!");
        }catch (Exception e){
            e.printStackTrace();
            logger.info("==>初始化bean失败!");
        }
    }

    private void scanClass(String packagePath){
        String path = this.getClass().getClassLoader().getResource("/" + packagePath.replaceAll("\\.", "/")).getPath();
        URL url = null;
        try {// 有些系统需要指定字符集，否则会乱码失效
            path = "file:"+ URLDecoder.decode(path,"utf-8");
            url = new URL(path);
        }catch (Exception e){
            e.printStackTrace();
        }
        for (File file : new File(url.getFile()).listFiles()){
            if (file.isDirectory()){
                scanClass(packagePath + "." + file.getName());
            }else if (file.getName().contains(".class")){
                classNames.add((packagePath + "." + file.getName().replaceAll(".class", "")));
            }
        }
    }

}
