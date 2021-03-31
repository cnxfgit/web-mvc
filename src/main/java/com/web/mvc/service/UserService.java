package com.web.mvc.service;

import com.web.mvc.annotation.WebService;
import com.web.mvc.content.UserContent;
import com.web.mvc.entity.User;

import java.util.List;

@WebService
public class UserService {

    UserContent userContent = UserContent.getInstance();

    public List<User> getUser(){
        return userContent.getAllUser();
    }
}
