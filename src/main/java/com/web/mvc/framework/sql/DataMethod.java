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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataMethod {

    private static final Log logger = LogFactory.getSimpleLog(DataMethod.class);

    private final Method method;
    private final Object[] args;

    public DataMethod(Method method, Object[] args) {
        this.method = method;
        this.args = args;
    }

    public Object execute(DataSource dataSource) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof Select) {
                return select(dataSource);
            }
            if (annotation instanceof Insert) {
                return insert(dataSource);
            }
            if (annotation instanceof Delete) {
                return delete(dataSource);
            }
            if (annotation instanceof Update) {
                return update(dataSource);
            }
        }
        return null;
    }

    private Object update(DataSource dataSource) throws SQLException {

        Map<Long, Connection> threadMap = dataSource.getThreadMap();
        Connection threadCoon = threadMap.get(Thread.currentThread().getId());
        Connection connection = null;
        if (threadCoon != null) {
            connection = threadCoon;
        } else {
            connection = dataSource.getConnection();
        }

        Statement statement = JdbcUtil.createStatement(connection);
        Update update = method.getAnnotation(Update.class);
        String sql = update.value();

        if (args != null && args.length != 0) {
            sql = JdbcUtil.sqlParam(update.value(), args);
        }

        logger.info("执行update: " + sql);
        Integer integer = null;
        try{
            integer = statement.executeUpdate(sql);
        }catch (Exception e){
            throw e;
        }finally {
            JdbcUtil.close(statement);
            if (threadCoon == null) {
                dataSource.setConnection(connection);
            }
        }

        return integer;
    }

    private Object delete(DataSource dataSource) throws SQLException {

        Map<Long, Connection> threadMap = dataSource.getThreadMap();
        Connection threadCoon = threadMap.get(Thread.currentThread().getId());
        Connection connection = null;
        if (threadCoon != null) {
            connection = threadCoon;
        } else {
            connection = dataSource.getConnection();
        }

        Statement statement = JdbcUtil.createStatement(connection);
        Delete delete = method.getAnnotation(Delete.class);
        String sql = delete.value();


        if (args != null && args.length != 0) {
            sql = JdbcUtil.sqlParam(delete.value(), args);
        }

        logger.info("执行delete: " + sql);
        Integer integer = null;
        try{
            integer = statement.executeUpdate(sql);
        }catch (Exception e){
            throw e;
        }finally {
            JdbcUtil.close(statement);
            if (threadCoon == null) {
                dataSource.setConnection(connection);
            }
        }

        return integer;
    }

    private Object insert(DataSource dataSource) throws SQLException {

        Map<Long, Connection> threadMap = dataSource.getThreadMap();
        Connection threadCoon = threadMap.get(Thread.currentThread().getId());
        Connection connection = null;
        if (threadCoon != null) {
            connection = threadCoon;
        } else {
            connection = dataSource.getConnection();
        }

        Statement statement = JdbcUtil.createStatement(connection);
        Insert insert = method.getAnnotation(Insert.class);
        String sql = insert.value();


        if (args != null && args.length != 0) {
            sql = JdbcUtil.sqlParam(insert.value(), args);
        }

        logger.info("执行insert: " + sql);
        Integer integer = null;
        try{
            integer = statement.executeUpdate(sql);
        }catch (Exception e){
            throw e;
        }finally {
            JdbcUtil.close(statement);
            if (threadCoon == null) {
                dataSource.setConnection(connection);
            }
        }

        return integer;
    }

    private Object select(DataSource dataSource) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        Map<Long, Connection> threadMap = dataSource.getThreadMap();
        Connection threadCoon = threadMap.get(Thread.currentThread().getId());
        Connection connection = null;
        if (threadCoon != null) {
            connection = threadCoon;
        } else {
            connection = dataSource.getConnection();
        }

        Class clazz = method.getReturnType();
        Select select = method.getAnnotation(Select.class);

        Statement statement = JdbcUtil.createStatement(connection);
        String sql = select.value();

        if (args != null && args.length != 0) {
            sql = JdbcUtil.sqlParam(select.value(), args);
        }

        logger.info("执行select: " + sql);
        ResultSet resultSet = JdbcUtil.executeQuery(statement, sql);

        ArrayList ArrRes = null;
        Object objRes = null;
        try {
            if (clazz == List.class) {
                Type type = method.getGenericReturnType();
                String genericClassName = StringUtil.getClassName(type);
                Class generic = Class.forName(genericClassName);
                ArrRes = new ArrayList<>();
                Field[] fields = generic.getDeclaredFields();

                while (resultSet.next()) {
                    Object obj = ReflectUtil.newInstance(generic);
                    for (Field field : fields) {
                        field.setAccessible(true);
                        JdbcUtil.fieldChange(field, resultSet, obj);
                    }
                    ArrRes.add(obj);
                }
            } else {
                objRes = null;
                Field[] fields = clazz.getDeclaredFields();
                while (resultSet.next()) {
                    objRes = clazz.newInstance();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        JdbcUtil.fieldChange(field, resultSet, objRes);
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            JdbcUtil.close(resultSet);
            JdbcUtil.close(statement);
            if (threadCoon == null) {
                dataSource.setConnection(connection);
            }
        }
        if (clazz == List.class) return ArrRes;
        else return objRes;
    }
}
