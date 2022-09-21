package com.biosphere.communitymodule.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.biosphere.communitymodule.mapper.LikeRecordMapper;
import com.biosphere.communitymodule.mapper.PostMapper;
import com.biosphere.library.pojo.*;
import com.biosphere.library.vo.CommunityPostVo;
import com.biosphere.library.vo.MainPageDataVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author Administrator
 * @Date 2022/9/2 10:52
 * @Version 1.0
 */

@Slf4j
@Component
public class CommunityDataAutoLoadUtil {

    /**
     * 功能描述: 定时任务，每半小时加载一次数据库中帖子数据到Redis
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/9/2 10:24
     */

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private LikeRecordMapper likeRecordMapper;

    @Scheduled(cron = "0 0 4 * * ?")
    public void communityMainPageData(){
        log.info("定时加载主页帖子数据任务启动");
        //每天凌晨四点更新缓存，把数据库里前1000条更新到缓存里。(没人能这么闲看完这么多帖子吧)
        List<CommunityPostVo> communityPostVo = postMapper.loadPostWithPage(0,999);
        Map<String, Object> postMap = new HashMap<>();
        Set<ZSetOperations.TypedTuple<Long>> tempSet = new HashSet<>();
        for (CommunityPostVo postVo : communityPostVo) {
            //先处理图片
            if (!Objects.isNull(postVo.getImageUrl()))
                postVo.setImageUrlList(postVo.getImageUrl().split("，"));
            //根据帖子发布时间进行排序
            ZSetOperations.TypedTuple<Long> temp = new DefaultTypedTuple<>(postVo.getPostID(),Double.valueOf(postVo.getPostDate().getTime()));
            tempSet.add(temp);
            //方便根据帖子ID查找对应的帖子信息
            postMap.put(postVo.getPostID().toString(),postVo);
        }
        //点赞缓存，用户为键、帖子为值（感觉后期可以优化，登录再加入缓存）
        // List<LikeRecord> likeRecords = likeRecordMapper.selectList(new QueryWrapper<LikeRecord>().select("userID,postID"));
        // Map<String, List<Long>> userToPost = new HashMap<>();
        // for (LikeRecord likeRecord : likeRecords) {
        //     if (!userToPost.containsKey(likeRecord.getUserID().toString())) {
        //         List<Long> list = new ArrayList<>();
        //         list.add(likeRecord.getPostID());
        //         userToPost.put(likeRecord.getUserID().toString(),list);
        //     }else {
        //         List<Long> list = userToPost.get(likeRecord.getUserID().toString());
        //         list.add(likeRecord.getPostID());
        //         userToPost.put(likeRecord.getUserID().toString(),list);
        //     }
        //
        // }

        try{
            redisTemplate.opsForZSet().add("postSet", tempSet);
            redisTemplate.opsForHash().putAll("postMap", postMap);
            // redisTemplate.opsForHash().putAll("likeRecords", userToPost);
            //评论缓存清空
            redisTemplate.delete("commentRecords");
            redisTemplate.expire("postMap",1500, TimeUnit.MINUTES);
            redisTemplate.expire("postSet",1501, TimeUnit.MINUTES);
            // redisTemplate.expire("likeRecords",1502, TimeUnit.MINUTES);
        }catch (Exception e){
            log.info("定时加载主页帖子数据任务失败");
            e.printStackTrace();
            return;
        }
        log.info("定时加载主页帖子数据任务结束");

    }





}
