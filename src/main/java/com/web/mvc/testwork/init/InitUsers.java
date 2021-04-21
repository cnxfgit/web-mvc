package com.web.mvc.testwork.init;

import com.web.mvc.testwork.entity.User;

import java.util.ArrayList;
import java.util.List;

public class InitUsers {

    List<User> list;

    public InitUsers(){
        list = new ArrayList<>();
        String[] ids = {"zhangsan","lisi","wangwu","zhaoliu"};
        int[] ages = {31,21,31,31};
        String[] names = {"张三","李四","王五","赵六"};
        for (int i = 0; i < ids.length; i++) {
            User user = new User();
            user.setId(ids[i]);
            user.setName(names[i]);
            user.setAge(ages[i]);
            list.add(user);
        }
    }

    public List<User> getList() {
        return list;
    }
}
