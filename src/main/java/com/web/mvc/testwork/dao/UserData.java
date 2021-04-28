package com.web.mvc.testwork.dao;

import com.web.mvc.framework.annotation.sql.Data;
import com.web.mvc.framework.annotation.sql.Param;
import com.web.mvc.framework.annotation.sql.Select;
import com.web.mvc.testwork.entity.User;

import java.util.List;

@Data
public interface UserData {

    @Select("select * from user where id = ?")
    List<User> print(@Param("id")Integer i);

    String update();

}
