package com.web.mvc.testwork.controller;

import com.web.mvc.framework.annotation.RequestMapping;
import com.web.mvc.framework.annotation.Value;
import com.web.mvc.framework.annotation.bean.Autowired;
import com.web.mvc.framework.annotation.component.RestController;
import com.web.mvc.testwork.entity.User;
import com.web.mvc.testwork.service.TestService;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @Value("scanPackage")
    String packageName;

    @Value("encoding")
    String encoding;

    @Autowired
    private User user;

    @RequestMapping("/te")
    public String te(){
        System.out.println(packageName);
        System.out.println(user.getName());
        System.out.println(encoding);
        return packageName + "<br/>" + user.getName() + "<br/>" +encoding;
    }

}
