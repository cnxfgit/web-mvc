package com.web.mvc.framework.util;

import com.web.mvc.framework.log.Log;
import com.web.mvc.framework.log.LogFactory;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.List;
import java.util.concurrent.Executors;

public class JdbcUtil {

    private static final Log logger = LogFactory.getSimpleLog(JdbcUtil.class);

    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                logger.err("连接关闭失败!");
                e.printStackTrace();
            }
        }
    }

    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (Exception e) {
                logger.err("statement关闭失败!");
                e.printStackTrace();
            }
        }
    }

    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (Exception var2) {
                logger.err("resultSet关闭失败!");
            }
        }
    }

    public synchronized static void printResultSet(ResultSet resultSet, String separator) {
        ResultSetMetaData metadata = null;
        PrintStream out = System.out;
        int columnCount = 0;
        try {
            metadata = resultSet.getMetaData();
            columnCount = metadata.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                if (i != 1) {
                    out.print(separator);
                }
                out.print(metadata.getColumnName(i));
            }

            out.println();

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    if (i != 1) {
                        out.print(separator);
                    }
                    out.print(resultSet.getString(i));
                }
                out.println();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void setNetworkTimeout(Connection connection, Integer milliseconds) {
        try {
            connection.setNetworkTimeout(Executors.newSingleThreadExecutor(), milliseconds);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setAutoCommit(Connection connection, boolean bool) {
        try {
            connection.setAutoCommit(bool);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public static Statement createStatement(Connection connection) {
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResultSet executeQuery(Statement statement, String sql) {
        try {
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Integer executeUpdate(Statement statement, String sql) {
        try {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void fieldChange(Field field, ResultSet resultSet, Object obj) throws SQLException {
        if (field.getType() == int.class || field.getType() == Integer.class) {
            int f = resultSet.getInt(field.getName());
            ReflectUtil.setField(field, obj, Integer.valueOf(f));
        } else {
            String f = resultSet.getString(field.getName());
            ReflectUtil.setField(field, obj, f);
        }
    }

    public static String sqlParam(String sql, Object[] args) {
        List<String> list = StringUtil.getChildren(sql,"@{","}");
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof String || args[i] instanceof Character) {
                String newStr = StringUtil.replaceFirst(list.get(i),
                        "?","'" + args[i].toString() + "'");
                sql = StringUtil.replaceFirst(sql,list.get(i), newStr.substring(2,newStr.length()-1));
            } else if (args[i] instanceof Integer || args[i] instanceof Long ||
                    args[i] instanceof Short || args[i] instanceof Byte ||
                    args[i] instanceof Double || args[i] instanceof Float ||
                    args[i] instanceof Boolean) {
                String newStr = StringUtil.replaceFirst(list.get(i), "?", args[i].toString());
                sql = StringUtil.replaceFirst(sql,list.get(i), newStr.substring(2,newStr.length()-1));
            } else if (args[i] == null){
                System.out.println(sql);
                System.out.println(list.get(i));
                sql = StringUtil.replaceFirst(sql,list.get(i), "");
            }
        }
        return sql;
    }

}
