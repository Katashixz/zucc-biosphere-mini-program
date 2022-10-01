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

    private static final String POST_QUEUE = "uploadPostQueue";
    private static final String LIKE_QUEUE = "uploadLikeQueue";
    private static final String COMMENT_QUEUE = "uploadCommentQueue";
    private static final String COMMUNITY_EXCHANGE = "postExchange";
    private static final String POST_KEY = "post.#";
    private static final String LIKE_KEY = "like.#";
    private static final String COMMENT_KEY = "comment.#";


    @Bean
    public Queue POST_QUEUE(){
        return new Queue(POST_QUEUE);
    }

    @Bean
    public Queue COMMENT_QUEUE(){
        return new Queue(COMMENT_QUEUE);
    }

    @Bean
    public Queue LIKE_QUEUE(){
        return new Queue(LIKE_QUEUE);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(COMMUNITY_EXCHANGE);
    }

    @Bean
    public Binding bindingPost(){
        return BindingBuilder.bind(POST_QUEUE()).to(topicExchange()).with(POST_KEY);
    }

    @Bean
    public Binding bindingLike(){
        return BindingBuilder.bind(LIKE_QUEUE()).to(topicExchange()).with(LIKE_KEY);
    }

    @Bean
    public Binding bindingComment(){
        return BindingBuilder.bind(COMMENT_QUEUE()).to(topicExchange()).with(COMMENT_KEY);
    }

}
