package com.bingsenh.seckill.controller;

import com.bingsenh.seckill.domain.User;
import com.bingsenh.seckill.redis.KeyPrefix;
import com.bingsenh.seckill.redis.RedisService;
import com.bingsenh.seckill.redis.UserKey;
import com.bingsenh.seckill.result.CodeMsg;
import com.bingsenh.seckill.result.Result;
import com.bingsenh.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by bingsenh on 2019/8/9.
 */
@Controller
@RequestMapping("/demo")
public class SimpleController {

    @Autowired
    UserService userService;
    @Autowired
    RedisService redisService;

    @RequestMapping("/thymeleaf")
    public String  thymeleaf(Model model){
        model.addAttribute("name","hbs");
        return "hello";
    }

    @RequestMapping("/success")
    @ResponseBody
    public Result<String> success(){
        return  Result.success("hello");
    }


    @RequestMapping("/error")
    @ResponseBody
    public Result<String> error(){
        return  Result.error(CodeMsg.SERVER_ERROR);
    }


    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbGet(){
        User user = userService.getById(1);
        return Result.success(user);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet(){
        User user =redisService.get(UserKey.getById,""+1,User.class);
        return Result.success(user);
    }


    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet(){
        User user = new User();
        user.setId(1);
        user.setName("hbs");
        redisService.set(UserKey.getById,""+1,user);
        return Result.success(true);
    }
}
