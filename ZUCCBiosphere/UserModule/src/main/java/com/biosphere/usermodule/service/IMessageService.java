package com.biosphere.usermodule.service;

import com.biosphere.library.pojo.ChatRecord;
import com.biosphere.library.pojo.NotifyEvent;
import com.biosphere.library.vo.ChatMessage;
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
   void saveChatMsg(ChatRecord chatRecord);

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
    * @return: java.util.List<com.biosphere.library.vo.ChatMessage>
    * @author hyh
    * @date: 2023/3/25 16:33
    */
   Map<Integer, ChatRecord> getNewChat(Integer userId);

   /**
    * 功能描述: 获取群体历史第一句对话
    * @param: userId
    * @return: java.util.Map<java.lang.Integer,com.biosphere.library.pojo.ChatRecord>
    * @author hyh
    * @date: 2023/3/25 16:54
    */
   List<ChatRecord> getChatRecord(Integer userId);


}
