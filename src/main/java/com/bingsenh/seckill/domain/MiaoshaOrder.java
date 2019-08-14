package com.bingsenh.seckill.domain;

import lombok.Data;

/**
 * @Author hbs
 * @Date 2019/8/14
 */
@Data
public class MiaoshaOrder {
    private Long id;
    private Long userId;
    private Long  orderId;
    private Long goodsId;
}
