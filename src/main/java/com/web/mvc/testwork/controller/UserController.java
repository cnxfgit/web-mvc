package com.web.mvc.testwork.controller;

import com.web.mvc.framework.annotation.component.RequestMapping;
import com.web.mvc.framework.annotation.aop.After;
import com.web.mvc.framework.annotation.aop.Before;
import com.web.mvc.framework.annotation.bean.Autowired;
import com.web.mvc.framework.annotation.component.Controller;
import com.web.mvc.framework.annotation.param.RequestBody;
import com.web.mvc.framework.annotation.param.RequestParam;
import com.web.mvc.framework.common.Result;
import com.web.mvc.framework.log.Log;
import com.web.mvc.framework.log.LogFactory;
import com.web.mvc.framework.util.StringUtil;
import com.web.mvc.testwork.common.TableResult;
import com.web.mvc.testwork.component.AopComponent;
import com.web.mvc.testwork.entity.User;
import com.web.mvc.testwork.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin/user")
public class UserController {

    private static Log logger = LogFactory.getSimpleLog(UserController.class);

    @Autowired
    private UserService userService;


    @RequestMapping("/get")
    @Before(bean = AopComponent.class,value = "开始查询用户!")
    @After(bean = AopComponent.class,value = "结束查询用户!")
    public TableResult getUser(@RequestParam("page") Integer page,
                               @RequestParam("limit") Integer limit,
                               @RequestParam("id") String id,
                               @RequestParam("age") Integer age){
        List<User> list = userService.getUser(StringUtil.emptyIsNull(id),age,page,limit);
        return TableResult.ok(list,list.size());
    }

    @RequestMapping("/add")
    @After(bean = AopComponent.class,value = "添加用户!")
    public Result addUser(@RequestBody User user){
       userService.addUser(user);
       return Result.ok("添加成功!");
    }

    @RequestMapping("/delete")
    @After(bean = AopComponent.class,value = "删除用户!")
    public Result deleteUser(@RequestParam("id") String id){
        userService.deleteUser(id);
        return Result.ok("删除成功!");
    }

    @RequestMapping("/update")
    @After(bean = AopComponent.class,value = "更新用户!")
    public Result updateUser(@RequestBody User user){
        userService.updateUser(user);
        return Result.ok("修改成功!");
    }

}
