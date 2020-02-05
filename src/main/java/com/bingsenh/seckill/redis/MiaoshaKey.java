package com.bingsenh.seckill.redis;

/**
 * Created by bingsenh on 2020/2/5.
 */
public class MiaoshaKey extends BasePrefix{
    private MiaoshaKey(String prefix) {
        super(prefix);
    }
    public static MiaoshaKey isGoodsOver = new MiaoshaKey("go");
}
