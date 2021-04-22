package com.web.mvc.testwork;

import com.web.mvc.framework.WebMvcApplication;
import com.web.mvc.framework.annotation.WebMvcRun;

@WebMvcRun
public class Application {
    public static void main(String[] args) {
        WebMvcApplication.run(Application.class,8080,args);
    }
}
