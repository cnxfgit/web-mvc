package com.web.mvc.framework.servlet;

import com.web.mvc.framework.annotation.component.RequestMapping;
import com.web.mvc.framework.annotation.aop.After;
import com.web.mvc.framework.annotation.aop.Before;
import com.web.mvc.framework.annotation.component.Controller;
import com.web.mvc.framework.annotation.component.Router;
import com.web.mvc.framework.annotation.param.RequestBody;
import com.web.mvc.framework.annotation.param.RequestParam;
import com.web.mvc.framework.common.Result;
import com.web.mvc.framework.constant.PropertiesConstant;
import com.web.mvc.framework.content.BeanContent;
import com.web.mvc.framework.content.PropertiesContent;
import com.web.mvc.framework.log.Log;
import com.web.mvc.framework.log.LogFactory;
import com.web.mvc.framework.util.JsonUtil;
import com.web.mvc.framework.util.ReflectUtil;
import com.web.mvc.framework.util.StringUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DispatchServlet extends HttpServlet {

    private Log logger = LogFactory.getSimpleLog(DispatchServlet.class);
    // 配置文件
    private PropertiesContent propertiesContent = PropertiesContent.getInstance();
    // bean容器
    private BeanContent beanContent = BeanContent.getInstance();
    // handleMapping url映射
    private Map<String, Method> handleMapping = new ConcurrentHashMap<>();
    // viewMapping 视图映射
    private Map<String, String> viewMapping = new ConcurrentHashMap<>();
    // 视图的前缀后缀
    private String[] viewPrefixSuffix = new String[2];

    @Override
    public void init(ServletConfig config) {

        viewPrefixSuffix[0] = propertiesContent.getProp(PropertiesConstant.VIEW_PREFIX);// 视图前缀
        viewPrefixSuffix[1] = propertiesContent.getProp(PropertiesConstant.VIEW_SUFFIX);// 视图后缀

        if (!initHandleMapping()) logger.err("url初始化失败!");

        if (!initViewMapping()) logger.err("视图初始化失败!");
    }

    private boolean initViewMapping() {
        for (Map.Entry entry : beanContent.getEntrySet()) {
            Class clazz = entry.getValue().getClass();
            // 如果不是Router则终止
            if (!clazz.isAnnotationPresent(Router.class)) continue;
            StringBuilder baseUrl = new StringBuilder();
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
                baseUrl.append(requestMapping.value());
            }
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                // 如果方法不带RequestMapping注解则终止
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    StringBuilder stringBuilder = new StringBuilder(baseUrl);
                    stringBuilder.append(requestMapping.value());
                    String beanName = method.getDeclaringClass().getSimpleName();
                    String viewUrl = viewPrefixSuffix[0] + ReflectUtil.invoke(method,beanName) + viewPrefixSuffix[1];
                    viewMapping.put(stringBuilder.toString(), viewUrl);
                }
            }
        }
        logger.info("视图映射装配完成!");
        return true;
    }

    private boolean initHandleMapping() {
        for (Map.Entry entry : beanContent.getEntrySet()) {
            Class clazz = entry.getValue().getClass();
            // 如果不是Controller则终止
            if (!clazz.isAnnotationPresent(Controller.class)) continue;
            StringBuilder baseUrl = new StringBuilder();
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
                baseUrl.append(requestMapping.value());
            }
            Method[] methods = clazz.getDeclaredMethods();
            ReflectUtil.methods(methods).stream().forEach(method->{
                // 如果方法不带WebRequestMapping注解则终止
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    StringBuilder stringBuilder = new StringBuilder(baseUrl);
                    stringBuilder.append(requestMapping.value());
                    handleMapping.put(stringBuilder.toString(), method);
                }
            });

        }
        logger.info("url映射装配完成!");
        return true;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doDispatch(req, resp);
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String contextPath = req.getContextPath();
        String url = req.getRequestURI().replace(contextPath, "");

        if (viewMapping.containsKey(url)) {
            String view = viewMapping.get(url);
            try {
                req.setAttribute("hide","true");// 有标记的资源才能访问
                req.getRequestDispatcher(view).forward(req, resp);// 转发致resource Servlet
            } catch (ServletException e) {
                e.printStackTrace();
            }
        } else if (handleMapping.containsKey(url)) {
            Method method = handleMapping.get(url);
            Parameter[] parameters = method.getParameters();
            List list = new ArrayList();
            for (Parameter parameter : parameters) {
                if (parameter.isAnnotationPresent(RequestBody.class)) {
                    Class clazz = parameter.getType();

                    Object object = ReflectUtil.newInstance(clazz);
                    Field[] fields = object.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        Class<?> fieldType = field.getType();
                        String fieldName = field.getName();
                        if (fieldType == String.class) {
                            ReflectUtil.setField(field, object, req.getParameter(fieldName));
                        }
                        if (fieldType == Integer.class || fieldType == int.class) {
                            String i = req.getParameter(fieldName);
                            if (!StringUtil.isEmpty(i)) ReflectUtil.setField(field, object, Integer.parseInt(i));
                        }
                    }
                    list.add(object);
                } else if (parameter.isAnnotationPresent(RequestParam.class)) {
                    RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                    if (parameter.getType() == String.class) {
                        String param = req.getParameter(requestParam.value());
                        list.add(param);
                    }
                    if (parameter.getType() == Integer.class || parameter.getType() == int.class) {
                        String i = req.getParameter(requestParam.value());
                        Integer param = null;
                        if (!StringUtil.isEmpty(i)) {
                            param = Integer.parseInt(i);
                        }
                        list.add(param);
                    }
                }else if (parameter.getType() == HttpServletRequest.class){
                    list.add(req);
                }else if (parameter.getType() == HttpServletResponse.class){
                    list.add(resp);
                }
            }
            String beanName = method.getDeclaringClass().getSimpleName();
            try {
                // before
                if (method.isAnnotationPresent(Before.class)){
                    Before before = method.getAnnotation(Before.class);
                    Object instance = beanContent.getBean(before.bean().getSimpleName());
                    Method bMethod = before.bean().getMethod("before",
                            HttpServletRequest.class,HttpServletResponse.class,String.class);
                    bMethod.invoke(instance,req,resp,before.value());
                }
                Object result = method.invoke(beanContent.getBean(beanName), list.toArray());// 执行controller对应的方法
                // after
                if (method.isAnnotationPresent(After.class)){
                    After after = method.getAnnotation(After.class);
                    Object instance = beanContent.getBean(after.bean().getSimpleName());
                    Method bMethod = after.bean().getMethod("after",
                            HttpServletRequest.class,HttpServletResponse.class,String.class);
                    bMethod.invoke(instance,req,resp,after.value());
                }
                if (result instanceof String) {
                    resp.getWriter().write(result.toString());
                    return;
                }
                if (result!=null){
                    resp.getWriter().write(JsonUtil.toJson(result));
                }

            } catch (Throwable e) {
                e.printStackTrace();
                while (e.getCause() != null) {// 获取最底层的异常
                    Throwable cause = e.getCause();
                    if (e.equals(cause)) {
                        System.out.println(e);
                    }
                    e = cause;
                }
                resp.getWriter().write(JsonUtil.toJson(Result.fail(e.toString())));
            }
        } else {
            resp.setStatus(404);
            resp.getWriter().write("<h1>404</h1>");// 无法匹配url路径，返回404页面
        }
    }
}
