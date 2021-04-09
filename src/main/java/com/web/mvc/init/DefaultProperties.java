package com.web.mvc.init;

import com.web.mvc.constant.PropertiesConstant;
import com.web.mvc.content.PropertiesContent;
import com.web.mvc.util.$;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;

public class DefaultProperties {

    private static Logger logger = Logger.getLogger(DefaultProperties.class.getName());
    // 配置文件
    static private Properties properties = new Properties();
    // properties容器
    static private PropertiesContent content = PropertiesContent.getInstance();

    public static final String APPLICATION_PROPERTIES = "application.properties";

    public static final String VIEW_PREFIX = "/WEB-INF/templates/";
    public static final String VIEW_SUFFIX = ".html";
    public static final String ENCODING = "UTF-8";
    public static final String SCAN_PACKAGE = "";

    public static void init(){
        // 获得配置文件的输入流
        InputStream ins = DefaultProperties.class.getClassLoader().getResourceAsStream(APPLICATION_PROPERTIES);
        try {
            properties.load(ins);
            logger.info("==>配置文件加载成功!");
        } catch (IOException e) {
            logger.warning("==>配置文件加载失败!");
            e.printStackTrace();
        }finally {
            if (ins != null)
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        try {
            loadProperties();
            logger.info("==>配置文件读取成功！");
        }catch (Exception e){
            logger.warning("==>配置文件读取失败！");
            e.printStackTrace();
        }


    }

    static private void loadProperties(){

        content.setProp(PropertiesConstant.VIEW_PREFIX,VIEW_PREFIX);
        content.setProp(PropertiesConstant.VIEW_SUFFIX,VIEW_SUFFIX);
        content.setProp(PropertiesConstant.SCAN_PACKAGE,SCAN_PACKAGE);
        content.setProp(PropertiesConstant.ENCODING,ENCODING);

        // 配置文件的配置将会覆盖默认配置
        Enumeration enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()){
            String name = (String)enumeration.nextElement();
            String value = properties.getProperty(name);
            content.setProp(name,$.isEmpty(value)?"":value);
        }
    }

}
