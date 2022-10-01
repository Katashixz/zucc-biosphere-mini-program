package com.biosphere.communitymodule.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.biosphere.library.pojo.Post;
import com.biosphere.library.vo.CommunityPostVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author Administrator
 * @Date 2022/9/22 16:04
 * @Version 1.0
 */
@Service
@Slf4j
public class MQSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendPostMsg(String msg){
        rabbitTemplate.convertAndSend("postExchange", "post.postMessage", msg);
    }

    public void sendLikeMsg(String msg){
        rabbitTemplate.convertAndSend("postExchange", "like.likeMessage", msg);
    }

    public void sendCommentMsg(String msg){
        rabbitTemplate.convertAndSend("postExchange", "comment.commentMessage", msg);
    }


}
