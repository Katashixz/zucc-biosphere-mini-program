package com.biosphere.communitymodule.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author  
 * @description: TODO
 * @date 2022/3/7 21:59
 */
@Configuration
public class RabbitMQConfig {

    private static final String QUEUE = "uploadPostQueue";
    private static final String EXCHANGE = "postExchange";
    private static final String key = "post.#";


    @Bean
    public Queue queue(){
        return new Queue(QUEUE);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(topicExchange()).with(key);
    }

}
