package com.bingsenh.seckill.utils;

import java.util.UUID;

/**
 * @Author hbs
 * @Date 2019/8/13
 */
public class UUIDUtil {
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
