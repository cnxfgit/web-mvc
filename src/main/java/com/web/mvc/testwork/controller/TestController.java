package com.web.mvc.testwork.controller;

import com.web.mvc.framework.annotation.RequestMapping;
import com.web.mvc.framework.annotation.bean.Autowired;
import com.web.mvc.framework.annotation.component.Controller;
import com.web.mvc.testwork.dao.UserData;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserData userData;

    @RequestMapping("/sql")
    public String sql() {
        userData.update("sb","lisi");
        return "ok";
    }
}
