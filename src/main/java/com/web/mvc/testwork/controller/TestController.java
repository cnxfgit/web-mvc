package com.web.mvc.testwork.controller;

import com.web.mvc.framework.annotation.RequestMapping;
import com.web.mvc.framework.annotation.Value;
import com.web.mvc.framework.annotation.bean.Autowired;
import com.web.mvc.framework.annotation.component.RestController;
import com.web.mvc.framework.sql.datasource.DataSource;
import com.web.mvc.framework.util.JdbcUtil;
import com.web.mvc.testwork.entity.User;
import com.web.mvc.testwork.service.TestService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @Value("scanPackage")
    String packageName;

    @Value("encoding")
    String encoding;

    @Autowired
    private User user;

    @Autowired
    private DataSource dataSource;

    @RequestMapping("/te")
    public String te(){
        System.out.println(packageName);
        System.out.println(user.getName());
        System.out.println(encoding);
        return packageName + "<br/>" + user.getName() + "<br/>" +encoding;
    }

    @RequestMapping("/sql")
    public String sql(){
        for (int i = 0; i < 50; i++) {
            new Thread(()->{
                Connection connection = dataSource.getConnection();
                Statement statement = JdbcUtil.createStatement(connection);
                ResultSet resultSet = JdbcUtil.executeQuery(statement,"select * from user");
                JdbcUtil.close(resultSet);
                JdbcUtil.close(statement);
                dataSource.setConnection(connection);
            }).start();
        }
        return "ok";
    }
}
