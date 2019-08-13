package com.bingsenh.seckill.controller;

import com.bingsenh.seckill.result.Result;
import com.bingsenh.seckill.service.MiaoshaUserService;
import com.bingsenh.seckill.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;


/**
 * @Author hbs
 * @Date 2019/8/12
 */
@Controller
@RequestMapping("/login")
public class LoginController {
    private static Logger logger = (Logger) LoggerFactory.getLogger(LoginController.class);

    @Autowired
    MiaoshaUserService userService;

    @RequestMapping("to_login")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("do_login")
    @ResponseBody
    public Result<Boolean> doLogin(@Valid LoginVo loginVo){
        logger.info(loginVo.toString());
        //登录
        userService.login(loginVo);
        return Result.success(true);
    }






}
