package com.web.mvc.util;

public class ThreadUtil {

    public static void stop(Integer seconds){
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
