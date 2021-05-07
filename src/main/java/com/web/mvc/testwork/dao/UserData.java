package com.web.mvc.testwork.dao;

import com.web.mvc.framework.annotation.sql.*;
import com.web.mvc.testwork.entity.User;

import java.util.List;

@Data
public interface UserData {

    @Select("select * from user where 1=1 @{and id = ?} @{and age = ?} limit @{?},@{?}")
    List<User> getUsers(@Param String id,@Param Integer age,@Param Integer page, @Param Integer limit);

    @Update("update user set name = @{?} where id = @{?}")
    Integer update(@Param String name,@Param String id,@Param Integer page, @Param Integer limit);

}
