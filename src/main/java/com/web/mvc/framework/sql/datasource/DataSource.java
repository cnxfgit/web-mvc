package com.web.mvc.framework.sql.datasource;

import com.web.mvc.framework.log.Log;
import com.web.mvc.framework.log.LogFactory;
import com.web.mvc.framework.util.JdbcUtil;
import com.web.mvc.framework.util.ThreadUtil;

import java.sql.*;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DataSource {

    private DriverBody driverBody;
    private static Log logger = LogFactory.getSimpleLog(DataSource.class);

    private AtomicInteger atomicInteger = new AtomicInteger(0);// 原子类型线程安全
    private BlockingQueue<Connection> pool;
    private Map<Long,Connection> threadMap = new ConcurrentHashMap<>();

    public Map<Long,Connection> getThreadMap(){
        return threadMap;
    }

    public void setConnection(Connection connection) {
        pool.offer(connection);
    }

    public Connection getConnection() {
        synchronized (this){
            if (pool.size() > 0) {
                return pool.poll();
            }
            if (atomicInteger.get()<driverBody.getMaxConnection()){
                Connection connection = createConnection(
                        driverBody.getUrl(),driverBody.getUsername(),driverBody.getPassword());
                pool.offer(connection);
                atomicInteger.incrementAndGet();
                return pool.poll();
            }
        }
        ThreadUtil.sleep(1);
        return getConnection();
    }

    public DataSource(DriverBody driverBody) {
        this.driverBody = driverBody;
        pool = new ArrayBlockingQueue<>(driverBody.getMaxConnection());

        new Thread(()->{
            while (true){
                ThreadUtil.sleep(5*1000);
                synchronized (this){
                    if (pool.size()>driverBody.getMinConnection()) {
                        atomicInteger.decrementAndGet();
                        Connection conn= pool.poll();
                        JdbcUtil.close(conn);// 关闭连接，释放资源
                        logger.info("关闭一条数据库连接!");
                        logger.info("当前可用连接为:" + pool.size() + "条!");
                        logger.info("当前总连接为:" + atomicInteger.get() + "条!");
                    }
                }
            }
        }).start();

        try {
            testConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void testConnection() throws SQLException {
        Connection connection = createConnection(
                driverBody.getUrl(),driverBody.getUsername(),driverBody.getPassword());
        atomicInteger.incrementAndGet();
        Statement statement = JdbcUtil.createStatement(connection);
        String sql = "select 1 from dual";
        ResultSet resultSet = JdbcUtil.executeQuery(statement,sql);
        while (resultSet.next()){
            logger.info("测试连接成功!sql=" + sql + "结果:" + resultSet.getString("1"));
        }
        JdbcUtil.close(resultSet);
        JdbcUtil.close(statement);
        setConnection(connection);
    }


    private Connection createConnection(String url, String username, String password){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

}
