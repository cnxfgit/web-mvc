package com.web.mvc.framework.listener;


import com.web.mvc.framework.init.DefaultProperties;
import com.web.mvc.framework.init.InitBean;
import com.web.mvc.framework.init.InitData;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextLoaderListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DefaultProperties.init(); // 配置文件初始化
        new InitBean().init();// bean初始化及装配
        new InitData().init();// dao层代理初始化
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }


}
