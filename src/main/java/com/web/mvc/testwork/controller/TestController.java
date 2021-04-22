package com.web.mvc.testwork.controller;

import com.web.mvc.framework.annotation.RequestMapping;
import com.web.mvc.framework.annotation.Value;
import com.web.mvc.framework.annotation.bean.Autowired;
import com.web.mvc.framework.annotation.component.Controller;
import com.web.mvc.testwork.service.TestService;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @Value("scanPackage")
    String packageName;

    @RequestMapping("/te")
    public String te(){
        System.out.println(packageName);
        return "index";
    }

}
