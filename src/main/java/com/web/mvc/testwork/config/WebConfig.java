package com.web.mvc.testwork.config;


import com.web.mvc.framework.annotation.component.Component;
import com.web.mvc.framework.content.WebContent;

import java.util.Map;
import java.util.Set;

@Component
public class WebConfig {

    private WebContent webContent = WebContent.getInstance();

    {
        addExcludePath();
        addWebPage();
    }

    // 通用页面设置
    public void addWebPage(){
        Map<String,String> map = webContent.getWebPageMap();
        map.put("loginPage","/admin/login.html");

        map.put("404page","/error/404page");
        map.put("500page","/error/500page");
    }

    // 放行的路径
    public void addExcludePath(){
       Set<String> set = webContent.getExcludePathSet();
       set.add("/admin/captcha");
       set.add("/admin/login.html");
       set.add("/admin/login");
    }

}
