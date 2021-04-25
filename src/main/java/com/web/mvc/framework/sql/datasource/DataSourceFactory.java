package com.web.mvc.framework.sql.datasource;

import com.web.mvc.framework.log.Log;
import com.web.mvc.framework.log.LogFactory;
import com.web.mvc.framework.util.StringUtil;

public class DataSourceFactory {

    private static Log logger = LogFactory.getSimpleLog(DataSourceFactory.class);

    public static DataSource createDataSource(DriverBody driverBody){
        if (!checkDriver(driverBody.getDriverClass())){
            logger.err("数据库驱动有误!");
            return null;
        }
        if (StringUtil.isEmpty(driverBody.getUrl())){
            logger.err("数据库url有误!");
            return null;
        }
        if (StringUtil.isEmpty(driverBody.getUsername())){
            logger.err("数据库用户名有误!");
            return null;
        }
        if (StringUtil.isEmpty(driverBody.getPassword())){
            logger.err("数据库密码有误!");
            return null;
        }
        return new DataSource(driverBody);
    }

    private static boolean checkDriver(String driverClass) {
        if (StringUtil.isEmpty(driverClass)) return false;
        try {
            Class.forName(driverClass);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
