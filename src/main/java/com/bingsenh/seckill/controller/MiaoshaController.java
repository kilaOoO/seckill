package com.bingsenh.seckill.controller;

import com.bingsenh.seckill.domain.MiaoshaOrder;
import com.bingsenh.seckill.domain.MiaoshaUser;
import com.bingsenh.seckill.domain.OrderInfo;
import com.bingsenh.seckill.result.CodeMsg;
import com.bingsenh.seckill.service.GoodsService;
import com.bingsenh.seckill.service.MiaoshaService;
import com.bingsenh.seckill.service.MiaoshaUserService;
import com.bingsenh.seckill.service.OrderService;
import com.bingsenh.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author hbs
 * @Date 2019/8/14
 */

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {
    @Autowired
    MiaoshaUserService miaoshaUserService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @RequestMapping("do_miaosha")
    public String list(Model model, MiaoshaUser user, @RequestParam("goodsId") long goodsId){

        model.addAttribute("user",user);
        if(user == null){
            return "login";
        }

        // 判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock <= 0){
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
            return "miaosha_fail";
        }

        // 判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if(order!=null){
            model.addAttribute("errmsg",CodeMsg.REPEATE_MIAOSHA.getMsg());
            return "miaosha_fail";
        }

        // 减库存 下订单
        OrderInfo orderInfo = miaoshaService.miaosha(user,goods);
        model.addAttribute("orderInfo",orderInfo);
        model.addAttribute("goods",goods);

        return "order_detail";
    }
}
