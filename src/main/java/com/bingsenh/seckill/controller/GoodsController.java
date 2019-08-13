package com.bingsenh.seckill.controller;

import com.bingsenh.seckill.domain.MiaoshaUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author hbs
 * @Date 2019/8/13
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @RequestMapping("to_list")
    public String list(Model model, MiaoshaUser user){
        model.addAttribute("user",user);
        return "goods_list";
    }

}
