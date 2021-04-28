package com.web.mvc.framework.sql;

import com.web.mvc.framework.sql.datasource.DataSource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


public class DataProxy<T> implements InvocationHandler {

    private final DataSource dataSource;


    public DataProxy(DataSource dataSource) {
        this.dataSource = dataSource;

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        DataMethod dataMethod = new DataMethod(method,args);
        return dataMethod.execute(dataSource);
    }

}
