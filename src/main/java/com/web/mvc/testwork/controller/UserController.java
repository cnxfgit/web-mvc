package com.web.mvc.testwork.controller;

import com.web.mvc.framework.annotation.RequestMapping;
import com.web.mvc.framework.annotation.bean.Autowired;
import com.web.mvc.framework.annotation.component.Controller;
import com.web.mvc.framework.annotation.param.RequestBody;
import com.web.mvc.framework.annotation.param.RequestParam;
import com.web.mvc.framework.common.Result;
import com.web.mvc.framework.log.Log;
import com.web.mvc.framework.log.LogFactory;
import com.web.mvc.testwork.common.TableResult;
import com.web.mvc.testwork.entity.User;
import com.web.mvc.testwork.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    Log logger = LogFactory.getSimpleLog(UserController.class);

    @Autowired
    private UserService userService;


    @RequestMapping("/get")
    public TableResult getUser(@RequestParam("page") String page,
                               @RequestParam("limit") String limit,
                               @RequestParam("id") String id,
                               @RequestParam("age") Integer age){

        List<User> list = userService.getUser(id,age);
        return TableResult.ok(list,list.size());
    }

    @RequestMapping("/getByName")
    public User getUserByName(@RequestParam("name") String name){
        System.out.println(name);
        User user = userService.getUserByName(name);
        return user;
    }

    @RequestMapping("/add")
    public Result addUser(@RequestBody User user){
        System.out.println(user);
        if (userService.addUser(user)){
            return Result.ok("添加成功!");
        }else return Result.ok("添加失败!");
    }

    @RequestMapping("/delete")
    public Result deleteUser(@RequestParam("id") String id){
        System.out.println(id);
        if (userService.deleteUser(id)){
            return Result.ok("删除成功!");
        }else return Result.ok("删除失败!");
    }

    @RequestMapping("/update")
    public Result updateUser(@RequestBody User user){
        System.out.println(user);
        if (userService.updateUser(user)){
            return Result.ok("更新成功!");
        }else return Result.ok("更新失败!");
    }

}
