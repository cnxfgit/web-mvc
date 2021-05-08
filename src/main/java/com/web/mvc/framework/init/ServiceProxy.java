package com.web.mvc.framework.init;

import com.web.mvc.framework.annotation.sql.Transactional;
import com.web.mvc.framework.content.BeanContent;
import com.web.mvc.framework.sql.datasource.DataSource;
import com.web.mvc.framework.util.JdbcUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Map;

public class ServiceProxy<T> implements InvocationHandler {

    T instance;

    // bean容器
    private BeanContent beanContent = BeanContent.getInstance();

    public ServiceProxy(T instance){
        this.instance = instance;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 获取真实对象的方法
        Method iMethod = instance.getClass().getMethod(method.getName(),method.getParameterTypes());
        DataSource dataSource = (DataSource) beanContent.getBean(DataSource.class.getSimpleName());

        Object result = null;
        if (iMethod.isAnnotationPresent(Transactional.class) && dataSource != null){// 开启sql事务处理

            Connection connection = dataSource.getConnection();
            Map<Long,Connection> map = dataSource.getThreadMap();
            map.put(Thread.currentThread().getId(),connection);
            JdbcUtil.setAutoCommit(connection,false);

            try {
                result = method.invoke(instance,args);
                connection.commit();
            }catch (Exception e){
                e.printStackTrace();
                JdbcUtil.rollback(connection);
            }finally {
                JdbcUtil.setAutoCommit(connection,true);
                dataSource.setConnection(connection);
                map.remove(Thread.currentThread().getId());
            }

            return result;
        }
        return method.invoke(instance,args);
    }
}