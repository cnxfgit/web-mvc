package com.web.mvc.testwork.router;

import com.web.mvc.framework.annotation.component.RequestMapping;
import com.web.mvc.framework.annotation.component.Router;

@Router
@RequestMapping("/admin")
public class UserRouter {

    @RequestMapping("/user")
    public String user(){
        return "admin/user/list";
    }

    @RequestMapping("/user/addPage")
    public String userAdd(){
        return "admin/user/add";
    }

    @RequestMapping("/user/editPage")
    public String userEdit(){
        return "admin/user/edit";
    }

}
