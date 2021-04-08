package com.web.mvc.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(initParams = @WebInitParam(name = "encoding",value = "UTF-8"), urlPatterns = "/*")
public class GlobalFilter implements Filter {

    private String encoding = null;

    @Override
    public void init(FilterConfig filterConfig) {
        encoding = filterConfig.getInitParameter("encoding");// 获取xml文件配置utf-8
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setContentType("text/html;charset="+encoding);// 设置全局编码
        HttpServletRequest req = (HttpServletRequest)request;
        String xRequestedWith = req.getHeader("X-Requested-With");
        if (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1) { // 判断是否异步请求
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
