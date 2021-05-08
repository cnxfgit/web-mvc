package com.web.mvc.testwork.service;

import com.web.mvc.testwork.entity.User;

import java.util.List;

public interface UserService {

    List<User> getUser(String id, Integer age,Integer page,Integer limit);

    void addUser(User user);

    void deleteUser(String id);

    void updateUser(User user);

    void test();
}
