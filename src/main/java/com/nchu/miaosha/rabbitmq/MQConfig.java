package com.nchu.miaosha.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: MQConfig
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/18 22:14
 * @Version: 1.0
 */
@Configuration
public class MQConfig {

    public static final String MIAOSHA_QUEUE = "miaosha.queue";
    public static final String QUEUE = "queue";
    public static final String TOPIC_QUEUE1 = "topic.queue1";
    public static final String TOPIC_QUEUE2 = "topic.queue2";
    public static final String HEADER_QUEUE = "header.queue";
    public static final String TOPIC_EXCHANGE = "topicExchage";
    public static final String FANOUT_EXCHANGE = "fanoutxchage"; // Fanout广播模式
    public static final String HEADERS_EXCHANGE = "headersExchage";

    /**
     * Direct直接交换模式 交换机Exchange
     * 生产者和消费者，具有相同的交换机名称（Exchange）、交换机类型和相同的密匙（routingKey），那么消费者即可成功获取到消息
     */
//    @Bean
//    public Queue queue() {
//        return new Queue(QUEUE, true);
//    }
    @Bean
    public Queue queue() {
        return new Queue(MIAOSHA_QUEUE, true);
    }

    /**
     * Topic主题模式 交换机Exchange
     */
    @Bean
    public Queue topicQueue1() {
        return new Queue(TOPIC_QUEUE1, true);
    }

    @Bean
    public Queue topicQueue2() {
        return new Queue(TOPIC_QUEUE2, true);
    }

    /**
     * 消息先放到Exchage中,再把消息放到queue中
     *
     * @return
     */
    @Bean
    public TopicExchange topicExchage() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    /**
     * exchange需要绑定
     * topic,key1的时候绑定到topicQueye1
     *
     * @return
     */
    @Bean
    public Binding topicBinding1() {
        return BindingBuilder.bind(topicQueue1()).to(topicExchage()).with("topic.key1");
    }

    /**
     * exchange需要绑定
     * '#' 匹配一个或多个词，比如 goods.add，goods.add.sub都能匹配到。
     * '*' 匹配一个，比如goods.delete能匹配到，goods.delete.haha就匹配不到
     * <p>
     * topic.#的时候绑定到topicQueue2
     *
     * @return
     */
    @Bean
    public Binding topicBinding2() {
        return BindingBuilder.bind(topicQueue2()).to(topicExchage()).with("topic.#");
    }

    /**
     * Fanout广播模式 交换机Exchange
     */
    @Bean
    public FanoutExchange fanoutExchage() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    /**
     * 广播绑定不需要routingkey
     *
     * @return
     */
    @Bean
    public Binding FanoutBinding1() {
        return BindingBuilder.bind(topicQueue1()).to(fanoutExchage());
    }

    /**
     * 广播绑定不需要routingkey
     *
     * @return
     */
    @Bean
    public Binding FanoutBinding2() {
        return BindingBuilder.bind(topicQueue2()).to(fanoutExchage());
    }

    /**
     * Headers模式 交换机Exchanges
     * 根据消息的headers来匹配对应的队列，在消息接收回调中指定headers， 可以是Map<String, Object>、String可变数组类型的keys等
     */
    @Bean
    public HeadersExchange headersExchage() {
        return new HeadersExchange(HEADERS_EXCHANGE);
    }

    @Bean
    public Queue headerQueue1() {
        return new Queue(HEADER_QUEUE, true);
    }

    @Bean
    public Binding headerBinding() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("header1", "value1");
        map.put("header2", "value2");
//        header 中必须满足map的key-value才往里面放消息
        return BindingBuilder.bind(headerQueue1()).to(headersExchage()).whereAll(map).match();
    }

}
