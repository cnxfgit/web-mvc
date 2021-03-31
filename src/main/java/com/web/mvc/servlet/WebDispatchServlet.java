package com.web.mvc.servlet;

import com.web.mvc.annotation.WebAutowired;
import com.web.mvc.annotation.component.WebComponent;
import com.web.mvc.annotation.component.WebController;
import com.web.mvc.annotation.WebRequestMapping;
import com.web.mvc.annotation.component.WebRestController;
import com.web.mvc.annotation.component.WebService;
import com.web.mvc.content.BeanContent;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

public class WebDispatchServlet extends HttpServlet {

    // 配置文件
    private Properties properties = new Properties();
    // 扫描指定包下的类名
    private List<String> classNames = new ArrayList<>();
    // bean容器
    private BeanContent beanContent = BeanContent.getInstance();
    // handleMapping url映射
    private Map<String, Method> handleMapping = new HashMap<>();
    // viewMapping 视图映射
    private Map<String, String> viewMapping = new HashMap<>();

    @Override
    public void init(ServletConfig config) {
        if (!initProperties(config)) throw new RuntimeException("==>配置文件初始化失败!");

        String packagePath = properties.getProperty("scanPackage");
        if (!scanClass(packagePath)) throw new RuntimeException("==>扫描包失败!");
        System.out.println("==>扫描包成功!");

        if (!initInstance()) throw new RuntimeException("==>初始化bean失败!");

        if (!dependencyInjection()) throw new RuntimeException("==>bean装配失败!");

        if (!initHandleMapping()) throw new RuntimeException("==>url初始化失败!");

        if (!initViewMapping()) throw new RuntimeException("==>视图初始化失败!");
    }

    private boolean initViewMapping() {
        for (Map.Entry entry : beanContent.getEntrySet()){
            Class clazz = entry.getValue().getClass();
            // 如果不是WebController则终止
            if (!clazz.isAnnotationPresent(WebController.class)) continue;
            StringBuilder baseUrl = new StringBuilder();
            if (clazz.isAnnotationPresent(WebRequestMapping.class)){
                WebRequestMapping webRequestMapping = (WebRequestMapping) clazz.getAnnotation(WebRequestMapping.class);
                baseUrl.append(webRequestMapping.value());
            }
            Method [] methods = clazz.getDeclaredMethods();
            for (Method method : methods){
                // 如果方法不带WebRequestMapping注解则终止
                if (!method.isAnnotationPresent(WebRequestMapping.class)) continue;
                WebRequestMapping webRequestMapping = method.getAnnotation(WebRequestMapping.class);
                StringBuilder stringBuilder = new StringBuilder(baseUrl);
                stringBuilder.append(webRequestMapping.value());
                String beanName = method.getDeclaringClass().getSimpleName();
                try {
                    viewMapping.put(stringBuilder.toString(), (String)method.invoke(beanContent.getBean(beanName)));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("==>视图映射装配完成!");
        return true;
    }

    private boolean initHandleMapping() {
        for (Map.Entry entry : beanContent.getEntrySet()){
            Class clazz = entry.getValue().getClass();
            // 如果不是WebRestController则终止
            if (!clazz.isAnnotationPresent(WebRestController.class)) continue;
            StringBuilder baseUrl = new StringBuilder();
            if (clazz.isAnnotationPresent(WebRequestMapping.class)){
                WebRequestMapping webRequestMapping = (WebRequestMapping) clazz.getAnnotation(WebRequestMapping.class);
                baseUrl.append(webRequestMapping.value());
            }
            Method [] methods = clazz.getDeclaredMethods();
            for (Method method : methods){
                // 如果方法不带WebRequestMapping注解则终止
                if (!method.isAnnotationPresent(WebRequestMapping.class)) continue;
                WebRequestMapping webRequestMapping = method.getAnnotation(WebRequestMapping.class);
                StringBuilder stringBuilder = new StringBuilder(baseUrl);
                stringBuilder.append(webRequestMapping.value());
                handleMapping.put(stringBuilder.toString(), method);
            }
        }
        System.out.println("==>url映射装配完成!");
        return true;
    }

    private boolean dependencyInjection() {
        for (Map.Entry<String, Object> entry: beanContent.getEntrySet()){
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields){
                // 判断属性有没有需要被注入的
                if (!field.isAnnotationPresent(WebAutowired.class)) continue;
                field.setAccessible(true); // 强制授权
                try {
                    field.set(entry.getValue(), beanContent.getBean(field.getType().getSimpleName()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("==>bean装配成功!");
        return true;
    }

    private boolean initInstance() {
        if (classNames.isEmpty()) return true;
        try{
            for (String className: classNames) {
                Class clazz = Class.forName(className);
                Object instance;
                // 类型做key，实例作为value
                if (clazz.isAnnotationPresent(WebController.class)) {
                    instance = clazz.newInstance();
                    beanContent.setBean(clazz.getSimpleName(),instance);
                }else if(clazz.isAnnotationPresent(WebService.class)) {
                    instance = clazz.newInstance();
                    beanContent.setBean(clazz.getSimpleName(),instance);
                }else if (clazz.isAnnotationPresent(WebComponent.class)){
                    instance = clazz.newInstance();
                    beanContent.setBean(clazz.getSimpleName(),instance);
                }else if (clazz.isAnnotationPresent(WebRestController.class)){
                    instance = clazz.newInstance();
                    beanContent.setBean(clazz.getSimpleName(),instance);
                }
            }
            System.out.println("==>初始化bean成功!");
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean scanClass(String packagePath) {
        String path = this.getClass().getClassLoader().getResource("/" + packagePath.replaceAll("\\.", "/")).getPath();
        URL url = null;
        try {// 有些系统需要指定字符集，否则会失效
            path = "file:"+URLDecoder.decode(path,"utf-8");
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
        return true;
    }


    private boolean initProperties(ServletConfig config) {
        String configLocation = config.getInitParameter("configLocation").replace("classpath:", "");
        //从项目下取得配置文件的输入流
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(configLocation);
        try {
            properties.load(ins);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("==>配置文件获取失败!");
        }finally {
            if (ins != null)
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        System.out.println("==>配置文件获取成功!");
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");// 设置编码
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doDispatch(req, resp);
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String contextPath = req.getContextPath();
        String url = req.getRequestURI().replace(contextPath, "");
        if (viewMapping.containsKey(url)){
            String view = viewMapping.get(url);
            try {
                req.getRequestDispatcher("/WEB-INF/templates/index.html").forward(req,resp);
            }catch (ServletException e){
                e.printStackTrace();
            }
        }else if (handleMapping.containsKey(url)){
            Method method = handleMapping.get(url);
            String beanName = method.getDeclaringClass().getSimpleName();
            try {
                resp.getWriter().write((String) method.invoke(beanContent.getBean(beanName)));// 执行controller对应的方法
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }else {
            System.out.println(url);
            resp.getWriter().write("<h1>404</h1>");// 无法匹配url路径，返回404页面
        }
    }
}
