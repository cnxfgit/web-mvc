package com.web.mvc.framework.log;

public class LogFactory {

    public static Log getSimpleLog(Class clazz){
        return SimpleLog.getLogger(clazz);
    }

    public static Log getColorLog(Class clazz){
        return null;
    }

    public static Log getDataLog(Class clazz){
        return null;
    }

}
