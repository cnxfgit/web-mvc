package com.web.mvc.framework.util;

public class ThreadUtil {

    public static void sleep(Integer millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
