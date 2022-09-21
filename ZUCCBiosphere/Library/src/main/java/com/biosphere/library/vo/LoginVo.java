package com.biosphere.library.vo;

import java.io.Serializable;

/**
 * @Author Administrator
 * @Date 2022/8/12 11:20
 * @Version 1.0
 */
public class LoginVo implements Serializable {
    private String code;
    private String avatarUrl;
    private String nickName;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
