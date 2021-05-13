package com.web.mvc.framework.filter;

import com.web.mvc.framework.content.WebContent;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class LoginFilter implements Filter {

    // web容器
    private WebContent webContent = WebContent.getInstance();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();
        session.setMaxInactiveInterval(60 * 30);// 设置30分钟

        String contextPath = req.getContextPath();
        String url = req.getRequestURI().replace(contextPath, "");
        Set<String> set = webContent.getExcludePathSet();
        if (set.contains(url)) {
            // 放行的url
            chain.doFilter(request,response);
            return;
        }

        // 对于未登录的用户重定向到登录页
        if (session.getAttribute("user") != null) chain.doFilter(request,response);
        else {
            Map<String,String> map = webContent.getWebPageMap();
            String loginPage = map.get("loginPage");
            if (loginPage == null) resp.sendRedirect("/");// 如果配置了登录页则重定向登录页，没有则回到/
            else resp.sendRedirect(loginPage);
        }
    }

}
