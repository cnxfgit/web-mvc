package com.web.mvc.controller;

import com.web.mvc.annotation.WebAutowired;
import com.web.mvc.annotation.WebRequestMapping;
import com.web.mvc.annotation.component.WebRestController;
import com.web.mvc.annotation.param.WebRequestBody;
import com.web.mvc.annotation.param.WebRequestParam;
import com.web.mvc.common.TableResult;
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
    public String getUser(@WebRequestParam("page") Integer page,
                          @WebRequestParam("limit") Integer limit,
                          @WebRequestBody User user){
        List<User> list = userService.getUser();
        System.out.println(user);
        System.out.println(limit);
        System.out.println(page);
        return $.toJson(TableResult.ok(list,list.size()));
    }

    @WebRequestMapping("/getByName")
    public String getUserByName(@WebRequestParam("name") String name){
        System.out.println(name);
        User user = userService.getUserByName(name);
        return $.toJson(user);
    }

    @WebRequestMapping("/add")
    public String addUser(@WebRequestBody User user){
        System.out.println(user.toString());
        return user.toString();
    }
}
