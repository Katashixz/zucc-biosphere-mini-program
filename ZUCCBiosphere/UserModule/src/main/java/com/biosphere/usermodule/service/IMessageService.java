package com.biosphere.usermodule.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.biosphere.library.pojo.*;
import com.biosphere.library.vo.ChatMessage;
import com.biosphere.library.vo.CommentNotifyVo;
import io.swagger.models.auth.In;

import java.util.List;
import java.util.Map;

public interface IMessageService {

   /**
    * 功能描述: 保存用户私聊消息到数据库
    * @param: chatRecord
    * @return: boolean
    * @author hyh
    * @date: 2023/3/9 17:47
    */
   void saveChatMsg(ChatRecord chatRecord, Map<Integer, ChatRecord> chatRecordMap);

   /** 
    * 功能描述: 拉取消息通知信息 
    * @param: userID 
    * @return: boolean 
    * @author hyh
    * @date: 2023/3/21 16:50
    */ 
   boolean hasMsg(Integer userID);

   /** 
    * 功能描述: 保存通知消息到数据库 
    * @param: notifyEvent 
    * @return: void 
    * @author hyh
    * @date: 2023/3/22 20:51
    */ 
   void saveNotifyMsg(NotifyEvent notifyEvent, Integer targetId);

   /**
    * 功能描述: 获取新的对话
    * @param: userId
    * @return: java.util.Map<java.lang.Integer,com.biosphere.library.pojo.ChatRecord>
    * @author hyh
    * @date: 2023/3/27 17:12
    */
   Map<Integer, ChatRecord> getNewChat(Integer userId);

   /**
    * 功能描述: 获取群体历史第一句对话
    * @param: userId,newChat
    * @return: com.alibaba.fastjson.JSONArray
    * @author hyh
    * @date: 2023/3/27 17:12
    */
   JSONArray getChatRecord(Integer userId,  Map<Integer, ChatRecord> newChat);

   /**
    * 功能描述: 获取是否有点赞、回复、充电通知
    * @param: userId
    * @return: com.alibaba.fastjson.JSONObject
    * @author hyh
    * @date: 2023/3/27 17:13
    */
   JSONObject hasNotify(Integer userId);

   /**
    * 功能描述: 获取与某人对话的历史记录
    * @param: sourceId,targetID
    * @return: java.util.List<com.biosphere.library.pojo.ViewChatMsg>
    * @author hyh
    * @date: 2023/3/28 15:55
    */
   List<ViewChatMsg> getOneChatHistory(Integer sourceId, Integer targetID);

   /**
    * 功能描述: 获取评论通知历史
    * @param: userID
    * @return: java.util.List<com.biosphere.library.vo.CommentNotifyVo>
    * @author hyh
    * @date: 2023/3/28 16:09
    */
   List<CommentNotifyVo> getCommentNotify(Integer userID);

   /**
    * 功能描述: 获取点赞通知历史
    * @param: userID
    * @return: java.util.List<com.biosphere.library.pojo.ViewLikeMsg>
    * @author hyh
    * @date: 2023/3/28 16:20
    */
   List<ViewLikeMsg> getLikeNotify(Integer userID);


   List<EnergyRecord> getEnergyNotify(Integer userID);



}
