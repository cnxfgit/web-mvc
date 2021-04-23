package com.web.mvc.framework;

import com.web.mvc.framework.annotation.WebMvcRun;
import com.web.mvc.framework.filter.GlobalFilter;
import com.web.mvc.framework.filter.LoginFilter;
import com.web.mvc.framework.listener.ContextLoaderListener;
import com.web.mvc.framework.listener.SessionListener;
import com.web.mvc.framework.log.Log;
import com.web.mvc.framework.log.LogFactory;
import com.web.mvc.framework.servlet.DispatchServlet;
import com.web.mvc.framework.servlet.ResourceServlet;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

public class WebMvcApplication {

    private static Log logger = LogFactory.getSimpleLog(WebMvcApplication.class);

    public static void run(Class mainClass, Integer port, String... args){
        if (mainClass.isAnnotationPresent(WebMvcRun.class)){
            start(port);
        }else logger.err("启动失败!无法识别启动类!");
    }

    public static void start(Integer port){
        Tomcat tomcat = new Tomcat();

        tomcat.setPort(port);
//        Connector connector = new Connector();
//        connector.setURIEncoding("");
//        tomcat.setConnector(connector);

        StandardContext context = new StandardContext();
        context.setPath("/");
        context.addLifecycleListener(new Tomcat.FixContextListener());
        tomcat.getHost().addChild(context);

        addListener(context);// 配置listener
        addFilter(context);// 配置filter
        addServlet(context);// 配置Servlet

        try {
            tomcat.init();
            tomcat.getConnector();
            tomcat.start();
            logger.info("tomcat启动成功...");
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
            logger.err("tomcat启动失败...");
        }
    }

    public static void addServlet(StandardContext context){
        Tomcat.addServlet(context, "DispatchServlet", new DispatchServlet());
        context.addServletMappingDecoded("/*", "DispatchServlet");

        Tomcat.addServlet(context, "ResourceServlet", new ResourceServlet());
        context.addServletMappingDecoded("/static/*", "ResourceServlet");
        context.addServletMappingDecoded("/templates/*", "ResourceServlet");
    }

    public static void addListener(StandardContext context){
        context.addApplicationLifecycleListener(new ContextLoaderListener());
        context.addApplicationLifecycleListener(new SessionListener());
        // context.addApplicationEventListener(); 事件监听器
    }

    public static void addFilter(StandardContext context){

        FilterDef globalFilter = new FilterDef();
        globalFilter.setFilterName(GlobalFilter.class.getSimpleName());
        globalFilter.setFilterClass(GlobalFilter.class.getName());

        FilterMap globalFilterMapping = new FilterMap();
        globalFilterMapping.setFilterName(GlobalFilter.class.getSimpleName());
        globalFilterMapping.addURLPattern("/*");

        context.addFilterDef(globalFilter);
        context.addFilterMap(globalFilterMapping);

        FilterDef loginFilter = new FilterDef();
        loginFilter.setFilterName(LoginFilter.class.getSimpleName());
        loginFilter.setFilterClass(LoginFilter.class.getName());

        FilterMap loginFilterMapping = new FilterMap();
        loginFilterMapping.setFilterName(LoginFilter.class.getSimpleName());
        loginFilterMapping.addURLPattern("/admin/*");

        context.addFilterDef(loginFilter);
        context.addFilterMap(loginFilterMapping);
    }

}
