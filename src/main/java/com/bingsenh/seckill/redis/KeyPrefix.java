package com.bingsenh.seckill.redis;

/**
 * Created by bingsenh on 2019/8/10.
 */
public interface KeyPrefix {
    public int expireSeconds();
    public String getPrefix();
}
