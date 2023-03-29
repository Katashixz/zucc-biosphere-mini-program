package com.biosphere.library.vo;

/**
 * @Author Administrator
 * @Date 2022/11/11 16:30
 * @Version 1.0
 */
public class UserInfoVo {
    private Integer userId;

    private String userName;

    private String avatarUrl;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}

