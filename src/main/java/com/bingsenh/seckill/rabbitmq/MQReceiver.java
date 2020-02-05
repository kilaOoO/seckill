package com.bingsenh.seckill.rabbitmq;

import com.bingsenh.seckill.domain.Goods;
import com.bingsenh.seckill.domain.MiaoshaOrder;
import com.bingsenh.seckill.domain.MiaoshaUser;
import com.bingsenh.seckill.redis.RedisService;
import com.bingsenh.seckill.service.GoodsService;
import com.bingsenh.seckill.service.MiaoshaService;
import com.bingsenh.seckill.service.OrderService;
import com.bingsenh.seckill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by bingsenh on 2020/2/4.
 */
@Service
public class MQReceiver {
    public static Logger logger = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receiveMiaoshaMessage(String message){
        logger.info("receive message:"+ message);
        MiaoshaMessage mm = redisService.stringToBean(message,MiaoshaMessage.class);
        MiaoshaUser user = mm.getUser();
        long goodsId = mm.getGoodsId();

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getGoodsStock();
        if(stock<=0){
            return;
        }
        // 判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if(order!=null){
            return;
        }
        //减库存 下订单 写入秒杀订单
        miaoshaService.miaosha(user,goods);

    }


    @RabbitListener(queues = MQConfig.QUEUE)
    public void receive(String message){
        logger.info("receive message:"+message);
    }


    @RabbitListener(queues=MQConfig.TOPIC_QUEUE1)
    public void receiveTopic1(String message) {
        logger.info(" topic  queue1 message:"+message);
    }

    @RabbitListener(queues=MQConfig.TOPIC_QUEUE2)
    public void receiveTopic2(String message) {
        logger.info(" topic  queue2 message:"+message);
    }

    @RabbitListener(queues=MQConfig.HEADER_QUEUE)
    public void receiveHeaderQueue(byte[] message) {
        logger.info(" header  queue message:"+new String(message));
    }


}
