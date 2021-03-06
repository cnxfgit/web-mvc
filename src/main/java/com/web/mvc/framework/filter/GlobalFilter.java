package com.web.mvc.framework.filter;

import com.web.mvc.framework.constant.PropertiesConstant;
import com.web.mvc.framework.content.PropertiesContent;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class GlobalFilter implements Filter {

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
