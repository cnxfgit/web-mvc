package com.web.mvc.testwork.controller;

import com.web.mvc.framework.annotation.component.RequestMapping;
import com.web.mvc.framework.annotation.bean.Autowired;
import com.web.mvc.framework.annotation.component.Controller;
import com.web.mvc.testwork.service.UserService;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserService userService;

    @RequestMapping("/sql")
    public String sql() {
        userService.test();
        return "ok";
    }
}
