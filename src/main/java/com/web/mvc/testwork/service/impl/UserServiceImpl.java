package com.web.mvc.testwork.service.impl;

import com.web.mvc.framework.annotation.bean.Autowired;
import com.web.mvc.framework.annotation.component.Service;
import com.web.mvc.testwork.dao.UserData;
import com.web.mvc.testwork.entity.User;
import com.web.mvc.testwork.init.UserContent;
import com.web.mvc.testwork.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    UserContent userContent = UserContent.getInstance();

    @Autowired
    UserData userData;

    @Override
    public List<User> getUser(String id,Integer age){
        System.out.println(userData.update("测试", 1));

        Integer i = null;
        return userData.print(i);
    }

    @Override
    public User getUserByName(String name){
        return userContent.getUser(name);
    }

    @Override
    public boolean addUser(User user){
        return userContent.addUser(user);
    }

    @Override
    public boolean deleteUser(String id){
        return userContent.deleteUser(id);
    }

    @Override
    public boolean updateUser(User user){
        return userContent.putUser(user);
    }
}
