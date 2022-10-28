package com.biosphere.usermodule.controller;


import com.alibaba.fastjson.JSONObject;
import com.biosphere.library.vo.*;
import com.biosphere.usermodule.service.IUserService;
import com.biosphere.library.pojo.User;
import com.biosphere.usermodule.service.IEnergyRecordService;
import com.biosphere.library.util.FormatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hyh
 * @since 2022-08-10
 */
@Api(tags = "用户模块")
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IEnergyRecordService energyrecordService;

    @ApiOperation(value = "用户登录", notes = "wx.login上传加密字符串即可")
    @RequestMapping(value = "/exposure/login",method = RequestMethod.POST)
    public ResponseResult userLogin(@RequestBody LoginVo loginVo){
        // Map<String,Object> resData = new HashMap<>();
        JSONObject resData = new JSONObject();
        ResponseResult res = new ResponseResult();
        if (loginVo.getCode() == null) {
            res.setCode(ResponseResult.error(RespBeanEnum.EMPTY_CODE).getCode());
            res.setMsg(ResponseResult.error(RespBeanEnum.EMPTY_CODE).getMsg());
            return res;
        }
        // 从微信官方接口获取用户数据
        String openID = userService.getOpenID(loginVo.getCode());
        if (Objects.isNull(openID)) {
            res.setCode(ResponseResult.error(RespBeanEnum.CODE_GET_ERROR).getCode());
            res.setMsg(ResponseResult.error(RespBeanEnum.CODE_GET_ERROR).getMsg());
            return res;
        }
        // 要根据openID生成JWT和token存入Redis和数据库
        User curUser = userService.checkUserInfo(openID, loginVo.getAvatarUrl(),loginVo.getNickName());
        resData.put("userInfo",curUser);
        resData.put("LatestLoginDate", FormatUtil.DateFormat(curUser.getLatestLoginTime()));
        // 生成token
        String token = userService.tokenGenerate(curUser);
        // 个人发帖存入缓存
        userService.saveUserPostRecords(curUser.getId());
        // 个人评论存入缓存
        userService.saveUserCommentRecords(curUser.getId());
        // 点赞数据存入缓存
        userService.saveUserLikeRecords(curUser.getId());
        // 收藏数据存入缓存
        userService.saveUserStarRecords(curUser.getId());
        resData.put("token", token);
        resData.put("level", curUser.getEnergyPoint() > 0 ? curUser.getEnergyPoint()/100 : 0);
        res.setData(resData);
        res.setMsg(ResponseResult.success(RespBeanEnum.SUCCESS).getMsg());
        res.setCode(ResponseResult.success(RespBeanEnum.SUCCESS).getCode());

        return res;
    }

    @ApiOperation(value = "每日签到", notes = "传入openID")
    @RequestMapping(value = "/auth/checkIn",method = RequestMethod.POST)
    public ResponseResult checkIn(@RequestBody JSONObject req){
        String openID = req.getString("openID");
        ResponseResult res = new ResponseResult();
        // User curUser = userService.getUserInfoByOpenId(openID);
        User curUser = userService.getUserInfoByOpenId(openID);
        if (Objects.isNull(curUser)) {
            res.setCode(RespBeanEnum.GET_USERINFO_ERROR.getCode());
            res.setMsg(RespBeanEnum.GET_USERINFO_ERROR.getMessage());
            return res;
        }
        ResponseResult insertRB = energyrecordService.checkIn(curUser);
        Integer totalDays = energyrecordService.getDaysSum(curUser);
        JSONObject resData = new JSONObject();
        resData.put("level", curUser.getEnergyPoint() > 0 ? curUser.getEnergyPoint()/100 : 0);
        resData.put("totalDays",totalDays);
        res.setCode(insertRB.getCode());
        res.setMsg(insertRB.getMsg());
        res.setData(resData);

        return res;
    }

    @ApiOperation(value = "获取用户信息", notes = "传入openID")
    @RequestMapping(value = "/auth/getUserInfo/{openID}",method = RequestMethod.GET)
    public ResponseResult getUserInfo(@PathVariable("openID") String openID){
        User userInfo = userService.getUserInfoByOpenId(openID);
        ResponseResult res = new ResponseResult();
        JSONObject resData = new JSONObject();
        if (Objects.isNull(userInfo)) {
            res.setCode(RespBeanEnum.GET_USERINFO_ERROR.getCode());
            res.setMsg(RespBeanEnum.GET_USERINFO_ERROR.getMessage());
            return res;
        }
        resData.put("userInfo", userInfo);
        resData.put("level", userInfo.getEnergyPoint() > 0 ? userInfo.getEnergyPoint()/100 : 0);
        res.setCode(RespBeanEnum.SUCCESS.getCode());
        res.setMsg(RespBeanEnum.SUCCESS.getMessage());
        res.setData(resData);
        return res;

    }

    @ApiOperation(value = "新增能量值记录", notes = "需要传入打赏者id，被打赏者id，能量值，打赏类型")
    @RequestMapping(value = "/auth/insertEnergyRecord",method = RequestMethod.POST)
    public ResponseResult insertEnergyRecord(@RequestBody RewardVo rewardVo){
        ResponseResult res = new ResponseResult();
        // 如果是打赏类型，则要对两个用户的能量值进行更新
        if (rewardVo.getType() == 1){

            // res2为打赏者能量值减少状态
            ResponseResult res2 = energyrecordService.updateUserEnergy(rewardVo.getUserID(),rewardVo.getPoint(),0);
            // 如果报错直接返回
            if (res2.getCode() != 200){
                return res2;
            }
            // res3为被打赏者能量值增加状态
            ResponseResult res3 = energyrecordService.updateUserEnergy(rewardVo.getToUserID(),rewardVo.getPoint(),1);
            if (res3.getCode() != 200){
                return res3;
            }
        }
        ResponseResult res1 = energyrecordService.insertEnergyRecord(rewardVo);

        

        return res1;

    }

    @ApiOperation(value = "加载我的帖子", notes = "需要传入userID")
    @RequestMapping(value = "/auth/loadMyPost",method = RequestMethod.GET)
    public ResponseResult loadMyPost(Integer userID){
        ResponseResult res = new ResponseResult();
        JSONObject resData = new JSONObject();
        List<SimplePostVo> simplePostVos = userService.loadMyPost(userID);
        if (simplePostVos.size() == 0) {
            res.setMsg(ResponseResult.success(RespBeanEnum.NO_POST).getMsg());
            res.setCode(ResponseResult.success(RespBeanEnum.NO_POST).getCode());
            return res;
        }
        resData.put("postList",simplePostVos);
        res.setData(resData);
        res.setMsg(ResponseResult.success(RespBeanEnum.SUCCESS).getMsg());
        res.setCode(ResponseResult.success(RespBeanEnum.SUCCESS).getCode());
        return res;
    }

    @ApiOperation(value = "加载我的收藏", notes = "需要传入userID")
    @RequestMapping(value = "/auth/loadMyStar",method = RequestMethod.GET)
    public ResponseResult loadMyStar(Integer userID){
        ResponseResult res = new ResponseResult();
        JSONObject resData = new JSONObject();
        List<StarPostVo> stars = userService.loadMyStar(userID);
        if (stars.size() == 0) {
            res.setMsg(ResponseResult.success(RespBeanEnum.NO_Star).getMsg());
            res.setCode(ResponseResult.success(RespBeanEnum.NO_Star).getCode());
            return res;
        }
        resData.put("postList",stars);
        res.setData(resData);
        res.setMsg(ResponseResult.success(RespBeanEnum.SUCCESS).getMsg());
        res.setCode(ResponseResult.success(RespBeanEnum.SUCCESS).getCode());
        return res;
    }


    @ApiOperation(value = "删除帖子", notes = "需要传入postID、userID")
    @RequestMapping(value = "/auth/deletePost",method = RequestMethod.POST)
    public ResponseResult deletePost(@RequestBody JSONObject req){
        ResponseResult res = userService.deletePost(req.getLong("postID"), req.getInteger("userID"));
        return res;
    }

    @ApiOperation(value = "删除收藏", notes = "需要传入postID、userID")
    @RequestMapping(value = "/auth/deleteStar",method = RequestMethod.POST)
    public ResponseResult deleteStar(@RequestBody JSONObject req){
        ResponseResult res = userService.deleteStar(req.getLong("postID"), req.getInteger("userID"));
        return res;
    }

    @ApiOperation(value = "测试", notes = "需要传入postID")
    @RequestMapping(value = "/exposure/dele",method = RequestMethod.POST)
    public ResponseResult dele(@RequestBody JSONObject req){

        ResponseResult res = new ResponseResult();
        res.setData(userService.test(req.getLong("postID"), req.getInteger("userID")));
        return res;
    }
}
