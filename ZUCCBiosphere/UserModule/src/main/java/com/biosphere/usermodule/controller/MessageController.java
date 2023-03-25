package com.biosphere.usermodule.controller;

import com.alibaba.fastjson.JSONObject;
import com.biosphere.library.pojo.ChatRecord;
import com.biosphere.library.vo.MessageVo;
import com.biosphere.usermodule.service.IMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "拉取私信以及点赞、回复、充电消息标识", notes = "传入用户Id")
    @RequestMapping(value = "/auth/getNotifyAndChat",method = RequestMethod.POST)
    public JSONObject getNotifyAndChat(@RequestBody JSONObject object){
        Integer userId = object.getInteger("userId");
        Map<Integer, ChatRecord> newChat = messageService.getNewChat(userId);

        return null;
    }






}
