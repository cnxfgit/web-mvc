package com.web.mvc.service;

import com.web.mvc.annotation.component.Service;
import com.web.mvc.content.UserContent;
import com.web.mvc.entity.User;

import java.util.List;

@Service
public class UserService {

    UserContent userContent = UserContent.getInstance();

    public List<User> getUser(){
        return userContent.getAllUser();
    }

    public User getUserByName(String name){
        return userContent.getUser(name);
    }
}
