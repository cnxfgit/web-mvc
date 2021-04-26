package com.web.mvc.testwork.controller;

import com.web.mvc.framework.annotation.Value;
import com.web.mvc.framework.annotation.component.Component;
import com.web.mvc.framework.sql.datasource.DataSource;
import com.web.mvc.framework.sql.datasource.DataSourceFactory;
import com.web.mvc.framework.sql.datasource.DriverBody;

@Component
public class TestComponent {

    @Value("dataSource.username")
    private String username;

    @Value("dataSource.password")
    private String password;

    @Value("dataSource.url")
    private String url;

    @Value("dataSource.driverClass")
    private String driverClass;


    // 注册进容器

    public DataSource setDataSource(){
        DriverBody driverBody = new DriverBody();
        driverBody.setDriverClass(driverClass);
        driverBody.setUsername(username);
        driverBody.setPassword(password);
        driverBody.setUrl(url);
        driverBody.setMaxConnection(20);
        driverBody.setMinConnection(5);
        return DataSourceFactory.createDataSource(driverBody);
    }
}
