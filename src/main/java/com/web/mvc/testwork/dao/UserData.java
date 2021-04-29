package com.web.mvc.testwork.dao;

import com.web.mvc.framework.annotation.sql.*;
import com.web.mvc.testwork.entity.User;

import java.util.List;

@Data
public interface UserData {

    @Select("select * from user @{ where id = ?}")
    List<User> print(@Param Integer id);

    @Update("update user set name = @{?} where id = @{?}")
    Integer update(@Param String name,@Param Integer id);

    @Insert("insert into ")
    Integer insert();

}
