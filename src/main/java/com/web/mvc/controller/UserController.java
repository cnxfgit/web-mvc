package com.web.mvc.controller;

import com.web.mvc.annotation.RequestMapping;
import com.web.mvc.annotation.bean.Autowired;
import com.web.mvc.annotation.component.RestController;
import com.web.mvc.annotation.param.RequestBody;
import com.web.mvc.annotation.param.RequestParam;
import com.web.mvc.common.Result;
import com.web.mvc.common.TableResult;
import com.web.mvc.entity.User;
import com.web.mvc.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/get")
    public TableResult getUser(@RequestParam("page") Integer page,
                               @RequestParam("limit") Integer limit){

        List<User> list = userService.getUser();
        System.out.println(limit);
        System.out.println(page);
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
