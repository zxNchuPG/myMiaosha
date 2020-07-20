package com.nchu.miaosha.rabbitmq;

import com.nchu.miaosha.redis.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: MQSender
 * @Author: 时间
 * @Description: 消息发送者
 * 信息是发送到交换机,由交换机做了一次路由
 * @Date: 2020/7/18 21:52
 * @Version: 1.0
 */
@Service
public class MQSender {
    private static Logger log = LoggerFactory.getLogger(MQSender.class);
    @Autowired
    AmqpTemplate amqpTemplate;

    public void sendMiaoshaMessage(MiaoshaMessage message) {
        String msg = RedisUtil.beanToString(message);
        log.info("sendMiaoshaMessage:" + msg);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, msg);
    }

    /**
     * Direct直接交换模式
     *
     * @param message
     */
//    public void send(Object message) {
//        String msg = RedisUtil.beanToString(message);
//        log.info("send message:" + msg);
//        amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);
//    }

    /**
     * Topic主题模式
     *
     * @param message
     */
//    public void sendTopic(Object message) {
//        String msg = RedisUtil.beanToString(message);
//        log.info("send topic message:" + msg);
//        // topicQueue1 的 routing 是 topic.key1 ,可以匹配
//        // topicQueue2 的 routing 是 topic.# ,可以匹配
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key1", msg + "1");
//
//        // topicQueue2 的 routing 是 topic.# ,可以匹配
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key2", msg + "2");
//    }

//    public void sendFanout(Object message) {
//        String msg = RedisUtil.beanToString(message);
//        log.info("send fanout message:" + msg);
//        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE, "", msg);
//    }

//    public void sendHeader(Object message) {
//        String msg = RedisUtil.beanToString(message);
//        log.info("send fanout message:" + msg);
//        MessageProperties properties = new MessageProperties();
//        properties.setHeader("header1", "value1");
//        properties.setHeader("header2", "value2");
//        Message obj = new Message(msg.getBytes(), properties);
//        amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE, "", obj);
//    }
}
