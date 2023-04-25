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
import com.biosphere.library.vo.*;
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

    @Transactional(rollbackFor = Exception.class)
    @RabbitListener(queues = "uploadPostQueue")
    public void postMsgReceiver(String message) {
        Post post = JSON.parseObject(message,Post.class);
        //更新到数据库
        postMapper.insert(post);
        CommunityPostVo postVo = postMapper.findOne(post.getId());
        if (!Objects.isNull(postVo.getImageUrl()))
            postVo.setImageUrlList(postVo.getImageUrl().split("，"));

        //更新到个人缓存
        List<SimplePostVo> myPost = (List<SimplePostVo>) redisTemplate.opsForHash().get("userID:" + postVo.getUserID(),"myPost");
        SimplePostVo simplePostVo = new SimplePostVo(postVo);
        myPost.add(0,simplePostVo);
        redisTemplate.opsForHash().put("userID:" + postVo.getUserID(),"myPost",myPost);

        //更新到全局缓存
        redisTemplate.opsForHash().put("postMap",postVo.getPostID().toString(),postVo);
        redisTemplate.opsForZSet().add("postSet",postVo.getPostID(),Double.valueOf(postVo.getPostDate().getTime()));
        redisTemplate.expire("postMap",1500, TimeUnit.MINUTES);
        redisTemplate.expire("postSet",1501, TimeUnit.MINUTES);
    }

    @Transactional(rollbackFor = Exception.class)
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

            try{
                // 新增到数据库
                likeRecordMapper.insert(likeRecord);
                // 缓存更新
                List<Long> likeRecList = (List<Long>) redisTemplate.opsForHash().get("userID:" + likeRecord.getUserID(), "likeRecords");
                likeRecList.add(likeRecord.getPostID());
                redisTemplate.opsForHash().put("userID:" + likeRecord.getUserID(),"likeRecords", likeRecList);

            }catch (Exception e){
                throw new Exception("点赞数据更新错误");
            }

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
            try{

                likeRecordMapper.deleteByMap(map);
                List<Long> likeRecList = (List<Long>) redisTemplate.opsForHash().get("userID:" + likeRecord.getUserID(), "likeRecords");
                likeRecList.remove(likeRecord.getPostID());
                redisTemplate.opsForHash().put("userID:" + likeRecord.getUserID(),"likeRecords", likeRecList);
            }catch (Exception e){
                throw new Exception("删除错误");
            }

        }
        //更新帖子点赞数
        CommunityPostVo post = (CommunityPostVo) redisTemplate.opsForHash().get("postMap", likeStatusVo.getPostID().toString());
        if (post != null) {
            Long cnt = likeRecordMapper.selectCount(new QueryWrapper<LikeRecord>().eq("postID", likeStatusVo.getPostID()));
            post.setLikeNum(cnt);
            redisTemplate.opsForHash().put("postMap",likeStatusVo.getPostID().toString(),post);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @RabbitListener(queues = "uploadCommentQueue")
    public void commentMsgReceiver(String message) throws Exception{
        UploadCommentVo req = JSON.parseObject(message, UploadCommentVo.class);
        // 先上传数据库，检测缓存里是否有这个帖子的评论缓存（一般能评论都是已经有缓存），如果有就更新缓存，没有就不作操作。最后更新评论数
        Comment comment = new Comment();
        comment.setCommentDate(new Date(System.currentTimeMillis()));
        comment.setCommentToUser(req.getToUserID());
        comment.setIsChecked(0);
        comment.setIsDeleted(0);
        comment.setPostID(req.getPostID());
        comment.setUserID(req.getUserID());
        comment.setContent(req.getContent());
        commentMapper.insert(comment);
        //缓存检测
        // Boolean hasRec = redisTemplate.opsForHash().hasKey("commentRecords", comment.getPostID().toString());
        // if (hasRec) {
        //更新缓存
        List<CommentVo> commentList = commentMapper.loadCommentByPostID(comment.getPostID());
        redisTemplate.opsForHash().put("commentRecords", comment.getPostID().toString(), commentList);
        // }
        //评论数更新
        CommunityPostVo post = (CommunityPostVo) redisTemplate.opsForHash().get("postMap", comment.getPostID().toString());
        if (post != null) {
            Long cnt = commentMapper.selectCount(new QueryWrapper<Comment>().eq("postID", comment.getPostID()).eq("isDeleted",0));
            post.setCommentNum(cnt);
            redisTemplate.opsForHash().put("postMap",comment.getPostID().toString(),post);
        }
        //更新到个人缓存
        List<CommentVo> commentVos = commentMapper.loadCommentByUserID(req.getUserID());
        for (CommentVo commentVo : commentList) {
            if (!Objects.isNull(commentVo.getImage()))
                commentVo.setImage(commentVo.getImage().split("，")[0]);
        }
        redisTemplate.opsForHash().put("userID:" + req.getUserID().toString(),"myComment", commentVos);


    }
}
