package com.web.mvc.testwork.router;

import com.web.mvc.framework.annotation.component.RequestMapping;
import com.web.mvc.framework.annotation.component.Router;

@Router
public class HomeRouter {

    @RequestMapping("/admin/login.html")
    public String login(){
        return "admin/home/login";
    }


}
