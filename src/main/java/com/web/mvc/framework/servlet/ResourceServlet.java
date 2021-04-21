package com.web.mvc.framework.servlet;

import com.web.mvc.framework.constant.PropertiesConstant;
import com.web.mvc.framework.content.PropertiesContent;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

public class ResourceServlet extends HttpServlet {

    // 配置文件
    private PropertiesContent propertiesContent = PropertiesContent.getInstance();
    private String encoding = null;

    @Override
    public void init() {
        encoding = propertiesContent.getProp(PropertiesConstant.ENCODING);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String suffix = "";
        try {
            suffix = req.getRequestURL().substring(req.getRequestURL().
                    lastIndexOf("."));// 求出后缀，赋予相应格式的content
        }catch (Exception e){
            resp.getWriter().write("<h1>错误的请求</h1>");
            return;
        }
        switch (suffix){
            case ".html":
                if (req.getAttribute("hide") == null){
                    resp.getWriter().write("<h1>该资源无法直接访问!</h1>");
                    return;
                }
                resp.setContentType("text/html;charset="+encoding);
                break;
            case ".css":
                resp.setContentType("text/css;charset="+encoding);
                break;
            case ".js":
                resp.setContentType("text/javascript;charset="+encoding);
                break;
            case ".ico":
                resp.setContentType("image/x-icon;charset="+encoding);
                break;
            default: resp.setContentType("text/html;charset="+encoding);
        }
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contextPath = req.getContextPath();
        String url = req.getRequestURI().replace(contextPath, "");

        InputStream in = this.getClass().getClassLoader().getResourceAsStream(url.substring(1));
        if (in == null){
            resp.getWriter().write("<h1>404</h1>");
            return;
        }
        IOUtils.copy(in,resp.getOutputStream());
    }
}

