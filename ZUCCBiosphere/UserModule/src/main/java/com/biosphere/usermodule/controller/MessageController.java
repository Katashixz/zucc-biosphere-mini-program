package com.biosphere.usermodule.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.biosphere.library.pojo.ChatRecord;
import com.biosphere.library.pojo.ViewChatMsg;
import com.biosphere.library.vo.ExceptionLogVo;
import com.biosphere.library.vo.MessageVo;
import com.biosphere.library.vo.RespBeanEnum;
import com.biosphere.usermodule.service.IMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author hyh
 * @description: TODO
 * @date 2023/1/20 17:14
 */
@Api(tags = "私信模块")
@Slf4j
@RestControllerAdvice
@CrossOrigin
@Validated
@RequestMapping("/user")
public class MessageController {
    @Autowired
    private IMessageService messageService;

    @ApiOperation(value = "拉取私信", notes = "传入用户Id")
    @RequestMapping(value = "/auth/getChat",method = RequestMethod.POST)
    public JSONArray getChat(@RequestBody JSONObject object){
        Integer userId = object.getInteger("userID");
        if (userId == null) {
            throw new ExceptionLogVo(RespBeanEnum.INFO_ERROR);
        }
        Map<Integer, ChatRecord> newChat = messageService.getNewChat(userId);
        JSONArray chatRecord = messageService.getChatRecord(userId, newChat);
        return chatRecord;
    }

    @ApiOperation(value = "拉取点赞、回复、充电通知", notes = "传入用户Id")
    @RequestMapping(value = "/auth/getNotify", method = RequestMethod.POST)
    public JSONObject getNotify(@RequestBody JSONObject object){
        Integer userId = object.getInteger("userID");
        if (userId == null) {
            throw new ExceptionLogVo(RespBeanEnum.INFO_ERROR);
        }
        return messageService.hasNotify(userId);
    }

    @ApiOperation(value = "获取与某人的聊天记录", notes = "传入用户与目标用户")
    @RequestMapping(value = "/auth/getOneChatHistory", method = RequestMethod.GET)
    public JSONObject getOneChatHistory(Integer sourceID, Integer targetID){
        JSONObject resData = new JSONObject();
        List<ViewChatMsg> oneChatHistory = messageService.getOneChatHistory(sourceID, targetID);
        resData.put("history", oneChatHistory);
        return resData;
    }

    @ApiOperation(value = "获取点赞/评论/打赏详情", notes = "传入用户Id与通知类型")
    @RequestMapping(value = "/auth/getNotifyDetail", method = RequestMethod.GET)
    public JSONObject getNotifyDetail(Integer userID, Integer notifyType){
        JSONObject res = new JSONObject();
        // 1为点赞，2为评论，3为充电
        switch (notifyType){
            case 0: res.put("likeMsgList",messageService.getLikeNotify(userID));break;
            case 1: res.put("commentMsgList", messageService.getCommentNotify(userID));break;
            case 2: res.put("energyMsgList", messageService.getEnergyNotify(userID));break;
        }
        return res;
    }







    }
