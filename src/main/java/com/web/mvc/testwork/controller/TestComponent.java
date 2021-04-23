package com.web.mvc.testwork.controller;

import com.web.mvc.framework.annotation.bean.Bean;
import com.web.mvc.framework.annotation.component.Component;
import com.web.mvc.testwork.entity.User;

@Component
public class TestComponent {

    @Bean
    public User setUser(){
        User user = new User();
        user.setName("我是被注入的user");
        return user;
    }

}
