package com.biosphere.usermodule.handler;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.biosphere.library.util.CommonUtil;
import com.biosphere.library.vo.*;
import com.biosphere.usermodule.config.NettyConfig;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 来源网络
 * @description: TODO
 * @date 2023/1/20 15:38
 */
@Component
@Slf4j
@ChannelHandler.Sharable
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Autowired
    private CommandHandler commandHandler;

    /**
     * 功能描述: 一旦连接，第一个被执行
     * @param: null
     * @return:
     * @author 来源网络
     * @date: 2023/1/20 16:34
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("handlerAdded 被调用" + ctx.channel().id().asLongText());
        // 添加到channelGroup 通道组
        NettyConfig.getChannelGroup().add(ctx.channel());
    }

    /**
     * 功能描述: 读取数据
     * @param: null
     * @return:
     * @author 来源网络
     * @date: 2023/1/20 16:38
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
        try{
            // MessageVo messageVo = JSON.parseObject(frame.text(), MessageVo.class);
            JSONObject obj = JSON.parseObject(frame.text(), JSONObject.class);
            switch (CommandEnum.match((Integer) obj.get("code"))){
                // 连接请求处理
                case CONNECTION : commandHandler.connectionExecute(ctx, frame);break;
                // 私聊请求处理
                case CHAT: commandHandler.chatExecute(ctx, frame);break;
                case LIKE: commandHandler.notifyExecute(ctx, frame);break;
                case COMMENT: commandHandler.notifyExecute(ctx, frame);break;
                case REWARD: commandHandler.notifyExecute(ctx, frame);break;
                default : ctx.channel().writeAndFlush(new TextWebSocketFrame(CommonUtil.toChannelErrorMsg(RespBeanEnum.UNSUPPORTED_CODE)));
            }
        }catch (ExceptionLogVo e){
            // ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(ResponseResult.error(e.getE()))));
            ctx.channel().writeAndFlush(new TextWebSocketFrame(CommonUtil.toChannelErrorMsg(e.getE())));
        }
    }

    /**
     * 功能描述: 移除通道及关联用户
     * @param: null
     * @return:
     * @author 来源网络
     * @date: 2023/1/20 16:43
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("handlerRemoved  被调用" + ctx.channel().id().asLongText());
        // 删除通道
        NettyConfig.getChannelGroup().remove(ctx.channel());
        removeUserId(ctx);
    }

    /**
     * 功能描述: 异常处理
     * @param: null
     * @return:
     * @author 来源网络
     * @date: 2023/1/20 16:45
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
        log.info("异常：{}", cause.getMessage());
        // 报错加入到错误日志
        // ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(cause.getMessage())));
        // 删除通道
        NettyConfig.getChannelGroup().remove(ctx.channel());
        removeUserId(ctx);
        ctx.close();
    }

    /**
     * 功能描述: 心跳检测相关方法 - 会主动调用handlerRemoved
     * @param: null
     * @return:
     * @author 来源网络
     * @date: 2023/1/20 16:47
     */
    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.ALL_IDLE) {
                // 清除超时会话
                ChannelFuture writerAndFlush = ctx.writeAndFlush("会话超时，将被关闭");
                writerAndFlush.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        ctx.channel().close();
                    }
                });
            }else {
                super.userEventTriggered(ctx, evt);
            }
        }
    }

    /**
     * 功能描述: 删除用户与channel的对应关系
     * @param: null
     * @return:
     * @author 来源网络
     * @date: 2023/1/20 16:51
     */
    private void removeUserId(ChannelHandlerContext ctx){
        AttributeKey<Integer> key = AttributeKey.valueOf("userId");
        Integer userId = ctx.channel().attr(key).get();
        NettyConfig.getUserChannelMap().remove(userId);
        log.info("删除uid与channel对应关系:{}",userId);
    }
}
