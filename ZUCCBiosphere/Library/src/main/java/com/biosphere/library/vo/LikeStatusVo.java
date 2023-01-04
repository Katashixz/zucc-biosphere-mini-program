package com.biosphere.library.vo;

import javax.validation.constraints.NotNull;

/**
 * @Author Administrator
 * @Date 2022/9/26 9:51
 * @Version 1.0
 */
public class LikeStatusVo {

    @NotNull(message = "传入信息有空值")
    private Integer userID;

    @NotNull(message = "传入信息有空值")
    private Long postID;

    @NotNull(message = "传入信息有空值")
    private Boolean status;

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Long getPostID() {
        return postID;
    }

    public void setPostID(Long postID) {
        this.postID = postID;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LikeStatusVo{" +
                "userID=" + userID +
                ", postID=" + postID +
                ", status=" + status +
                '}';
    }
}
