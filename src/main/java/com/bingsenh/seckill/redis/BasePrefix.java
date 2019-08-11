package com.bingsenh.seckill.redis;

/**
 * Created by bingsenh on 2019/8/10.
 */
public abstract class BasePrefix implements KeyPrefix {

    private int expireSeconds;
    private String prefix;

    public BasePrefix(String prefix){
        this(0,prefix);
    }
    public BasePrefix(int expireSeconds,String prefix){
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() {// 0代表永不过期
        return expireSeconds;
    }

    @Override
    public String getPrefix() {

        String className = getClass().getSimpleName();
        return className+":"+prefix;
    }
}
