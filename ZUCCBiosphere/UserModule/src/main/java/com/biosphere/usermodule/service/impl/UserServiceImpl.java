package com.biosphere.usermodule.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biosphere.library.pojo.*;
import com.biosphere.library.util.*;
import com.biosphere.library.vo.*;
import com.biosphere.usermodule.mapper.*;
import com.biosphere.usermodule.service.IUserService;
import com.biosphere.usermodule.util.RandomNameGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hyh
 * @since 2022-08-10
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LikeRecordMapper likeRecordMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StarRecordMapper starRecordMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private IUserService userService;

    @Override
    public String getOpenID(String code) {
        // 获取openid
        String params = "appid="+MyInfo.wxspAppID+"&secret="+MyInfo.wxspSecret+"&js_code="+code+"&grant_type="+MyInfo.grantType;
        String str = HttpMethodUtil.sendGet("https://api.weixin.qq.com/sns/jscode2session",params);
        JSONObject json = JSONObject.parseObject(str);
        if (json.containsKey("errcode")) {
            throw new ExceptionLogVo(RespBeanEnum.CODE_GET_ERROR);
        }
        String openID = MD5Util.md5(json.get("openid").toString());
        return openID;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    // public User checkUserInfo(String openID, String url, String nickName){
    public User checkUserInfo(String openID){
        User curUser = new User();
        // curUser.setUserName(nickName);
        // curUser.setAvatarUrl(url);
        curUser.setOpenID(openID);
        // 检查数据库中是否存在该用户信息，不存在则自动注册，存在就更新
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("openID",curUser.getOpenID());
        Long cnt = userMapper.selectCount(userQueryWrapper);
        if (cnt == 0) {
            // 注册
            //由于小程序接口政策改变，头像和名称在注册时随机生成
            curUser.setAvatarUrl(RandomNameGenerator.getAvatar());
            curUser.setUserName(RandomNameGenerator.getWholeName(1).get(0));
            curUser.setEnergyPoint(0);
            curUser.setLatestLoginTime(new Date(System.currentTimeMillis()));
            userMapper.insert(curUser);
            log.info("[用户:{}{} 注册成功]",curUser.getOpenID(),curUser.getUserName());
        }else{
            //更新最后登录时间与人品值
            curUser = userMapper.selectOne(userQueryWrapper);
            curUser.setLatestLoginTime(new Date(System.currentTimeMillis()));
            userMapper.updateById(curUser);
            log.info("[用户:{}{} 更新信息成功]",curUser.getOpenID(),curUser.getUserName());
            }
            return curUser;
    }

    @Override
    public String tokenGenerate(User curUser) {

        try {
            String token = JwtUtil.createJWT(curUser.getOpenID());
            // 四天过期
            redisTemplate.opsForValue().set("login:" + curUser.getOpenID(), curUser, 5760, TimeUnit.MINUTES);
            return token;
        }catch (Exception e){
            log.info("[生成token失败{}]",e);
            return null;
        }
    }

    @Override
    public User getUserInfoByOpenId(String openID) {
        User user = new User();
        try {
            // user = userMapper.selectById(openID);
            user = (User) redisTemplate.opsForValue().get("login:" + openID);
        } catch (Exception e) {
            log.info("[openID:{} 查询用户信息失败,]",openID);
            log.info("[错误信息:{}]",e.getMessage());
        }
        return user;
    }

    @Override
    public void saveUserLikeRecords(Integer userID) {
        // 点赞缓存，用户为键、帖子为值
        try{
            List<Long> likeRecords = likeRecordMapper.loadLikeRecordsByUserID(userID);
            redisTemplate.opsForHash().put("userID:" + userID, "likeRecords", likeRecords);
        }catch (Exception e){
            log.info("加载用户点赞信息失败：",e);
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void saveUserStarRecords(Integer userID) {
        // 数据库取出该用户的收藏，存入Redis
        try{
            List<StarPostVo> starRecords = starRecordMapper.getStaredPost(userID);
            for (StarPostVo starPostVo : starRecords) {
                if (!Objects.isNull(starPostVo.getImageUrl()))
                    starPostVo.setImageUrlList(starPostVo.getImageUrl().split("，"));

            }
            redisTemplate.opsForHash().put("userID:" + userID, "starRecords",starRecords);
            redisTemplate.expire("userID:" + userID,5761,TimeUnit.MINUTES);
        }catch (Exception e){
            log.info("加载用户收藏信息失败：",e);
            e.printStackTrace();
            return;
        }

    }

    @Override
    public void saveUserPostRecords(Integer userID) {
        // 数据库取出该用户的发帖数据，存入Redis
        try{
            List<SimplePostVo> myPostList = postMapper.loadPostByUserID(userID);
            for (SimplePostVo simplePostVo : myPostList) {
                if (!Objects.isNull(simplePostVo.getImageUrl()))
                    simplePostVo.setImageUrlList(simplePostVo.getImageUrl().split("，"));

            }
            redisTemplate.opsForHash().put("userID:" + userID, "myPost", myPostList);
        }catch (Exception e){
            log.info("加载用户发帖信息失败：",e);
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void saveUserCommentRecords(Integer userID) {
        // 数据库取出该用户的评论数据，存入Redis
        try{
            List<CommentVo> commentList = commentMapper.loadCommentByUserID(userID);
            for (CommentVo commentVo : commentList) {
                if (!Objects.isNull(commentVo.getImage()))
                    commentVo.setImage(commentVo.getImage().split("，")[0]);
            }
            redisTemplate.opsForHash().put("userID:" + userID, "myComment", commentList);
        }catch (Exception e){
            log.info("加载用户评论信息失败：",e);
            e.printStackTrace();
            return;
        }
    }

    @Override
    public List<SimplePostVo> loadMyPost(Integer userID) {
        List<SimplePostVo> myPost = (List<SimplePostVo>) redisTemplate.opsForHash().get("userID:" + userID, "myPost");

        return myPost;
    }

    @Override
    public List<StarPostVo> loadMyStar(Integer userID) {
        List<StarPostVo> myStar = (List<StarPostVo>) redisTemplate.opsForHash().get("userID:" + userID, "starRecords");
        return myStar;
    }

    @Override
    public List<CommentVo> loadMyComment(Integer userID) {
        List<CommentVo> myComment = (List<CommentVo>) redisTemplate.opsForHash().get("userID:" + userID, "myComment");
        return myComment;
    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deletePost(Long postID, Integer userID){
        // 删除帖子缓存数据【缓存可能没数据】
        Long mapIsDeleted = redisTemplate.opsForHash().delete("postMap", postID.toString());
        Long setIsDeleted = redisTemplate.opsForZSet().remove("postSet", postID);
        // 标记数据库帖子删除标志为1【数据库必有】，凌晨定时删除所有标记为1的数据，评论缓存会自己失效不必更新
        UpdateWrapper<Post> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", postID).set("isDeleted",1);
        int dbIsDeleted = postMapper.update(wrapper.getEntity(), wrapper);
        // 更新我的帖子缓存
        List<SimplePostVo> simplePostVos = postMapper.loadPostByUserID(userID);
        for (SimplePostVo simplePostVo : simplePostVos) {
            if (!Objects.isNull(simplePostVo.getImageUrl()))
                simplePostVo.setImageUrlList(simplePostVo.getImageUrl().split("，"));
        }
        redisTemplate.opsForHash().put("userID:" + userID,"myPost", simplePostVos);


        // 将待删除的帖子id加入队列
        redisTemplate.opsForList().leftPush("deleteQueue", postID);
        if (dbIsDeleted == 0) {
            throw new ExceptionLogVo(RespBeanEnum.DELETE_ERROR);

        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteComment(Long postID, Integer id, Integer userID) {
        // 数据库标记为1，凌晨统一删除
        UpdateWrapper<Comment> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id).set("isDeleted",1);
        int dbIsDeleted = commentMapper.update(updateWrapper.getEntity(), updateWrapper);
        // 更新我的评论缓存
        userService.saveUserCommentRecords(userID);
        if (dbIsDeleted == 0){
            throw new ExceptionLogVo(RespBeanEnum.DELETE_ERROR);
        }
        // 帖子评论缓存更新（如果有）
        if (redisTemplate.opsForHash().hasKey("commentRecords", postID.toString())) {
            // 如果删除这条评论后这个帖子没评论了，就删除缓存，有才更新
            List<CommentVo> commentVos = commentMapper.loadCommentByPostID(postID);
            if (commentVos.size() > 0) {
                Map<String, Object> map = new HashMap<>();
                map.put(postID.toString(), commentVos);
                redisTemplate.opsForHash().putAll("commentRecords", map);
                redisTemplate.expire("commentRecords",1503, TimeUnit.MINUTES);
            }else {
                redisTemplate.opsForHash().delete("commentRecords", postID.toString());
            }
        }
        // 更新帖子中的评论数
        Long cnt = commentMapper.selectCount(new QueryWrapper<Comment>().eq("postID", postID).eq("isDeleted",0));
        CommunityPostVo post = (CommunityPostVo) redisTemplate.opsForHash().get("postMap", postID.toString());
        if (!Objects.isNull(post)) {
            post.setCommentNum(cnt);
            redisTemplate.opsForHash().put("postMap",postID.toString(),post);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateInfo(Integer id, String nickName, String avatar, String openID) {
        // 数据库更新
        Integer integer = userMapper.updateInfo(id, nickName, avatar);
        if (integer != 1) {
            throw new ExceptionLogVo(RespBeanEnum.USER_ID_ERROR);
        }
        User user = userMapper.selectById(openID);
        // 缓存更新
        // 四天过期
        redisTemplate.opsForValue().set("login:" + openID, user, 5760, TimeUnit.MINUTES);

    }

    @Override
    public List<MyAdoptVo> loadMyAdopt(Integer userID) {
        List<MyAdoptVo> myAdopt = userMapper.getMyAdopt(userID);
        for (MyAdoptVo myAdoptVo : myAdopt) {
            myAdoptVo.setImage(myAdoptVo.getImage().split("，")[0]);
        }
        return myAdopt;

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteStar(Long postID, Integer userID) {
        //删除收藏操作并不复杂，删除数据库数据+更新缓存
        Integer cnt = starRecordMapper.deleteStar(postID, userID);
        if (cnt == 0) {
            throw new ExceptionLogVo(RespBeanEnum.DELETE_ERROR);
        }
        List<StarPostVo> starRecords = starRecordMapper.getStaredPost(userID);
        for (StarPostVo starPostVo : starRecords) {
            if (!Objects.isNull(starPostVo.getImageUrl()))
                starPostVo.setImageUrlList(starPostVo.getImageUrl().split("，"));
        }
        redisTemplate.opsForHash().put("userID:" + userID, "starRecords",starRecords);
    }




}
