package com.web.mvc.router;

import com.web.mvc.annotation.WebRequestMapping;
import com.web.mvc.annotation.component.WebController;

@WebController
public class IndexRouter {

    @WebRequestMapping("/index")
    public String index(){
        return "index";
    }

    @WebRequestMapping("/user")
    public String user(){
        return "user";
    }

}
