package com.example.HaiKangDemo.mq;


import com.example.HaiKangDemo.domain.EventAccessInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @authon GMY
 * @create 2020-10-22 20:30
 */
@Component
public class RabbitMQProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    @Autowired
    private ObjectMapper objectMapper;

    public void send(EventAccessInfo eventAccessInfo) {
//        String context = "Hello " + "Rabbit MQ！";
        System.out.println("发送MQ消息 : " + eventAccessInfo);

        String msgJson = null;
        try {
            msgJson = objectMapper.writeValueAsString(eventAccessInfo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
//        Message message = MessageBuilder.withBody(msgJson.getBytes())
//                .setContentType(MessageProperties.CONTENT_TYPE_JSON).build();
        this.rabbitTemplate.convertAndSend(queue.getName(), msgJson);
    }
}
