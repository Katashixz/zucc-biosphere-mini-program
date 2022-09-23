package com.biosphere.communitymodule.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.biosphere.communitymodule.mapper.PostMapper;
import com.biosphere.library.pojo.Post;
import com.biosphere.library.vo.CommunityPostVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author Administrator
 * @Date 2022/9/22 16:31
 * @Version 1.0
 */

@Service
@Slf4j
public class MQReceiver {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PostMapper postMapper;

    @RabbitListener(queues = "uploadPostQueue")
    public void postMsgReceiver(String message){
        Post post = JSON.parseObject(message,Post.class);
        Map<String ,Object> postMap = redisTemplate.opsForHash().entries("postMap");

        //更新到数据库
        postMapper.insert(post);
        CommunityPostVo postVo = postMapper.findOne(post.getId());
        if (!Objects.isNull(postVo.getImageUrl()))
            postVo.setImageUrlList(postVo.getImageUrl().split("，"));
        //更新到缓存
        // postMap.put(post.getId().toString(), post);
        redisTemplate.opsForHash().put("postMap",postVo.getPostID().toString(),postVo);
        redisTemplate.opsForZSet().add("postSet",postVo.getPostID(),Double.valueOf(postVo.getPostDate().getTime()));
        redisTemplate.expire("postMap",1500, TimeUnit.MINUTES);
        redisTemplate.expire("postSet",1501, TimeUnit.MINUTES);
    }
}
