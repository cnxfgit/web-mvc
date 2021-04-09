package com.web.mvc.filter;

import com.web.mvc.constant.PropertiesConstant;
import com.web.mvc.content.PropertiesContent;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(initParams = @WebInitParam(name = "encoding",value = "UTF-8"),urlPatterns = {"*.css","*.js"})
public class StaticFilter implements Filter {

    // 配置文件
    private PropertiesContent propertiesContent = PropertiesContent.getInstance();
    private String encoding = null;

    @Override
    public void init(FilterConfig filterConfig) {
        encoding = propertiesContent.getProp(PropertiesConstant.ENCODING);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        String suffix = req.getRequestURL().substring(req.getRequestURL().lastIndexOf("."));// 求出后缀，赋予相应格式的content
        switch (suffix){
            case ".css":
                response.setContentType("text/css;charset="+encoding);
                break;
            case ".js":
                response.setContentType("text/javascript;charset="+encoding);
                break;
            default: response.setContentType("text/html;charset="+encoding);
        }

        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
