package com.web.mvc.framework.util;


import com.web.mvc.framework.log.Log;
import com.web.mvc.framework.log.LogFactory;

import java.io.PrintStream;
import java.sql.*;
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

    public synchronized static void printResultSet(ResultSet resultSet, String separator){
        ResultSetMetaData metadata = null;
        PrintStream out = System.out;
        int columnCount = 0;
        try {
            metadata = resultSet.getMetaData();
            columnCount = metadata.getColumnCount();
            for(int i = 1; i <= columnCount; i++) {
                if (i != 1) {
                    out.print(separator);
                }
                out.print(metadata.getColumnName(i));
            }

            out.println();

            while (resultSet.next()){
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

    public static void setNetworkTimeout(Connection connection,Integer milliseconds){
        try {
            connection.setNetworkTimeout(Executors.newSingleThreadExecutor(),milliseconds);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setAutoCommit(Connection connection,boolean bool){
        try {
            connection.setAutoCommit(bool);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rollback(Connection connection){
        try {
            connection.rollback();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }



    public static Statement createStatement(Connection connection){
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResultSet executeQuery(Statement statement,String sql){
        try {
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
