package com.web.mvc.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class StaticFilter implements Filter {

    private String encoding = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        encoding = filterConfig.getInitParameter("encoding");// 获取xml文件配置utf-8
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        String suffix = req.getRequestURL().substring(req.getRequestURL().lastIndexOf("."));
        System.out.println(suffix);
        if (".css".equals(suffix)){
            response.setContentType("text/css;charset="+encoding);
        }else if (".js".equals(suffix)){
            response.setContentType("text/javascript;charset="+encoding);
        }
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
