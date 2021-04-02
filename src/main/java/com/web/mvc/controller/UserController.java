package com.web.mvc.controller;

import com.web.mvc.annotation.WebAutowired;
import com.web.mvc.annotation.WebRequestMapping;
import com.web.mvc.annotation.component.WebRestController;
import com.web.mvc.annotation.param.WebRequestBody;
import com.web.mvc.annotation.param.WebRequestParam;
import com.web.mvc.entity.User;
import com.web.mvc.service.UserService;

import java.util.List;

@WebRestController
@WebRequestMapping("/user")
public class UserController {

    @WebAutowired
    private UserService userService;

    @WebRequestMapping("/get")
    public String getUser(){
        List<User> list = userService.getUser();
        StringBuilder stringBuilder = new StringBuilder();
        for (User user:list) {
            stringBuilder.append(user.toString()).append(",");
        }
        if (!list.isEmpty()) return "查找成功！" + stringBuilder;
        return "查找失败！";
    }

    @WebRequestMapping("/getByName")
    public User getUserByName(@WebRequestParam("name") String name){
        System.out.println(name);
        User user = userService.getUserByName(name);
        System.out.println(user);
        return user;
    }

    @WebRequestMapping("/add")
    public String addUser(@WebRequestBody User user){
        System.out.println(user.toString());

        return "OK";
    }
}
