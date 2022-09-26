package com.biosphere.usermodule.controller;


import com.alibaba.fastjson.JSONObject;
import com.biosphere.usermodule.service.IUserService;
import com.biosphere.library.pojo.User;
import com.biosphere.usermodule.service.IEnergyRecordService;
import com.biosphere.library.util.FormatUtil;
import com.biosphere.library.vo.LoginVo;
import com.biosphere.library.vo.RespBeanEnum;
import com.biosphere.library.vo.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IEnergyRecordService energyrecordService;

    @ApiOperation(value = "用户登录", notes = "wx.login上传加密字符串即可")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
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
        //要根据openID生成JWT和token存入Redis和数据库
        User curUser = userService.checkUserInfo(openID, loginVo.getAvatarUrl(),loginVo.getNickName());
        resData.put("userInfo",curUser);
        resData.put("LatestLoginDate", FormatUtil.DateFormat(curUser.getLatestLoginTime()));
        //生成token
        String token = userService.tokenGenerate(curUser);
        //点赞数据存入缓存
        userService.saveUserLikeRecords(curUser.getId());

        resData.put("token", token);
        resData.put("level", curUser.getEnergyPoint() > 0 ? curUser.getEnergyPoint()/100 : 0);
        res.setData(resData);
        res.setMsg(ResponseResult.success(RespBeanEnum.SUCCESS).getMsg());
        res.setCode(ResponseResult.success(RespBeanEnum.SUCCESS).getCode());

        return res;
    }

    @ApiOperation(value = "每日签到", notes = "传入openID")
    @RequestMapping(value = "/checkIn",method = RequestMethod.POST)
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
    @RequestMapping(value = "/getUserInfo/{openID}",method = RequestMethod.GET)
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






}
