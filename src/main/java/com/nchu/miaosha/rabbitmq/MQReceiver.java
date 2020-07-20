package com.nchu.miaosha.rabbitmq;

import com.nchu.miaosha.bd.domain.User;
import com.nchu.miaosha.materials.service.GoodsService;
import com.nchu.miaosha.materials.vo.GoodsVo;
import com.nchu.miaosha.miaosha.domain.MiaoshaOrderBill;
import com.nchu.miaosha.miaosha.service.MiaoshaOrderService;
import com.nchu.miaosha.miaosha.service.MiaoshaService;
import com.nchu.miaosha.redis.service.RedisService;
import com.nchu.miaosha.redis.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: MQReceiver
 * @Author: 时间
 * @Description: 消息接收者
 * @Date: 2020/7/18 21:54
 * @Version: 1.0
 */
@Service
public class MQReceiver {
    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    MiaoshaOrderService miaoshaOrderService;

    @Autowired
    MiaoshaService miaoshaService;

    /**
     * 监听queues = MQConfig.MIAOSHA_QUEUE
     *
     * @param message
     */
    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receive(String message) {
        log.info("receive message:" + message);
        MiaoshaMessage miaoshaMessage = RedisUtil.stringToBean(message, MiaoshaMessage.class);

        // 拿到消息传过来的数据对象
        User user = miaoshaMessage.getUser();
        long goodsId = miaoshaMessage.getGoodsId();

        // 走数据库查询库存,前面已经抵挡大部分请求
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return;
        }

        //判断是否已经秒杀到了(这步可以省略,Controller已经判断过,并且加了唯一索引)
        MiaoshaOrderBill miaoshaOrderBill = miaoshaOrderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (miaoshaOrderBill != null) {
            return;
        }

        //减库存 下订单 写入秒杀订单
        miaoshaService.miaosha(user, goods);
    }

//    @RabbitListener(queues = MQConfig.QUEUE)
//    public void receive(String message) {
//        log.info("receive message:" + message);
//    }

//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
//    public void receiveTopic1(String message) {
//        log.info(" topic  queue1 message:" + message);
//    }

//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
//    public void receiveTopic2(String message) {
//        log.info(" topic  queue2 message:" + message);
//    }

//    @RabbitListener(queues = MQConfig.HEADER_QUEUE)
//    public void receiveHeaderQueue(byte[] message) {
//        log.info(" header  queue message:" + new String(message));
//    }
}
