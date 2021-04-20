package com.web.mvc.router;

import com.web.mvc.annotation.RequestMapping;
import com.web.mvc.annotation.component.Controller;

@Controller
public class IndexRouter {

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/")
    public String index2(){
        return "index";
    }

    @RequestMapping("/user")
    public String user(){
        return "user/list";
    }

    @RequestMapping("/user/addPage")
    public String userAdd(){
        return "user/add";
    }


    @RequestMapping("/user/editPage")
    public String userEdit(){
        return "user/edit";
    }

}
