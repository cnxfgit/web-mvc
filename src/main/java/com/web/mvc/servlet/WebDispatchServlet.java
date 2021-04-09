package com.web.mvc.servlet;

import com.web.mvc.annotation.WebRequestMapping;
import com.web.mvc.annotation.component.WebController;
import com.web.mvc.annotation.component.WebRestController;
import com.web.mvc.annotation.param.WebRequestBody;
import com.web.mvc.annotation.param.WebRequestParam;
import com.web.mvc.constant.PropertiesConstant;
import com.web.mvc.content.BeanContent;
import com.web.mvc.content.PropertiesContent;
import com.web.mvc.util.$;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet(urlPatterns = "/*",loadOnStartup = 0)
public class WebDispatchServlet extends HttpServlet {

    // 配置文件
    private PropertiesContent propertiesContent = PropertiesContent.getInstance();
    // bean容器
    private BeanContent beanContent = BeanContent.getInstance();
    // handleMapping url映射
    private Map<String, Method> handleMapping = new ConcurrentHashMap<>();
    // viewMapping 视图映射
    private Map<String, String> viewMapping = new HashMap<>();
    // 视图的前缀后缀
    private String[] viewPrefixSuffix = new String[2];

    @Override
    public void init(ServletConfig config) {

        viewPrefixSuffix[0] = propertiesContent.getProp(PropertiesConstant.VIEW_PREFIX);// 视图前缀
        viewPrefixSuffix[1] = propertiesContent.getProp(PropertiesConstant.VIEW_SUFFIX);// 视图后缀

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
            Method[] methods = clazz.getDeclaredMethods();
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
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e){
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
            Method[] methods = clazz.getDeclaredMethods();
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
                                String i = req.getParameter(fieldName);
                                if (!$.isEmpty(i)) field.set(object,Integer.parseInt(i));
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
                        String i = req.getParameter(webRequestParam.value());
                        Integer param = null;
                        if (!$.isEmpty(i)){
                            param = Integer.parseInt(i);
                        }
                        list.add(param);
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
