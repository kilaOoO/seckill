package com.bingsenh.seckill.controller;

import com.bingsenh.seckill.domain.Goods;
import com.bingsenh.seckill.domain.MiaoshaUser;
import com.bingsenh.seckill.domain.OrderInfo;
import com.bingsenh.seckill.redis.RedisService;
import com.bingsenh.seckill.result.CodeMsg;
import com.bingsenh.seckill.result.Result;
import com.bingsenh.seckill.service.GoodsService;
import com.bingsenh.seckill.service.MiaoshaUserService;
import com.bingsenh.seckill.service.OrderService;
import com.bingsenh.seckill.vo.GoodsVo;
import com.bingsenh.seckill.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by bingsenh on 2020/2/3.
 */

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    MiaoshaUserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(MiaoshaUser user, @RequestParam("orderId") long orderId){
        if(user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo order = orderService.getOrderById(orderId);
        if(order == null){
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        long goodsId = order.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrder(order);
        vo.setGoods(goods);
        return Result.success(vo);
    }
}
