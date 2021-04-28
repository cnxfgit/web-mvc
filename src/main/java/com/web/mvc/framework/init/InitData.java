package com.web.mvc.framework.init;

import com.web.mvc.framework.annotation.bean.Autowired;
import com.web.mvc.framework.annotation.sql.Data;
import com.web.mvc.framework.content.BeanContent;
import com.web.mvc.framework.content.ClassNamesContent;
import com.web.mvc.framework.log.Log;
import com.web.mvc.framework.log.LogFactory;
import com.web.mvc.framework.sql.ProxyFactory;
import com.web.mvc.framework.sql.datasource.DataSource;

import java.lang.reflect.Field;
import java.util.Map;

public class InitData {

    private Log logger = LogFactory.getSimpleLog(DefaultProperties.class);
    // 扫描指定包下的类名
    private ClassNamesContent classNames = ClassNamesContent.getInstance();
    // bean容器
    private BeanContent beanContent = BeanContent.getInstance();

    public void init() {
        DataSource dataSource = (DataSource) beanContent.getBean(DataSource.class.getSimpleName());
        if (dataSource == null) return;
        setDataProxy(dataSource);
        dependencyDataProxy();
    }

    private void dependencyDataProxy() {
        for (Map.Entry<String, Object> entry : beanContent.getEntrySet()) {
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                // 判断属性有没有需要被注入的
                if (!field.isAnnotationPresent(Autowired.class)) continue;
                if (!field.getType().isInterface()) continue;
                field.setAccessible(true); // 强制授权
                try {
                    field.set(entry.getValue(), beanContent.getBean(field.getType().getSimpleName()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    logger.err("Data注入失败!");
                }
            }
        }
        logger.info("Data注入成功!");
    }

    private void setDataProxy(DataSource dataSource) {
        try {
            for (String className:classNames.getList()) {
                Class clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(Data.class) && clazz.isInterface()){
                    // 生成代理类并注入到容器
                    ProxyFactory factory = new ProxyFactory<>(clazz);
                    Object obj = factory.newInstance(dataSource);
                    beanContent.setBean(clazz.getSimpleName(),obj);
                }
            }
            logger.info("Data代理生成成功!");
        }catch (Exception e){
            logger.err("Data代理生成失败!");
            e.printStackTrace();
        }
    }

}
