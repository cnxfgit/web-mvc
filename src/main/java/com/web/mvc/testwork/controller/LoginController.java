package com.web.mvc.testwork.controller;

import com.web.mvc.framework.annotation.RequestMapping;
import com.web.mvc.framework.annotation.component.Controller;
import com.web.mvc.framework.annotation.param.RequestParam;
import com.web.mvc.framework.common.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @RequestMapping("/login")
    public Result login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam("verCode") Integer code,
                        HttpServletRequest request,
                        HttpServletResponse response){

        HttpSession session = request.getSession();
        Integer i = (Integer) session.getAttribute("captcha");

        System.out.println("i"+i);
        System.out.println("code"+code);

        if (code != i) return Result.fail("验证码错误!");
        if (!"root".equals(username) || !"1234".equals(password)) return Result.fail("用户名或密码错误!");


        session.setAttribute("user","");

        return Result.ok("登录成功");
    }

}
