package com.web.mvc.framework.log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

    private static String className;
    private static final String PRE = " ==> ";

    private Log(Class clazz){
        className = clazz.getName();
    }

    public static Log getLogger(Class clazz){
        return new Log(clazz);
    }

    public void info(String msg){
        System.out.println(dateTime() + " 信息 " + className + " " + Thread.currentThread()
                + "\n" + PRE + msg);
    }

    public void err(String msg){
        System.err.println(dateTime() + " 错误 " + className + " " + Thread.currentThread()
                + "\n" + PRE + msg);
    }

    private static String dateTime(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

}
