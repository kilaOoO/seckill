package com.bingsenh.seckill.redis;

import org.apache.tomcat.util.buf.UEncoder;

/**
 * Created by bingsenh on 2019/8/10.
 */
public class UserKey extends BasePrefix {
    public UserKey(String prefix) {
        super(prefix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");
}
