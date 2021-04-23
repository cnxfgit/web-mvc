package com.web.mvc.framework.log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleLog implements Log{

    private static String className;
    private static final String PRE = " ==> ";

    private SimpleLog(Class clazz){
        className = clazz.getName();
    }

    public static SimpleLog getLogger(Class clazz){
        return new SimpleLog(clazz);
    }

    @Override
    public void info(String msg){
        System.out.println(dateTime() + " 信息 " + className + " " + Thread.currentThread()
                + "\n" + PRE + msg);
    }

    @Override
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
