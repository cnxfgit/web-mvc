package com.web.mvc.servlet;

import com.web.mvc.annotation.RequestMapping;
import com.web.mvc.annotation.component.Controller;
import com.web.mvc.annotation.component.RestController;
import com.web.mvc.annotation.param.RequestBody;
import com.web.mvc.annotation.param.RequestParam;
import com.web.mvc.constant.PropertiesConstant;
import com.web.mvc.content.BeanContent;
import com.web.mvc.content.PropertiesContent;
import com.web.mvc.util.JsonUtil;
import com.web.mvc.util.ReflectUtil;
import com.web.mvc.util.StringUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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

@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatchServlet extends HttpServlet {

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

        if (!initHandleMapping()) throw new RuntimeException("==>url初始化失败!");

        if (!initViewMapping()) throw new RuntimeException("==>视图初始化失败!");
    }

    private boolean initViewMapping() {
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
        System.out.println("==>视图映射装配完成!");
        return true;
    }

    private boolean initHandleMapping() {
        for (Map.Entry entry : beanContent.getEntrySet()) {
            Class clazz = entry.getValue().getClass();
            // 如果不是RestController则终止
            if (!clazz.isAnnotationPresent(RestController.class)) continue;
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
        System.out.println("==>url映射装配完成!");
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
                req.getRequestDispatcher(view).forward(req, resp);// 转发致default Servlet
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
                }
            }
            String beanName = method.getDeclaringClass().getSimpleName();
            try {
                Object result = method.invoke(beanContent.getBean(beanName), list.toArray());// 执行controller对应的方法
                resp.getWriter().write(JsonUtil.toJson(result));
            } catch (Exception e) {
                e.printStackTrace();
                resp.getWriter().write(e.getMessage());
            }
        } else {
            resp.setStatus(404);
            resp.getWriter().write("<h1>404</h1>");// 无法匹配url路径，返回404页面
        }
    }
}
