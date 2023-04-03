package com.biosphere.usermodule.controller;


import com.alibaba.fastjson.JSONObject;
import com.biosphere.library.util.JwtUtil;
import com.biosphere.library.util.TencentCosUtil;
import com.biosphere.library.vo.*;
import com.biosphere.usermodule.service.IMessageService;
import com.biosphere.usermodule.service.IUserService;
import com.biosphere.library.pojo.User;
import com.biosphere.usermodule.service.IEnergyRecordService;
import com.biosphere.library.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
@RestControllerAdvice
@CrossOrigin
@Validated
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IEnergyRecordService energyrecordService;

    @Autowired
    private IMessageService messageService;

    @ApiOperation(value = "用户登录", notes = "wx.login上传加密字符串即可")
    @RequestMapping(value = "/exposure/login",method = RequestMethod.POST)
    public JSONObject userLogin(@Validated @RequestBody LoginVo loginVo){
        JSONObject resData = new JSONObject();
        // 从微信官方接口获取用户数据
        String openID = userService.getOpenID(loginVo.getCode());
        if (CommonUtil.isBlank(openID)) {
            throw new ExceptionLogVo(RespBeanEnum.CODE_GET_ERROR);
        }
        // 要根据openID生成JWT和token存入Redis和数据库
        // User curUser = userService.checkUserInfo(openID, loginVo.getAvatarUrl(),loginVo.getNickName());
        User curUser = userService.checkUserInfo(openID);
        resData.put("userInfo",curUser);
        resData.put("LatestLoginDate", CommonUtil.DateFormat(curUser.getLatestLoginTime()));
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
        // 确认消息通知缓存信息
        boolean hasMsg = messageService.hasMsg(curUser.getId());
        resData.put("token", token);
        resData.put("level", curUser.getEnergyPoint() > 0 ? curUser.getEnergyPoint()/100 : 0);
        resData.put("hasMsg", hasMsg);
        return resData;
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
    public JSONObject getUserInfo(@PathVariable("openID")String openID){
        User userInfo = userService.getUserInfoByOpenId(openID);
        JSONObject resData = new JSONObject();
        if (Objects.isNull(userInfo)) {
            throw new ExceptionLogVo(RespBeanEnum.GET_USERINFO_ERROR);
        }
        // 确认消息通知缓存信息
        boolean hasMsg = messageService.hasMsg(userInfo.getId());

        resData.put("userInfo", userInfo);
        resData.put("level", userInfo.getEnergyPoint() > 0 ? userInfo.getEnergyPoint()/100 : 0);
        resData.put("hasMsg", hasMsg);
        return resData;

    }

    @ApiOperation(value = "新增能量值记录", notes = "需要传入打赏者id，被打赏者id，能量值，打赏类型")
    @RequestMapping(value = "/auth/insertEnergyRecord",method = RequestMethod.POST)
    public void insertEnergyRecord(@Validated @RequestBody RewardVo rewardVo){
        // 如果是打赏类型，则要对两个用户的能量值进行更新
        if (rewardVo.getType() == 1){
            // 打赏者能量值减少状态
            energyrecordService.updateUserEnergy(rewardVo.getUserID(),rewardVo.getPoint(),0);
            // 被打赏者能量值增加状态
            energyrecordService.updateUserEnergy(rewardVo.getToUserID(),rewardVo.getPoint(),1);
        }else if(rewardVo.getType() == 3){
            // 打赏者能量值减少状态
            energyrecordService.updateUserEnergy(rewardVo.getUserID(),rewardVo.getPoint(),0);
        }
        energyrecordService.insertEnergyRecord(rewardVo);
    }

    @ApiOperation(value = "加载我的帖子", notes = "需要传入userID")
    @RequestMapping(value = "/auth/loadMyPost",method = RequestMethod.GET)
    public JSONObject loadMyPost(Integer userID){
        JSONObject resData = new JSONObject();
        List<SimplePostVo> simplePostVos = userService.loadMyPost(userID);
        if (simplePostVos.size() == 0) {
            throw new ExceptionNoLogVo(RespBeanEnum.NO_POST);
        }
        resData.put("postList",simplePostVos);
        return resData;
    }

    @ApiOperation(value = "加载我的收藏", notes = "需要传入userID")
    @RequestMapping(value = "/auth/loadMyStar",method = RequestMethod.GET)
    public JSONObject loadMyStar(Integer userID){
        JSONObject resData = new JSONObject();
        List<StarPostVo> stars = userService.loadMyStar(userID);
        if (stars.size() == 0) {
            throw new ExceptionNoLogVo(RespBeanEnum.NO_Star);
        }
        resData.put("postList",stars);
        return resData;
    }
    @ApiOperation(value = "加载我的评论", notes = "需要传入userID")
    @RequestMapping(value = "/auth/loadMyComment",method = RequestMethod.GET)
    public JSONObject loadMyComment(Integer userID){
        JSONObject resData = new JSONObject();
        List<CommentVo> commentVos = userService.loadMyComment(userID);
        if (commentVos.size() == 0) {
            throw new ExceptionNoLogVo(RespBeanEnum.NO_Comment);
        }
        resData.put("commentList",commentVos);
        return resData;
    }

    @ApiOperation(value = "加载我的投食", notes = "需要传入userID")
    @RequestMapping(value = "/auth/loadMyAdopt",method = RequestMethod.GET)
    public JSONObject loadMyAdopt(Integer userID){
        JSONObject resData = new JSONObject();
        List<MyAdoptVo> adoptVos = userService.loadMyAdopt(userID);
        resData.put("adoptList",adoptVos);
        return resData;
    }

    @ApiOperation(value = "删除帖子", notes = "需要传入postID、userID")
    @RequestMapping(value = "/auth/deletePost",method = RequestMethod.POST)
    public void deletePost(@RequestBody JSONObject req){
        userService.deletePost(req.getLong("postID"), req.getInteger("userID"));
    }


    @ApiOperation(value = "删除评论", notes = "需要传入评论id、postID、userID")
    @RequestMapping(value = "/auth/deleteComment",method = RequestMethod.POST)
    public void deleteComment(@RequestBody JSONObject req){
        userService.deleteComment(req.getLong("postID"), req.getInteger("id"), req.getInteger("userID"));
    }

    @ApiOperation(value = "删除收藏", notes = "需要传入postID、userID")
    @RequestMapping(value = "/auth/deleteStar",method = RequestMethod.POST)
    public void deleteStar(@RequestBody JSONObject req){
        userService.deleteStar(req.getLong("postID"), req.getInteger("userID"));
    }

    @ApiOperation(value = "修改用户头像和名称", notes = "需要传入userID、nickName、avatar")
    @RequestMapping(value = "/auth/updateInfo",method = RequestMethod.POST)
    public void updateInfo(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        Integer id = Integer.valueOf(request.getParameter("id"));
        String nickName = request.getParameter("nickName");
        if (Objects.isNull(id) || CommonUtil.isBlank(nickName)) {
            throw new ExceptionNoLogVo(RespBeanEnum.ERROR_INFO);
        }
        // 取出Token
        String token = request.getHeader("token");
        String openID = new String();
        try{
            openID = JwtUtil.parseJWT(token).getSubject();
        }catch (Exception e){
            throw new ExceptionLogVo(RespBeanEnum.ERROR_INFO);
        }
        String url = TencentCosUtil.uploadAvatar(file,openID);
        if (CommonUtil.isBlank(url)) {
            throw new ExceptionLogVo(RespBeanEnum.UPLOAD_IMG_ERROR);
        }
        userService.updateInfo(id, nickName, url, openID);

    }

    @ApiOperation(value = "测试", notes = "")
    @RequestMapping(value = "/exposure/test",method = RequestMethod.POST)
    public Integer dele() {
        // int i = 1/0;
        throw new ExceptionLogVo(RespBeanEnum.DELETE_ERROR);
    }
}
