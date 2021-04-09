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
        DefaultProperties.init();
        new InitBean().init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }


}
