package com.web.mvc.testwork.controller;

import com.web.mvc.framework.annotation.RequestMapping;
import com.web.mvc.framework.annotation.component.Controller;

@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/sql")
    public String sql() {
        return "ok";
    }
}
