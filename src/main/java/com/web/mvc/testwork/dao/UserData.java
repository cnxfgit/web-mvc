package com.web.mvc.testwork.dao;

import com.web.mvc.framework.annotation.sql.*;
import com.web.mvc.testwork.entity.User;

import java.util.List;

@Data
public interface UserData {

    @Select("select * from user where id = @{?}")
    User findUserById(String id);

    @Select("select * from user where 1=1 @{and id = ?} @{and age = ?} limit @{?},@{?}")
    List<User> getUsers(@Param String id,@Param Integer age,@Param Integer page, @Param Integer limit);

    @Delete("delete from user where id = @{?}")
    Integer deleteUser(@Param String id);

    @Insert("insert into user values(@{?},@{?},@{?})")
    Integer addUser(@Param String id,@Param String name,@Param Integer age);

    @Update("update user set name = @{?},age = @{?} where id = @{?}")
    Integer updateUser(@Param String name,@Param Integer age,@Param String id);

}
