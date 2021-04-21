package com.web.mvc.testwork.init;

import com.web.mvc.testwork.entity.User;
import com.web.mvc.framework.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserContent {

    private Map<String, User> userMap;

    private static UserContent instance = new UserContent();

    private UserContent(){
        userMap = new HashMap<>();
        InitUsers initUsers = new InitUsers();
        List<User> list = initUsers.getList();
        for (int i = 0; i < list.size(); i++) {
            userMap.put(list.get(i).getId(),list.get(i));
        }
    }

    public static UserContent getInstance() {
        return instance;
    }

    public User getUser(String id){
        if (userMap.containsKey(id)) return userMap.get(id);
        return null;
    }

    public List<User> getAllUser(String id,Integer age){
        if (userMap.isEmpty()) return new ArrayList<>();
        List<User> list = new ArrayList<>();
        for (Map.Entry<String, User> entry : userMap.entrySet()) {
            if (!StringUtil.isEmpty(id) && !entry.getValue().getId().equals(id))
                continue;
            if (age!=null && entry.getValue().getAge()!=age)
                continue;
            list.add(entry.getValue());
        }
        return list;
    }

    public boolean deleteUser(String id){
        if (userMap.containsKey(id)){
            userMap.remove(id);
            return true;
        }
        return false;
    }

    public boolean addUser(User user){
        if (!userMap.containsKey(user.getId())){
            userMap.put(user.getId(),user);
            return true;
        }
        return false;
    }

    public boolean putUser(User user){
        if (userMap.containsKey(user.getId())){
            userMap.put(user.getId(),user);
            return true;
        }
        return false;
    }

}
