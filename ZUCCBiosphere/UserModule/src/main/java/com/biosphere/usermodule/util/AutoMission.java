package com.biosphere.usermodule.util;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.biosphere.library.pojo.Comment;
import com.biosphere.usermodule.mapper.CommentMapper;
import com.biosphere.usermodule.mapper.LikeRecordMapper;
import com.biosphere.usermodule.mapper.PostMapper;
import com.biosphere.usermodule.mapper.StarRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author Administrator
 * @Date 2022/10/21 17:11
 * @Version 1.0
 */
@Slf4j
@Component
public class AutoMission {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private LikeRecordMapper likeRecordMapper;

    @Autowired
    private StarRecordMapper starRecordMapper;

    @Scheduled(cron = "0 0 5 * * ?")
    // @Scheduled(cron = "25 * * * * ? ")
    public void deletePost(){
        log.info("定时删除标记帖任务启动");
        Integer postNum = postMapper.deleteMarkedPost();
        Integer commentNum = 0;
        Integer likeNum = 0;
        Integer starNum = 0;
        // int a = 1/0;
        //删除与之关联的评论、点赞和收藏
        List<Long> postToBeDeleted = new ArrayList<>();
        Long size = redisTemplate.opsForList().size("deleteQueue");
        while (size > 0) {
            postToBeDeleted.add(((Integer) redisTemplate.opsForList().rightPop("deleteQueue")).longValue());
            size = redisTemplate.opsForList().size("deleteQueue");
        }
        if (postToBeDeleted.size() > 0){
             commentNum = commentMapper.deleteComments(postToBeDeleted);
             likeNum = likeRecordMapper.deleteLikes(postToBeDeleted);
             starNum = starRecordMapper.deleteStars(postToBeDeleted);

        }

        log.info("定时删除标记帖任务结束，删除了{}个帖子，{}条评论，{}条收藏，{}条点赞", postNum, commentNum, starNum, likeNum);

    }




}
