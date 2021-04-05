package com.web.mvc.controller;

import com.web.mvc.annotation.WebAutowired;
import com.web.mvc.annotation.WebRequestMapping;
import com.web.mvc.annotation.component.WebRestController;
import com.web.mvc.annotation.param.WebRequestBody;
import com.web.mvc.annotation.param.WebRequestParam;
import com.web.mvc.entity.User;
import com.web.mvc.service.UserService;
import com.web.mvc.util.$;

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
        String string = stringBuilder.substring(0,stringBuilder.length()-1);
        if (!list.isEmpty()) return "{\"code\": 0, \"msg\": \"OK\", \"data\": [" + string+"], \"count\": 4}";
        return "查找失败！";
    }

    @WebRequestMapping("/getByName")
    public User getUserByName(@WebRequestParam("name") String name){
        System.out.println(name);
        User user = userService.getUserByName(name);
        return user;
    }

    @WebRequestMapping("/add")
    public String addUser(@WebRequestBody User user){
        System.out.println(user.toString());

        return user.toString();
    }
}
