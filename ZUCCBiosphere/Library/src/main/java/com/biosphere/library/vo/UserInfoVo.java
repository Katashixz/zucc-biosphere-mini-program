package com.biosphere.library.vo;

/**
 * @Author Administrator
 * @Date 2022/11/11 16:30
 * @Version 1.0
 */
public class UserInfoVo {
    private Integer id;

    private String nickName;

    private String avatar;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
