package com.example.HaiKangDemo.mq.config;

import com.example.HaiKangDemo.mq.RabbitMQProducer;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @authon GMY
 * @create 2020-10-23 17:01
 */
@Configuration
public class DirectConfig {

    //队列名称
    private String Direct_HC = "haikang";

    //交换机名称
    private String EXCHANGE_NAME = "exchange.direct";

    //1.定义队列
    @Bean
    public Queue directDaHuaQueue() {
        return new Queue(Direct_HC);
    }

    //2.定义交换机
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean(name = "RabbitMQProducer")
    public RabbitMQProducer send(){
        return new RabbitMQProducer();
    }
}

