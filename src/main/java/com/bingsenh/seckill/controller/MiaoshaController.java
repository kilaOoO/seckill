package com.bingsenh.seckill.controller;

import com.bingsenh.seckill.domain.MiaoshaOrder;
import com.bingsenh.seckill.domain.MiaoshaUser;
import com.bingsenh.seckill.domain.OrderInfo;
import com.bingsenh.seckill.result.CodeMsg;
import com.bingsenh.seckill.result.Result;
import com.bingsenh.seckill.service.GoodsService;
import com.bingsenh.seckill.service.MiaoshaService;
import com.bingsenh.seckill.service.MiaoshaUserService;
import com.bingsenh.seckill.service.OrderService;
import com.bingsenh.seckill.vo.GoodsVo;
import com.sun.org.apache.bcel.internal.classfile.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping(value="/do_miaosha", method= RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> list(Model model, MiaoshaUser user, @RequestParam("goodsId") long goodsId){

        model.addAttribute("user",user);
        if(user == null){
            //return "login";
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        // 判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock <= 0){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        // 判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if(order!=null){
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }

        // 减库存 下订单
        OrderInfo orderInfo = miaoshaService.miaosha(user,goods);
        return Result.success(orderInfo);
        //return "order_detail";
    }
}
