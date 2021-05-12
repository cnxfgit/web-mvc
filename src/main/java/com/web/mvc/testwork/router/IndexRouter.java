package com.web.mvc.testwork.router;

import com.web.mvc.framework.annotation.RequestMapping;
import com.web.mvc.framework.annotation.component.Router;

@Router
public class IndexRouter {

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/")
    public String index2(){
        return "index";
    }

}
