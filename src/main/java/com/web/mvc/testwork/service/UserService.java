package com.web.mvc.testwork.service;

import com.web.mvc.testwork.entity.User;

import java.util.List;

public interface UserService {

    List<User> getUser(String id, Integer age,Integer page,Integer limit);

    User getUserByName(String name);

    boolean addUser(User user);

    boolean deleteUser(String id);

    boolean updateUser(User user);

}
