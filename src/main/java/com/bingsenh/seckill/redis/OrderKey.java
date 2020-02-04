package com.bingsenh.seckill.redis;

/**
 * Created by bingsenh on 2019/8/10.
 */
public class OrderKey extends BasePrefix{
    public OrderKey(String prefix) {
        super(prefix);
    }
    public static OrderKey getMiaoshaOrderByUidGid = new OrderKey("moug");
}
