package com.adda.mistry.booking.addamistrybookingservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.order.name}")
    private String orderQueue;

//    @Value("${rabbitmq.queue.email.name}")
//    private String emailQueue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.binding.order.routing.key}")
    private String orderRoutingKey;

    // spring bean for queue - order queue
    @Bean
    public Queue orderQueue(){
        return new Queue("order");
    }

    // spring bean for exchange
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange("order_exchange");
    }

    // spring bean for binding between exchange and queue using routing key
    @Bean
    public Binding binding(){
        return BindingBuilder
                .bind(orderQueue())
                .to(exchange())
                .with("order_routing_key");
    }



    // message converter
    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    // configure RabbitTemplate
    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
