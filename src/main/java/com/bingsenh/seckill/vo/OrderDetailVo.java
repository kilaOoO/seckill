package com.bingsenh.seckill.vo;

import com.bingsenh.seckill.domain.OrderInfo;

/**
 * Created by bingsenh on 2020/2/3.
 */
public class OrderDetailVo {
    private GoodsVo goods;
    private OrderInfo order;
    public GoodsVo getGoods() {
        return goods;
    }
    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }
    public OrderInfo getOrder() {
        return order;
    }
    public void setOrder(OrderInfo order) {
        this.order = order;
    }
}
