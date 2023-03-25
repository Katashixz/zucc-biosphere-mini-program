package com.biosphere.usermodule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.biosphere.library.pojo.ChatRecord;
import com.biosphere.library.pojo.NotifyEvent;
import com.biosphere.library.vo.ChatMessage;
import com.biosphere.library.vo.ExceptionNoLogVo;
import com.biosphere.library.vo.RespBeanEnum;
import com.biosphere.usermodule.mapper.ChatRecordMapper;
import com.biosphere.usermodule.mapper.NotifyEventMapper;
import com.biosphere.usermodule.service.IMessageService;

import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveChatMsg(ChatRecord chatRecord) {
        //将聊天信息更新到数据库
        int insert = chatRecordMapper.insert(chatRecord);
        if (insert <= 0) {
            throw new ExceptionNoLogVo(RespBeanEnum.ERROR);
        }
    }

    @Override
    public boolean hasMsg(Integer userID) {
        if (redisTemplate.opsForHash().hasKey("userID:" + userID, "chatMsg")) {
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
        redisTemplate.opsForHash().delete("userID:" + userId, "chatMsg");
        return chatMsg;
    }

    @Override
    public List<ChatRecord> getChatRecord(Integer userId) {

        return null;
    }
}
