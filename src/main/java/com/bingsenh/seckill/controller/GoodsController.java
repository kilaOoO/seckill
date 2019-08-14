package com.bingsenh.seckill.controller;

import com.bingsenh.seckill.domain.Goods;
import com.bingsenh.seckill.domain.MiaoshaUser;
import com.bingsenh.seckill.redis.RedisService;
import com.bingsenh.seckill.service.GoodsService;
import com.bingsenh.seckill.service.MiaoshaUserService;
import com.bingsenh.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Author hbs
 * @Date 2019/8/13
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    MiaoshaUserService miaoshaUserService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/to_list")
    public String list(Model model, MiaoshaUser user){
        model.addAttribute("user",user);
        //查询商品列表
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList",goodsList);
        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model, MiaoshaUser user, @PathVariable("goodsId") long goodsId){

        model.addAttribute("user",user);
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods",goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now<startAt){
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt-now)/1000);
        }else if(now > endAt){
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus",miaoshaStatus);
        model.addAttribute("remainSeconds",remainSeconds);

        return "goods_detail";

    }



}
