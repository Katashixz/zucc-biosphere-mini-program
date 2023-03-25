package com.biosphere.library.vo;

import io.swagger.models.auth.In;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述: 命令枚举类
 * @param: null
 * @return:
 * @author hyh
 * @date: 2023/1/23 15:23
 */
public enum CommandEnum {


    // 建立连接指令
    CONNECTION(10001),
    // 聊天指令
    CHAT(10002),
    // 点赞通知
    LIKE(10003),
    // 评论通知
    COMMENT(10004),
    // 打赏通知
    REWARD(10005),

    // 不支持的类型
    ERROR(-1),
    ;

    private final Integer code;

    public static CommandEnum match(Integer code){
        for (CommandEnum value : CommandEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return ERROR;
    }

    CommandEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
