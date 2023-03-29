package com.biosphere.usermodule.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.biosphere.library.pojo.*;
import com.biosphere.library.vo.*;
import com.biosphere.usermodule.mapper.*;
import com.biosphere.usermodule.service.IMessageService;

import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hyh
 * @description: TODO
 * @date 2023/1/20 17:10
 */
@Service
public class MessageService implements IMessageService {

    @Autowired
    private ChatRecordMapper chatRecordMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private NotifyEventMapper notifyEventMapper;

    @Autowired
    private ViewChatMsgMapper viewChatMsgMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ViewLikeMsgMapper viewLikeMsgMapper;

    @Autowired
    private EnergyRecordMapper energyRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveChatMsg(ChatRecord chatRecord, Map<Integer, ChatRecord> chatRecordMap) {
        //将聊天信息更新到数据库
        int insert = chatRecordMapper.insert(chatRecord);
        if (insert <= 0) {
            throw new ExceptionNoLogVo(RespBeanEnum.ERROR);
        }
        chatRecordMap.put(chatRecord.getSourceID(), chatRecord);
        redisTemplate.opsForHash().put("userID:" + chatRecord.getTargetID(),"chatMsg", chatRecordMap);

    }

    @Override
    public boolean hasMsg(Integer userID) {
        Map<Integer, ChatRecord> chatMsg = (Map<Integer, ChatRecord>) redisTemplate.opsForHash().get("userID:" + userID, "chatMsg");
        if (chatMsg != null && chatMsg.size() > 0) {
            return true;
        }
        Map<Integer, NotifyEvent> commentMsg = (Map<Integer, NotifyEvent>) redisTemplate.opsForHash().get("userID:" + userID.toString(), "commentMsg");
        if (commentMsg != null && commentMsg.size() > 0) {
            return true;
        }
        Map<Integer, NotifyEvent> likeMsg = (Map<Integer, NotifyEvent>) redisTemplate.opsForHash().get("userID:" + userID.toString(), "likeMsg");
        if (likeMsg != null && likeMsg.size() > 0) {
            return true;
        }
        Map<Integer, NotifyEvent> chargeMsg = (Map<Integer, NotifyEvent>) redisTemplate.opsForHash().get("userID:" + userID.toString(), "chargeMsg");
        if (chargeMsg != null && chargeMsg.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveNotifyMsg(NotifyEvent notifyEvent, Integer targetId) {
        String hashKey;
        switch (notifyEvent.getAction()){
            case 1: hashKey = "likeMsg";break;
            case 2: hashKey = "commentMsg";break;
            case 4: hashKey = "chargeMsg";break;
            case 5: hashKey = "systemMsg";break;
            default: hashKey = "";
        }
        Map<Integer, NotifyEvent> map = new HashMap<>();
        if (redisTemplate.opsForHash().hasKey("userID:" + targetId, hashKey)) {
            map = (Map<Integer, NotifyEvent>) redisTemplate.opsForHash().get("userID:" + targetId, hashKey);
        }
        // 将通知信息保存到数据库
        if (notifyEvent.getRemoveState() == 0) {
            int insert = notifyEventMapper.insert(notifyEvent);
            if (insert <= 0) {
                throw new ExceptionNoLogVo(RespBeanEnum.ERROR);
            }
            map.put(notifyEvent.getUserID(), notifyEvent);

        }else {
            // 如果是撤销，那么之前一定有一次插入操作
            // 如果是点赞的撤销，只需要找最新一次对这个帖子的点赞更改即可
            if (notifyEvent.getAction() == 1) {
                // UpdateWrapper<NotifyEvent> wrapper = new UpdateWrapper<>();
                // wrapper.set("createdAt",notifyEvent.getCreatedAt()).set("removeState",notifyEvent.getRemoveState())
                //         .eq("userID",notifyEvent.getUserID())
                //         .eq("action", notifyEvent.getAction())
                //         .eq("objectID", notifyEvent.getObjectID())
                //         .eq("objectType", notifyEvent.getObjectType())
                //         .orderByDesc("createdAt");
                QueryWrapper<NotifyEvent> queryWrapper = new QueryWrapper<>();
                // 找到最新的行为
                queryWrapper.select("id")
                        .eq("userID",notifyEvent.getUserID())
                        .eq("action", notifyEvent.getAction())
                        .eq("objectID", notifyEvent.getObjectID())
                        .eq("objectType", notifyEvent.getObjectType())
                        .eq("removeState", 0)
                        .orderByDesc("createdAt");
                NotifyEvent eventId = notifyEventMapper.selectOne(queryWrapper);
                notifyEvent.setId(eventId.getId());
                int update = notifyEventMapper.updateById(notifyEvent);
                if (update <= 0) {
                    throw new ExceptionNoLogVo(RespBeanEnum.ERROR);
                }
            }
            // Redis存的是某个用户对某个用户的最后行为，所以如果是撤销操作，要先检查Redis里的行为是不是要撤销的那个
            // Redis实现消息通知，如果是撤销操作，先确认redis里的消息是否为此消息，是则移除，不是则不作任何操作。
            NotifyEvent tmp = map.get(notifyEvent.getUserID().toString());
            if (tmp != null && tmp.equalState(notifyEvent)) {
                map.remove(notifyEvent.getUserID().toString());
            }
        }
        redisTemplate.opsForHash().put("userID:" + targetId, hashKey, map);

    }

    @Override
    public Map<Integer, ChatRecord> getNewChat(Integer userId) {
        // 新的对话就是redis里缓存的
        Map<Integer, ChatRecord> chatMsg = (Map<Integer, ChatRecord>) redisTemplate.opsForHash().get("userID:" + userId, "chatMsg");
        // redisTemplate.opsForHash().delete("userID:" + userId, "chatMsg");
        return chatMsg;
    }

    @Override
    public JSONArray getChatRecord(Integer userId, Map<Integer, ChatRecord> newChat) {
        List<ViewChatMsg> viewChatMsgs = viewChatMsgMapper.getLatestMsg(userId);
        JSONArray res = new JSONArray();
        // 获取的历史消息最后一条可能是自己，但是前端需要接受的是目标用户信息
        for (ViewChatMsg viewChatMsg : viewChatMsgs) {
            JSONObject tmp = new JSONObject();
            tmp.put("chatMsg", viewChatMsg);
            // 如果消息来源于自己，用户信息要获取target的，如果不是自己，就获取source的
            UserInfoVo targetUser = new UserInfoVo();
            if (viewChatMsg.getSourceID() == userId) {
                targetUser.setUserId(viewChatMsg.getTargetID());
                targetUser.setUserName(viewChatMsg.getTargetName());
                targetUser.setAvatarUrl(viewChatMsg.getTargetAvatar());
            }else {
                targetUser.setUserId(viewChatMsg.getSourceID());
                targetUser.setUserName(viewChatMsg.getSourceName());
                targetUser.setAvatarUrl(viewChatMsg.getSourceAvatar());
            }
            tmp.put("userInfo", targetUser);
            // 是否为未读消息只需要判断最后一条消息的主键是否与redis里的一致
            if (newChat != null)
                tmp.put("newFlag", viewChatMsg.getChatId().equals(newChat.getOrDefault(targetUser.getUserId().toString(), new ChatRecord()).getId()));
            else
                tmp.put("newFlag", false);
            res.add(tmp);
        }
        return res;
    }

    @Override
    public JSONObject hasNotify(Integer userId) {
        JSONObject res = new JSONObject();
        Map<Integer, NotifyEvent> commentMsg = (Map<Integer, NotifyEvent>) redisTemplate.opsForHash().get("userID:" + userId.toString(), "commentMsg");
        Map<Integer, NotifyEvent> chargeMsg = (Map<Integer, NotifyEvent>) redisTemplate.opsForHash().get("userID:" + userId.toString(), "chargeMsg");
        Map<Integer, NotifyEvent> likeMsg = (Map<Integer, NotifyEvent>) redisTemplate.opsForHash().get("userID:" + userId.toString(), "likeMsg");
        res.put("hasLikeMsg", false);
        res.put("hasCommentMsg", false);
        res.put("hasChargeMsg", false);
        if (commentMsg != null && commentMsg.size() > 0) {
            res.put("hasCommentMsg", true);
        }
        if (likeMsg != null && likeMsg.size() > 0) {
            res.put("hasLikeMsg", true);
        }
        if (chargeMsg != null && chargeMsg.size() > 0) {
            res.put("hasChargeMsg", true);
        }
        return res;
    }

    @Override
    public List<ViewChatMsg> getOneChatHistory(Integer sourceId, Integer targetID) {
        // 访问了与某人的聊天记录，就要清空缓存中与target的新消息记录
        List<ViewChatMsg> res = viewChatMsgMapper.getOneChatHistory(sourceId, targetID);
        Map<Integer, ChatRecord> chatMsg = (Map<Integer, ChatRecord>) redisTemplate.opsForHash().get("userID:" + sourceId.toString(), "chatMsg");
        if (chatMsg != null) {
            chatMsg.remove(targetID.toString());
            redisTemplate.opsForHash().put("userID:" + sourceId.toString(),"chatMsg", chatMsg);
        }
        return res;
    }

    @Override
    public List<CommentNotifyVo> getCommentNotify(Integer userID) {
        List<CommentNotifyVo> commentNotifyVos = commentMapper.loadCommentNotify(userID);
        redisTemplate.opsForHash().delete("userID:" + userID, "commentMsg");
        return commentNotifyVos;
    }

    @Override
    public List<ViewLikeMsg> getLikeNotify(Integer userID) {
        QueryWrapper<ViewLikeMsg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("targetID", userID).ne("userID", userID).orderByDesc("createdAt");
        List<ViewLikeMsg> viewLikeMsgs = viewLikeMsgMapper.selectList(queryWrapper);
        redisTemplate.opsForHash().delete("userID:" + userID, "likeMsg");
        return viewLikeMsgs;
    }

    @Override
    public List<EnergyRecord> getEnergyNotify(Integer userID) {
        QueryWrapper<EnergyRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("userID", userID).or().eq("toUserID", userID).orderByDesc("getDate");
        List<EnergyRecord> energyRecords = energyRecordMapper.selectList(wrapper);
        redisTemplate.opsForHash().delete("userID:" + userID, "chargeMsg");
        return energyRecords;
    }
}
