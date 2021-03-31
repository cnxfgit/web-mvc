package com.web.mvc.controller;

import com.web.mvc.annotation.WebAutowired;
import com.web.mvc.annotation.component.WebService;
import com.web.mvc.service.TestService;

@WebService
public class TestController {

    @WebAutowired
    private TestService testService;

}
