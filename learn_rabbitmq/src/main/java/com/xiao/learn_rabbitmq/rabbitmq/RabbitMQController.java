package com.xiao.learn_rabbitmq.rabbitmq;

import com.xiao.learn_rabbitmq.service.MessageSendAndGet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author aloneMan
 * @projectName plan-message-middleware
 * @createTime 2022-08-27 11:17:47
 * @description
 */

@RestController
@RequestMapping("/rabbitmq")
@Api(tags = "rabbitmq生产者和消费者")
public class RabbitMQController {

    private MessageSendAndGet messageSendAndGet;


    public RabbitMQController(MessageSendAndGet messageSendAndGet) {
        this.messageSendAndGet = messageSendAndGet;
    }

    @PostMapping("/commodity")
    @ApiOperation("商品消息")
    public void commodity(String message) {
        messageSendAndGet.commodity(message);
    }

    @PostMapping("/secKill")
    @ApiOperation("秒杀消息")
    public void secKill(String message) {
        messageSendAndGet.secKill(message);
    }
}