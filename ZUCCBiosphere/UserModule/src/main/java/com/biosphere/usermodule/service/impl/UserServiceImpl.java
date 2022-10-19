package com.biosphere.usermodule.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biosphere.library.pojo.*;
import com.biosphere.library.util.MyInfo;
import com.biosphere.library.vo.CommentVo;
import com.biosphere.library.vo.CommunityPostVo;
import com.biosphere.library.vo.SimplePostVo;
import com.biosphere.usermodule.mapper.*;
import com.biosphere.usermodule.service.IUserService;
import com.biosphere.library.util.HttpMethodUtil;
import com.biosphere.library.util.JwtUtil;
import com.biosphere.library.util.MD5Util;
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

    @Override
    public String getOpenID(String code) {
        // 获取openid
        String params = "appid="+MyInfo.wxspAppID+"&secret="+MyInfo.wxspSecret+"&js_code="+code+"&grant_type="+MyInfo.grantType;
        String str = HttpMethodUtil.sendGet("https://api.weixin.qq.com/sns/jscode2session",params);
        JSONObject json = JSONObject.parseObject(str);
        String openID=  MD5Util.md5(json.get("openid").toString());
        return openID;
    }

    @Override
    @Transactional
    public User checkUserInfo(String openID, String url, String nickName) {
        User curUser = new User();
        curUser.setUserName(nickName);
        curUser.setAvatarUrl(url);
        curUser.setOpenID(openID);
        // 检查数据库中是否存在该用户信息，不存在则自动注册，存在就更新
        try{
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("openID",curUser.getOpenID());
            Long cnt = userMapper.selectCount(userQueryWrapper);
            if (cnt == 0) {
                // 注册
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
        }catch (Exception e){
            log.error("[更新数据库信息失败:{}]" + e.getMessage());
            return null;
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
            // redisTemplate.opsForHash().put("likeRecords", userID.toString(), likeRecords);
            // redisTemplate.expire("likeRecords",5761, TimeUnit.MINUTES);
            // redisTemplate.opsForValue().set("userID:" + userID + ":likeRecords",likeRecords,5761,TimeUnit.MINUTES);
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
            List<StarRecord> starRecords = starRecordMapper.selectList(new QueryWrapper<StarRecord>().eq("userID",userID));
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

}
