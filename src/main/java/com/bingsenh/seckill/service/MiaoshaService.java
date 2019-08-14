package com.bingsenh.seckill.service;

import com.bingsenh.seckill.domain.Goods;
import com.bingsenh.seckill.domain.MiaoshaUser;
import com.bingsenh.seckill.domain.OrderInfo;
import com.bingsenh.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author hbs
 * @Date 2019/8/14
 */
@Service
public class MiaoshaService {

    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;

    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods){
        // 减库存 下订单
        goodsService.reduceStock(goods);
        return orderService.createOrder(user,goods);
    }


}
