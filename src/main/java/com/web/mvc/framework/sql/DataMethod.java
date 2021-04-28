package com.web.mvc.framework.sql;

import com.web.mvc.framework.annotation.sql.Delete;
import com.web.mvc.framework.annotation.sql.Insert;
import com.web.mvc.framework.annotation.sql.Select;
import com.web.mvc.framework.annotation.sql.Update;
import com.web.mvc.framework.log.Log;
import com.web.mvc.framework.log.LogFactory;
import com.web.mvc.framework.sql.datasource.DataSource;
import com.web.mvc.framework.util.JdbcUtil;
import com.web.mvc.framework.util.ReflectUtil;
import com.web.mvc.framework.util.StringUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataMethod {

    private static final Log logger = LogFactory.getSimpleLog(DataMethod.class);

    private final Method method;
    private final Object[] args;

    public DataMethod(Method method,Object[] args){
        this.method = method;
        this.args = args;
    }

    public Object execute(DataSource dataSource) {
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation:annotations) {
            if (annotation instanceof Select){
                return select(dataSource);
            }
            if (annotation instanceof Insert){
                return insert(dataSource);
            }
            if (annotation instanceof Delete){
                return delete(dataSource);
            }
            if (annotation instanceof Update){
                return update(dataSource);
            }
        }
        return method.getReturnType();
    }

    private Object update(DataSource dataSource) {
        System.out.println("update");
        return null;
    }

    private Object delete(DataSource dataSource) {
        System.out.println("delete");
        return null;
    }

    private Object insert(DataSource dataSource) {
        System.out.println("insert");
        return null;
    }

    private Object select(Connection connection) {
        Class clazz = method.getReturnType();
        Select select = method.getAnnotation(Select.class);
        Statement statement = JdbcUtil.createStatement(connection);
        String sql = select.value();
        System.out.println(args.length);
        if (args.length != 0){
            for (int i = 0; i < args.length; i++) {
                sql = sql.replace("?",args[i].toString());
            }
        }
        System.out.println(sql);
        ResultSet resultSet = JdbcUtil.executeQuery(statement,sql);

        ArrayList res = null;
        try {
            if (clazz == List.class){
                Type type = method.getGenericReturnType();
                String genericClassName = StringUtil.getClassName(type);
                Class generic = Class.forName(genericClassName);
                res = new ArrayList<>();
                Field[] fields = generic.getDeclaredFields();

                while (resultSet.next()){
                    Object obj = ReflectUtil.newInstance(generic);
                    for (Field field:fields) {
                        field.setAccessible(true);
                        JdbcUtil.fieldChange(field,resultSet,obj);
                    }
                    res.add(obj);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JdbcUtil.close(resultSet);
            JdbcUtil.close(statement);
        }
        return res;
    }

    private Object select(DataSource dataSource) {

        Map<Long,Connection> threadMap = dataSource.getThreadMap();
        Connection threadCoon = threadMap.get(Thread.currentThread().getId());
        Connection connection = null;
        if (threadCoon != null){
            connection = threadCoon;
        }else {
            connection = dataSource.getConnection();
        }

        Class clazz = method.getReturnType();
        Select select = method.getAnnotation(Select.class);

        Statement statement = JdbcUtil.createStatement(connection);
        String sql = select.value();

        if (args.length != 0){
            sql = JdbcUtil.sqlParam(select.value(),args);
        }

        logger.info("执行sql: " + sql);
        ResultSet resultSet = JdbcUtil.executeQuery(statement,sql);

        ArrayList res = null;
        try {
            if (clazz == List.class){
                Type type = method.getGenericReturnType();
                String genericClassName = StringUtil.getClassName(type);
                Class generic = Class.forName(genericClassName);
                res = new ArrayList<>();
                Field[] fields = generic.getDeclaredFields();

                while (resultSet.next()){
                    Object obj = ReflectUtil.newInstance(generic);
                    for (Field field:fields) {
                        field.setAccessible(true);
                        if (field.getType() == int.class || field.getType() == Integer.class){
                            int f = resultSet.getInt(field.getName());
                            ReflectUtil.setField(field,obj,Integer.valueOf(f));
                        }else {
                            String f = resultSet.getString(field.getName());
                            ReflectUtil.setField(field,obj,f);
                        }
                    }
                    res.add(obj);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JdbcUtil.close(resultSet);
            JdbcUtil.close(statement);
            if (threadCoon == null){
                dataSource.setConnection(connection);
            }
        }
        return res;
    }
}
