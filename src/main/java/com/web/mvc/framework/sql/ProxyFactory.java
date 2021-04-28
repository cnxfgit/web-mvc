package com.web.mvc.framework.sql;

import com.web.mvc.framework.sql.datasource.DataSource;

import java.lang.reflect.Proxy;

public class ProxyFactory<T> {

    private final Class<T> dataInterface;

    public ProxyFactory(Class<T> mapperInterface) {
        this.dataInterface = mapperInterface;
    }

    private T newInstance(DataProxy<T> dataProxy) {
        return (T) Proxy.newProxyInstance(dataInterface.getClassLoader(), new Class[] {dataInterface}, dataProxy);
    }

    public T newInstance(DataSource dataSource) {
        final DataProxy<T> dataProxy = new DataProxy<T>(dataSource);
        return newInstance(dataProxy);
    }

}
