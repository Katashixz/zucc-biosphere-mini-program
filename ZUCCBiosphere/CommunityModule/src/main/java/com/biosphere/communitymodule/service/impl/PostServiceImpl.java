package com.biosphere.communitymodule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biosphere.communitymodule.mapper.CommentMapper;
import com.biosphere.communitymodule.mapper.PostMapper;
import com.biosphere.library.pojo.Comment;
import com.biosphere.library.pojo.Post;
import com.biosphere.communitymodule.service.IPostService;
import com.biosphere.library.vo.CommentVo;
import com.biosphere.library.vo.CommunityPostVo;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AliasFor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hyh
 * @since 2022-09-08
 */
@Service
@Slf4j
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements IPostService, InitializingBean {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CommentMapper commentMapper;

    //预计要插入多少数据
    private static int size = 1000000;
    //期望的误判率
    private static double fpp = 0.01;
    //创建布隆过滤器
    private static BloomFilter<Long> commentBloomFilter = BloomFilter.create(Funnels.longFunnel(), size, fpp);


    @Override
    public Map<String ,Object> loadPost(Integer curPage, Integer pageSize) {
        //根据页号加载帖子
        Integer left = (curPage - 1) * pageSize;
        Integer right = curPage * pageSize - 1;
        log.info("加载数据 {}-{}",left,right);
        Map<String ,Object> result = new HashMap<>();
        //帖子数据是否加载完毕
        Boolean isEnd = false;
        try{
            Set<Integer> postSet = redisTemplate.opsForZSet().reverseRange("postSet", left, right);
            Map<String ,Object> postMap = redisTemplate.opsForHash().entries("postMap");
            List<CommunityPostVo> postVos = new ArrayList<>();
            for (Integer integer : postSet) {
                postVos.add((CommunityPostVo) postMap.get(integer.toString()));
            }
            //如果Redis里的数据加载到底了，就返回已全部加载(目前只允许浏览前1000条)
            if (postSet.size() < (right - left)) {
                isEnd = true;
            }
            result.put("postList", postVos);
            result.put("isEnd", isEnd);
        }catch (Exception e){
            log.error("帖子数据分页加载出现错误：");
            e.printStackTrace();
            return null;
        }
        return result;

    }

    @Override
    public CommunityPostVo loadPostDetail(Long postID) {
        CommunityPostVo res = new CommunityPostVo();
        try{
            res = (CommunityPostVo) redisTemplate.opsForHash().get("postMap", postID.toString());
        }catch (Exception e){
            log.error("加载帖子详情失败：");
            e.printStackTrace();
            return null;
        }
        return res;
    }

    @Override
    public boolean isLiked(Long postID, Integer userID) {
        if (Objects.isNull(userID)) return false;
        try{
            // List<Long> likeRecords = (List<Long>) redisTemplate.opsForHash().get("likeRecords", userID.toString());
            List<Long> likeRecords = (List<Long>) redisTemplate.opsForValue().get("userID:" + userID + ":likeRecords");
            if (!Objects.isNull(likeRecords) && likeRecords.contains(postID))
                return true;

        }catch (Exception e){
            log.error("点赞数据加载失败：");
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public List<CommentVo> loadComments(Long postID) {
        List<CommentVo> commentVos = new ArrayList<>();
        try{
            //如果缓存有就到缓存里取出来
            commentVos = (List<CommentVo>) redisTemplate.opsForHash().get("commentRecords", postID.toString());
            //此时容易有缓存穿透问题，对空值缓存防不住大流量撑爆Redis，所以采用布隆过滤器
            //如果缓存没有，先判断布隆过滤器里有没有，有再从数据库里取，再存入缓存，没有直接返回null
            if (Objects.isNull(commentVos)) {
                if (!commentBloomFilter.mightContain(postID)) {
                    return null;
                }
                commentVos = commentMapper.loadCommentByPostID(postID);
                Map<String, Object> map = new HashMap<>();
                map.put(postID.toString(), commentVos);
                redisTemplate.opsForHash().putAll("commentRecords", map);
                redisTemplate.expire("commentRecords",1503, TimeUnit.MINUTES);
            }


        }catch (Exception e){
            log.error("加载评论错误：");
            e.printStackTrace();
            return null;
        }
        return commentVos;
    }

    @Override
    public Map<String, Object> updateLike(Integer userID, Map<String, Object> post) {
        try{
            // List<Long> likeRecords = (List<Long>) redisTemplate.opsForHash().get("likeRecords", userID.toString());
            List<Long> likeRecords = (List<Long>) redisTemplate.opsForValue().get("userID:" + userID + ":likeRecords");

            if (Objects.isNull(likeRecords)) {
                return post;
            }else {
                List<CommunityPostVo> postVos = (List<CommunityPostVo>) post.get("postList");
                for (CommunityPostVo postVo : postVos) {
                    if (likeRecords.contains(postVo.getPostID())) {
                        postVo.setPostIsLiked(true);
                    }
                }
                post.put("postList", postVos);
            }


        }catch (Exception e){
            log.error("点赞数据加载失败：");
            e.printStackTrace();
            return null;
        }
        return post;
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("布隆过滤器初始化");
        List<Long> postHasComment = commentMapper.loadPostWhichHasComments();
        for (Long aLong : postHasComment) {
            commentBloomFilter.put(aLong);
        }
        log.info("布隆过滤器初始化完成");

    }
    @Scheduled(cron = "0 0/10 * * * ?")
    public void commentBloomFilterUpdate(){
        log.info("布隆过滤器定时更新");
        List<Long> postHasComment = commentMapper.loadPostWhichHasComments();
        for (Long aLong : postHasComment) {
            commentBloomFilter.put(aLong);
        }
        log.info("布隆过滤器更新完成");

    }


    }
