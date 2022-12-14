package com.xiao.learn_rabbitmq.config;


import com.xiao.learn_rabbitmq.handler.ConfirmCallbackHandler;
import com.xiao.learn_rabbitmq.handler.FastJsonMessageConverter;
import com.xiao.learn_rabbitmq.handler.ReturnCallbackHandler;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;


/**
 * @author aloneMan
 * @projectName plan-message-middleware
 * @createTime 2022-08-27 11:23:59
 * @description
 */
@Configuration
public class RabbitMQConfig {


    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("127.0.0.1:5672");
        connectionFactory.setUsername("aloneman");
        connectionFactory.setPassword("aloneman");
        connectionFactory.setVirtualHost("learn_1");
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        connectionFactory.setPublisherReturns(true);
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, ConfirmCallbackHandler confirmCallbackHandler, ReturnCallbackHandler returnCallbackHandler) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        //returnCallBack?????????????????????
        template.setMandatory(true);
        //???????????????Broker??????
        template.setConfirmCallback(confirmCallbackHandler);
        //????????????????????????
        template.setReturnsCallback(returnCallbackHandler);
        return template;
    }

    /**
     * ????????????JSON???????????????????????????JSON?????????????????????
     *
     * @return
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public FastJsonMessageConverter fastJsonMessageConverter() {
        return new FastJsonMessageConverter();
    }


    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory =
                new SimpleRabbitListenerContainerFactory();
        configurer.configure(simpleRabbitListenerContainerFactory, connectionFactory);
        //?????????????????????ack
        simpleRabbitListenerContainerFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        simpleRabbitListenerContainerFactory.setReceiveTimeout(3000L);
        //?????????????????????
//        simpleRabbitListenerContainerFactory.setConcurrentConsumers(3);
        //?????????????????????
//        simpleRabbitListenerContainerFactory.setMaxConcurrentConsumers(10);
        //????????????????????????????????????????????????
        simpleRabbitListenerContainerFactory.setPrefetchCount(3);
        return simpleRabbitListenerContainerFactory;
    }


    // ?????????json????????????????????????
/*    @Bean
    public RabbitListenerConfigurer rabbitListenerConfigurer() {
        return registrar -> {
            registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
        };
    }

    @Bean
    MessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory messageHandlerMethodFactory = new DefaultMessageHandlerMethodFactory();
        messageHandlerMethodFactory.setMessageConverter(mappingJackson2MessageConverter());
        return messageHandlerMethodFactory;
    }

    @Bean
    public MappingJackson2MessageConverter mappingJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }*/
}
