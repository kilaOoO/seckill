package com.bingsenh.seckill.vo;

import com.bingsenh.seckill.domain.Goods;
import lombok.Data;

import java.util.Date;

/**
 * @Author hbs
 * @Date 2019/8/14
 */
@Data
public class GoodsVo extends Goods {
    private Double miaoshaPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
