package com.web.mvc.testwork.service;

import com.web.mvc.framework.annotation.component.Service;
import com.web.mvc.testwork.init.UserContent;
import com.web.mvc.testwork.entity.User;

import java.util.List;

@Service
public class UserService {

    UserContent userContent = UserContent.getInstance();

    public List<User> getUser(String id,Integer age){
        return userContent.getAllUser(id,age);
    }

    public User getUserByName(String name){
        return userContent.getUser(name);
    }

    public boolean addUser(User user){
        return userContent.addUser(user);
    }

    public boolean deleteUser(String id){
        return userContent.deleteUser(id);
    }

    public boolean updateUser(User user){
        return userContent.putUser(user);
    }
}
