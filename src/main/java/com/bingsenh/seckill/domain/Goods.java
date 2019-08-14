package com.bingsenh.seckill.domain;

import lombok.Data;

/**
 * @Author hbs
 * @Date 2019/8/14
 */
@Data
public class Goods {
    private Long id;
    private String goodsName;
    private String goodsTitle;
    private String goodsImg;
    private String goodsDetail;
    private Double goodsPrice;
    private Integer goodsStock;
}
