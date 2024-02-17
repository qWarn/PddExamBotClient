package ru.qwarn.pddexambotclient.bot.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class RabbitmqConfig {

    private final RabbitmqProperties rabbitmqProperties;

    @Bean
    public DirectExchange exchange(){
        return new DirectExchange(rabbitmqProperties.getExchange());
    }

    @Bean
    public Queue queue(){
        return new Queue(rabbitmqProperties.getQueue());
    }

    @Bean
    public Binding binding(DirectExchange exchange, Queue queue){
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(rabbitmqProperties.getRoutingKey());
    }

    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }


}
