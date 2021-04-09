package com.web.mvc.controller;

import com.web.mvc.annotation.bean.Autowired;
import com.web.mvc.annotation.component.Service;
import com.web.mvc.service.TestService;

@Service
public class TestController {

    @Autowired
    private TestService testService;

}
