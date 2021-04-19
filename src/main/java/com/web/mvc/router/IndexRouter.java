package com.web.mvc.router;

import com.web.mvc.annotation.RequestMapping;
import com.web.mvc.annotation.component.Controller;

@Controller
public class IndexRouter {

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/user")
    public String user(){
        return "user/user";
    }

    @RequestMapping("/user/add")
    public String userAdd(){
        return "user/add";
    }

}
