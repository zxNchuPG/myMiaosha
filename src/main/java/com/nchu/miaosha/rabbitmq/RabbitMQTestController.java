package com.nchu.miaosha.rabbitmq;

import com.nchu.miaosha.common.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName: RabbitMQTestController测试类
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/19 1:06
 * @Version: 1.0
 */
@Controller
@RequestMapping(value = "/mq")
public class RabbitMQTestController {
    @Autowired
    MQSender sender;

//    @RequestMapping("/header")
//    @ResponseBody
//    public ApiResponse<String> header() {
//        sender.sendHeader("hello,zxnchu");
//        return ApiResponse.success("Hello，world");
//    }
//
//    @RequestMapping("/fanout")
//    @ResponseBody
//    public ApiResponse<String> fanout() {
//        sender.sendFanout("hello,zxnchu");
//        return ApiResponse.success("Hello，world");
//    }
//
//    @RequestMapping("/topic")
//    @ResponseBody
//    public ApiResponse<String> topic() {
//        sender.sendTopic("hello,zxnchu");
//        return ApiResponse.success("Hello，world");
//    }

    //    @RequestMapping("/direct")
//    @ResponseBody
//    public ApiResponse<String> mq() {
//        sender.send("hello,zxnchu");
//        return ApiResponse.success("Hello，world");
//    }
//    @RequestMapping("/direct")
//    @ResponseBody
//    public ApiResponse<String> mq() {
//        sender.send("hello,zxnchu");
//        return ApiResponse.success("Hello，world");
//    }
}
