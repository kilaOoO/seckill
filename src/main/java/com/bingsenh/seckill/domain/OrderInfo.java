package com.bingsenh.seckill.domain;

import lombok.Data;

import java.util.Date;

/**
 * @Author hbs
 * @Date 2019/8/14
 */
@Data
public class OrderInfo {
    private Long id;
    private Long userId;
    private Long goodsId;
    private Long  deliveryAddrId;
    private String goodsName;
    private Integer goodsCount;
    private Double goodsPrice;
    private Integer orderChannel;
    private Integer status;
    private Date createDate;
    private Date payDate;
}
