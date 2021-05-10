package com.web.mvc.testwork.service.impl;

import com.web.mvc.framework.annotation.bean.Autowired;
import com.web.mvc.framework.annotation.component.Service;
import com.web.mvc.framework.annotation.sql.Transactional;
import com.web.mvc.testwork.dao.UserData;
import com.web.mvc.testwork.entity.User;
import com.web.mvc.testwork.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserData userData;

    @Override
    public List<User> getUser(String id,Integer age,Integer page,Integer limit){
        return userData.getUsers(id,age,(page-1) * limit,limit);
    }

    @Override
    public void addUser(User user){

        User old = userData.findUserById(user.getId());
        if (old != null) throw new RuntimeException("id已存在");

        userData.addUser(user.getId(),user.getName(),user.getAge());
    }

    @Override
    public void deleteUser(String id){
        userData.deleteUser(id);
    }

    @Override
    public void updateUser(User user){
        userData.updateUser(user.getName(),user.getAge(),user.getId());
    }

    @Override
    @Transactional
    public void test() {
        userData.addUser("id","test",1);
        userData.addUser("id","test",1);
    }
}
