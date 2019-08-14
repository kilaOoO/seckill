package com.bingsenh.seckill.domain;

import lombok.Data;

import java.util.Date;

/**
 * @Author hbs
 * @Date 2019/8/14
 */
@Data
public class MiaoshaGoods {
    private Long id;
    private Long goodsId;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
