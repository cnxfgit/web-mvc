package com.web.mvc.servlet;

import com.web.mvc.annotation.WebAutowired;
import com.web.mvc.annotation.WebRequestMapping;
import com.web.mvc.annotation.component.WebComponent;
import com.web.mvc.annotation.component.WebController;
import com.web.mvc.annotation.component.WebRestController;
import com.web.mvc.annotation.component.WebService;
import com.web.mvc.annotation.param.WebRequestBody;
import com.web.mvc.annotation.param.WebRequestParam;
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
import java.lang.reflect.Parameter;
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
    // 视图的前缀后缀
    private String[] viewPrefixSuffix = new String[2];

    @Override
    public void init(ServletConfig config) {
        if (!initProperties(config)) throw new RuntimeException("==>配置文件初始化失败!");

        String packagePath = properties.getProperty("scanPackage");//获取包路径
        viewPrefixSuffix[0] = properties.getProperty("viewPrefix");// 视图前缀
        viewPrefixSuffix[1] = properties.getProperty("viewSuffix");// 视图后缀

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
                    String viewUrl = viewPrefixSuffix[0] + (String)method.invoke(beanContent.getBean(beanName)) + viewPrefixSuffix[1];
                    viewMapping.put(stringBuilder.toString(), viewUrl);
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
        try {// 有些系统需要指定字符集，否则会乱码失效
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
                req.getRequestDispatcher(view).forward(req,resp);// 转发致default Servlet
            }catch (ServletException e){
                e.printStackTrace();
            }
        }else if (handleMapping.containsKey(url)){
            Method method = handleMapping.get(url);
            Parameter[] parameters = method.getParameters();
            List list = new ArrayList();
            for (Parameter parameter:parameters) {
                if (parameter.isAnnotationPresent(WebRequestBody.class)){
                    Class clazz = parameter.getType();
                    try {
                        Object object = clazz.newInstance();
                        Field[] fields = object.getClass().getDeclaredFields();
                        for (Field field:fields) {
                            field.setAccessible(true);
                            Class<?> fieldType = field.getType();
                            String fieldName = field.getName();
                            if (fieldType == String.class){
                                field.set(object,req.getParameter(fieldName));
                            }
                            if (fieldType == Integer.class || fieldType == int.class){
                                field.set(object,Integer.parseInt(req.getParameter(fieldName)));
                            }
                        }
                        list.add(object);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else if (parameter.isAnnotationPresent(WebRequestParam.class)){
                    WebRequestParam webRequestParam = parameter.getAnnotation(WebRequestParam.class);
                    if (parameter.getType() == String.class){
                        String param = req.getParameter(webRequestParam.value());
                        list.add(param);
                    }

                    if (parameter.getType() == Integer.class || parameter.getType() == int.class){
                        Integer param = Integer.parseInt(req.getParameter(webRequestParam.value()));
                    }

                }
            }
            String beanName = method.getDeclaringClass().getSimpleName();
            try {
                Object result = method.invoke(beanContent.getBean(beanName),list.toArray());// 执行controller对应的方法
                resp.getWriter().write(result.toString());
            } catch (Exception e) {
                e.printStackTrace();
                resp.getWriter().write(e.getMessage());
            }
        }else {
            resp.setStatus(404);
            resp.getWriter().write("<h1>404</h1>");// 无法匹配url路径，返回404页面
        }
    }
}
