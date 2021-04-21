package com.web.mvc.testwork.controller;

import com.web.mvc.framework.annotation.bean.Autowired;
import com.web.mvc.framework.annotation.component.Service;
import com.web.mvc.testwork.service.TestService;

@Service
public class TestController {

    @Autowired
    private TestService testService;

}
