package com.web.mvc.filter;

import com.web.mvc.constant.PropertiesConstant;
import com.web.mvc.content.PropertiesContent;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 通过注解配置的过滤器执行顺序由类名按照字典顺序执行
 * 所以所有过滤器按照A-Z的首字母命名用来控制过滤器的执行顺序
 */
@WebFilter(urlPatterns = "/*")
public class AGlobalFilter implements Filter {

    // 配置文件
    private PropertiesContent propertiesContent = PropertiesContent.getInstance();
    private String encoding = null;

    @Override
    public void init(FilterConfig filterConfig) {
        encoding = propertiesContent.getProp(PropertiesConstant.ENCODING);// 获取xml文件配置utf-8
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setContentType("text/html;charset="+encoding);// 设置全局编码
        HttpServletRequest req = (HttpServletRequest)request;
        String xRequestedWith = req.getHeader("X-Requested-With");
        if (xRequestedWith != null && xRequestedWith.contains("XMLHttpRequest")) { // 判断是否异步请求
            response.setContentType("application/json");
        }
        request.setCharacterEncoding(encoding);
        response.setCharacterEncoding(encoding);
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
