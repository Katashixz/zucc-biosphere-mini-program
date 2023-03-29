package com.biosphere.usermodule.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.biosphere.library.pojo.ChatRecord;
import com.biosphere.library.pojo.NotifyEvent;
import com.biosphere.library.pojo.Post;
import com.biosphere.library.util.CommonUtil;
import com.biosphere.library.vo.*;
import com.biosphere.usermodule.config.NettyConfig;
import com.biosphere.usermodule.mapper.NotifyEventMapper;
import com.biosphere.usermodule.mapper.PostMapper;
import com.biosphere.usermodule.service.IMessageService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hyh
 * @description: TODO
 * @date 2023/1/23 15:52
 */
@Slf4j
@Component
public class CommandHandler {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private PostMapper postMapper;

    public void connectionExecute(ChannelHandlerContext ctx, TextWebSocketFrame frame){

        MessageVo msg = JSON.parseObject(frame.text(), MessageVo.class);
        // 获取用户ID，关联channel
        Integer uid = msg.getUserId();
        // 当用户ID已存入通道内,则不进行写入,只有第一次建立连接时才会存入,其他情况发送uid则为心跳需求
        if (!NettyConfig.getUserChannelMap().containsKey(uid)) {
            log.info("服务器收到消息：{}", msg);
            NettyConfig.getUserChannelMap().put(uid, ctx.channel());
            // 将用户ID作为自定义属性加入到channel中，方便随时channel中获取用户ID
            AttributeKey<Integer> key = AttributeKey.valueOf("userId");
            ctx.channel().attr(key).setIfAbsent(uid);
            // 回复消息
            ctx.channel().writeAndFlush(new TextWebSocketFrame(CommonUtil.toChannelSuccessMsg(null)));
            // ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器连接成功！"));
        }else {
            // 前端定时请求，保持心跳连接，避免服务端误删通道
            ctx.channel().writeAndFlush(new TextWebSocketFrame(CommonUtil.toChannelSuccessMsg(null)));
        }
    }

    public void chatExecute(ChannelHandlerContext ctx, TextWebSocketFrame frame){
        // 接收到私聊消息，将消息存入到数据库
        ChatRecord chatMessage = JSON.parseObject(frame.text(), ChatRecord.class);

        if (chatMessage.getTargetID() == null) {
            ctx.channel().writeAndFlush(new TextWebSocketFrame(CommonUtil.toChannelErrorMsg(RespBeanEnum.USER_ID_ERROR)));
            return;
        }
        Channel channel = NettyConfig.getUserChannelMap().get(chatMessage.getTargetID());
        // 用户不在线的情况
        // if (channel == null || !channel.isActive()) {
        //     // 不在线的情况要将消息存入到Redis，用户上线后主动拉取
        //     Map<Integer, ChatRecord> chatRecordMap = new HashMap<>();
        //     // 如果存在这个用户的登陆状态，就更新
        //     if (redisTemplate.opsForHash().hasKey("userID:" + chatMessage.getTargetID(), "chatMsg")) {
        //         // Map数据结构key为消息接受者ID,value为一个消息列表chatRecordMap，其中key为消息发送者ID，value为最新的chatRecord
        //         chatRecordMap = (Map<Integer, ChatRecord>) redisTemplate.opsForHash().get("userID:" + chatMessage.getTargetID(), "chatMsg");
        //     }
        //     // 不存在就新建，不设置过期时间
        //     chatRecordMap.put(chatMessage.getSourceID(), chatMessage);
        //     redisTemplate.opsForHash().put("userID:" + chatMessage.getTargetID(),"chatMsg", chatRecordMap);
        // }
        if(channel != null && channel.isActive()){
            // 在线的情况，websocket用于实现实时聊天，客户端收到消息对全局变量进行处理。
            log.info("服务器收到消息：{}", chatMessage);
            channel.writeAndFlush(new TextWebSocketFrame(CommonUtil.toChannelSuccessMsg(chatMessage)));
        }
        // Redis实现消息通知，不在线的话用户上线后主动拉取，在线的话进入通知后拉取
        Map<Integer, ChatRecord> chatRecordMap = new HashMap<>();
        // 如果存在这个用户的登陆状态，就更新
        if (redisTemplate.opsForHash().hasKey("userID:" + chatMessage.getTargetID(), "chatMsg")) {
            // Map数据结构key为消息接受者ID,value为一个消息列表chatRecordMap，其中key为消息发送者ID，value为最新的chatRecord
            chatRecordMap = (Map<Integer, ChatRecord>) redisTemplate.opsForHash().get("userID:" + chatMessage.getTargetID(), "chatMsg");
        }
        // 不存在就新建，在service里操作，不设置过期时间。key为消息来源的ID

        messageService.saveChatMsg(chatMessage, chatRecordMap);
    }

    public void notifyExecute(ChannelHandlerContext ctx, TextWebSocketFrame frame){
        // 接收到评论或打赏或点赞通知，行为存入到数据库
        NotifyEvent notifyEvent = JSON.parseObject(frame.text(), NotifyEvent.class);
        Integer targetId = notifyEvent.getObjectID();
        // 点赞是人对帖子的行为，要经过消息通知需要先获得发帖人的ID
        if (notifyEvent.getObjectType() == 2){
            QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("userID").eq("id",notifyEvent.getObjectID());
            targetId = postMapper.selectOne(queryWrapper).getUserID();
        }
        Channel channel = NettyConfig.getUserChannelMap().get(targetId);
        // 缓存中存入，作为已读和未读标志
        // 用户在线的情况
        if(channel != null && channel.isActive()){
            log.info("服务器收到点赞/取消点赞消息：{}", notifyEvent);
            channel.writeAndFlush(new TextWebSocketFrame(CommonUtil.toChannelSuccessMsg(notifyEvent)));
        }
        // Redis存的是某个用户对某个用户的最后行为，所以如果是撤销操作，要先检查Redis里的行为是不是要撤销的那个
        // Redis实现消息通知，如果是撤销操作，先确认redis里的消息是否为此消息，是则移除，不是则不作任何操作。
        // Map<Integer, NotifyEvent> map = new HashMap<>();
        // if (redisTemplate.opsForHash().hasKey("userID:" + targetId, "likeMsg")) {
        //     map = (Map<Integer, NotifyEvent>) redisTemplate.opsForHash().get("userID:" + targetId, "likeMsg");
        // }
        // if (notifyEvent.getRemoveState() == 1) {
        //     if (map.get(notifyEvent.getUserID()).equalState(notifyEvent))
        //         map.remove(notifyEvent.getUserID());
        // }
        messageService.saveNotifyMsg(notifyEvent, targetId);
    }

}
