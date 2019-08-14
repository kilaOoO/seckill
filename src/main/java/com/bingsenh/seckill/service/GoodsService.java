package com.bingsenh.seckill.service;

import com.bingsenh.seckill.dao.GoodsDao;
import com.bingsenh.seckill.domain.Goods;
import com.bingsenh.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author hbs
 * @Date 2019/8/14
 */
@Service
public class GoodsService {
    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId){
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }


    public void reduceStock(GoodsVo goods){
        long goodsId = goods.getId();
        goodsDao.reduceStock(goodsId);
    }

}
