package com.bingsenh.seckill.rabbitmq;

import com.bingsenh.seckill.domain.MiaoshaUser;
import lombok.Data;

/**
 * Created by bingsenh on 2020/2/4.
 */
@Data
public class MiaoshaMessage {
    private MiaoshaUser user;
    private long goodsId;
}
