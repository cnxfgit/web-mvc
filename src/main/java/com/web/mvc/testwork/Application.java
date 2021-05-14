package com.web.mvc.testwork;

import com.web.mvc.framework.WebMvcApplication;
import com.web.mvc.framework.annotation.component.RequestMapping;
import com.web.mvc.framework.annotation.WebMvcRun;
import com.web.mvc.framework.annotation.component.Controller;

@WebMvcRun
@Controller
public class Application {

    public static void main(String[] args) {
        WebMvcApplication.run(Application.class,8080,args);
    }

    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }

}
