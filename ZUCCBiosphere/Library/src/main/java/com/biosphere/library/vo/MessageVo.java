package com.biosphere.library.vo;

import io.swagger.models.auth.In;

import java.util.Date;

/**
 * @author hyh
 * @description: TODO
 * @date 2023/1/22 17:02
 */
public class MessageVo {

    // 命令
    private Integer code;

    private Integer userId;

    private Date time;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "MessageVo{" +
                "code=" + code +
                ", userId='" + userId + '\'' +
                ", time=" + time +
                '}';
    }
}
