package com.bingsenh.seckill.service;

import com.bingsenh.seckill.domain.Goods;
import com.bingsenh.seckill.domain.MiaoshaOrder;
import com.bingsenh.seckill.domain.MiaoshaUser;
import com.bingsenh.seckill.domain.OrderInfo;
import com.bingsenh.seckill.redis.MiaoshaKey;
import com.bingsenh.seckill.redis.RedisService;
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
    @Autowired
    RedisService redisService;

    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods){
        // 减库存 下订单
        boolean success = goodsService.reduceStock(goods);
        if(success) {
            return orderService.createOrder(user, goods);
        }else {
            setGoodsOver(goods.getId());
            return null;
        }
    }

    public long getMiaoshaResult(Long userId,long goodsId){
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(userId,goodsId);
        if(order!=null){
            return order.getOrderId();
        }else{
            boolean isOver = getGoodsOver(goodsId);
            if(isOver){
                return -1;
            }else {
                return 0;
            }
        }
    }

    private void setGoodsOver(Long goodsId){
        redisService.set(MiaoshaKey.isGoodsOver,""+goodsId,true);
    }

    private boolean getGoodsOver(long goodsId){
        return redisService.exists(MiaoshaKey.isGoodsOver,""+goodsId);
    }


}
