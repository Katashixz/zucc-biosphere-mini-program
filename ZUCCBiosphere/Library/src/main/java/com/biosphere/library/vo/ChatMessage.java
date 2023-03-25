package com.biosphere.library.vo;

import io.swagger.models.auth.In;

/**
 * @author hyh
 * @description: TODO
 * @date 2023/1/23 17:02
 */
public class ChatMessage extends MessageVo{

    // 私聊对象Id
    private Integer target;

    // 私聊内容
    private String content;

    //消息类型，1为

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "target=" + target +
                ", content='" + content + '\'' +
                '}';
    }
}
