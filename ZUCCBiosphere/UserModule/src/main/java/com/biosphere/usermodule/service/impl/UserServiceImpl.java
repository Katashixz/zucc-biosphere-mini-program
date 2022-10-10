package com.biosphere.usermodule.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biosphere.library.pojo.LikeRecord;
import com.biosphere.library.util.MyInfo;
import com.biosphere.usermodule.mapper.LikeRecordMapper;
import com.biosphere.usermodule.mapper.UserMapper;
import com.biosphere.library.pojo.User;
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

    @Override
    public String getOpenID(String code) {
        //获取openid
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
        //检查数据库中是否存在该用户信息，不存在则自动注册，存在就更新
        try{
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("openID",curUser.getOpenID());
            Long cnt = userMapper.selectCount(userQueryWrapper);
            if (cnt == 0) {
                //注册
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
            //四天过期
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
        //点赞缓存，用户为键、帖子为值
        List<Long> likeRecords = likeRecordMapper.loadLikeRecordsByUserID(userID);
        // Map<String, List<Long>> userToPost = new HashMap<>();
        // userToPost.put(userID.toString(),likeRecords);
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
            // redisTemplate.opsForHash().put("likeRecords", userID.toString(), likeRecords);
            // redisTemplate.expire("likeRecords",5761, TimeUnit.MINUTES);
            redisTemplate.opsForValue().set("userID:" + userID + ":likeRecords",likeRecords,5761,TimeUnit.MINUTES);
        }catch (Exception e){
            log.info("加载用户点赞信息失败");
            e.printStackTrace();
            return;
        }
    }

}
