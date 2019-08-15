package com.bingsenh.seckill.controller;

import com.bingsenh.seckill.domain.MiaoshaUser;
import com.bingsenh.seckill.result.Result;
import com.bingsenh.seckill.service.MiaoshaUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by bingsenh on 2019/8/15.
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @RequestMapping("/info")
    @ResponseBody
    public Result<MiaoshaUser> info(Model model,MiaoshaUser user){
        return Result.success(user);
    }
}
