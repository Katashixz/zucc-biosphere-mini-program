package com.biosphere.communitymodule.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.biosphere.communitymodule.mapper.CommentMapper;
import com.biosphere.communitymodule.mapper.LikeRecordMapper;
import com.biosphere.communitymodule.mapper.PostMapper;
import com.biosphere.library.pojo.Comment;
import com.biosphere.library.pojo.LikeRecord;
import com.biosphere.library.pojo.Post;
import com.biosphere.library.vo.CommentVo;
import com.biosphere.library.vo.CommunityPostVo;
import com.biosphere.library.vo.LikeStatusVo;
import com.biosphere.library.vo.UploadCommentVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

    @Autowired
    private LikeRecordMapper likeRecordMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Transactional
    @RabbitListener(queues = "uploadPostQueue")
    public void postMsgReceiver(String message) throws Exception{
        Post post = JSON.parseObject(message,Post.class);
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

    @Transactional
    @RabbitListener(queues = "uploadLikeQueue")
    public void likeMsgReceiver(String message) throws Exception{
        LikeStatusVo likeStatusVo = JSON.parseObject(message, LikeStatusVo.class);
        // 解决以下场景-虽然节流了，但是仍可能会有对一个帖子两次点赞的情况(高峰期)，就得先查询再新增
        LikeRecord likeRecord = new LikeRecord();
        likeRecord.setPostID(likeStatusVo.getPostID());
        likeRecord.setUserID(likeStatusVo.getUserID());
        QueryWrapper<LikeRecord> likeRecordQueryWrapper = new QueryWrapper<>();
        likeRecordQueryWrapper.select("id").eq("userID", likeStatusVo.getUserID()).eq("postID",likeStatusVo.getPostID());
        Long count = likeRecordMapper.selectCount(likeRecordQueryWrapper);
        if (count == 0L && likeStatusVo.getStatus()) {
            // 如果数据库没数据并且状态为新增点赞时，才进行新增操作
            likeRecord.setDate(new Date(System.currentTimeMillis()));
            likeRecord.setIschecked(0);
            // 新增到数据库
            likeRecordMapper.insert(likeRecord);
            // 缓存更新
            List<Long> likeRecList = (List<Long>) redisTemplate.opsForValue().get("userID:" + likeRecord.getUserID() + ":likeRecords");
            likeRecList.add(likeRecord.getPostID());
            redisTemplate.opsForValue().set("userID:" + likeRecord.getUserID() + ":likeRecords", likeRecList,5761, TimeUnit.MINUTES);

        }
        else if (count > 0L && likeStatusVo.getStatus()){
            // 如果数据库有数据并且状态为新增点赞，默认忽略此请求
            return;
        }
        if (!likeStatusVo.getStatus()) {
            // 如果是取消点赞操作
            Map<String, Object> map = new HashMap<>();
            map.put("userID", likeStatusVo.getUserID());
            map.put("postID", likeStatusVo.getPostID());
            likeRecordMapper.deleteByMap(map);
            List<Long> likeRecList = (List<Long>) redisTemplate.opsForValue().get("userID:" + likeRecord.getUserID() + ":likeRecords");
            likeRecList.remove(likeRecord.getPostID());
            redisTemplate.opsForValue().set("userID:" + likeRecord.getUserID() + ":likeRecords", likeRecList,5761, TimeUnit.MINUTES);

        }
        //更新帖子点赞数
        Long cnt = likeRecordMapper.selectCount(new QueryWrapper<LikeRecord>().eq("postID", likeStatusVo.getPostID()));
        CommunityPostVo post = (CommunityPostVo) redisTemplate.opsForHash().get("postMap", likeStatusVo.getPostID().toString());
        post.setLikeNum(cnt);
        redisTemplate.opsForHash().put("postMap",likeStatusVo.getPostID().toString(),post);

    }

    @Transactional
    @RabbitListener(queues = "uploadCommentQueue")
    public void commentMsgReceiver(String message) throws Exception{
        long a = System.currentTimeMillis();
        UploadCommentVo req = JSON.parseObject(message, UploadCommentVo.class);
        // 先上传数据库，检测缓存里是否有这个帖子的评论缓存（一般能评论都是已经有缓存），如果有就更新缓存，没有就不作操作。最后更新评论数
        Comment comment = new Comment();
        comment.setCommentDate(new Date(System.currentTimeMillis()));
        comment.setCommentToUser(req.getToUserID());
        comment.setIschecked(0);
        comment.setPostID(req.getPostID());
        comment.setUserID(req.getUserID());
        comment.setContent(req.getContent());
        commentMapper.insert(comment);
        //缓存检测
        Boolean hasRec = redisTemplate.opsForHash().hasKey("commentRecords", comment.getPostID().toString());
        if (hasRec) {
            //缓存有就更新缓存
            List<CommentVo> commentList = commentMapper.loadCommentByPostID(comment.getPostID());
            redisTemplate.opsForHash().put("commentRecords",comment.getPostID().toString(),commentList);
        }
        //评论数更新
        Long cnt = commentMapper.selectCount(new QueryWrapper<Comment>().eq("postID", comment.getPostID()));
        CommunityPostVo post = (CommunityPostVo) redisTemplate.opsForHash().get("postMap", comment.getPostID().toString());
        post.setCommentNum(cnt);
        redisTemplate.opsForHash().put("postMap",comment.getPostID().toString(),post);
        long b = System.currentTimeMillis();
        System.out.println(b-a);



    }
}
