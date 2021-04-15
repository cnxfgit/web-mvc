package com.web.mvc.listener;


import com.web.mvc.init.DefaultProperties;
import com.web.mvc.init.InitBean;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextLoaderListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DefaultProperties.init(); // 配置文件初始化
        new InitBean().init();// bean初始化及装配
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }


}
