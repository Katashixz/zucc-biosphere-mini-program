package com.biosphere.library.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author Administrator
 * @Date 2022/9/28 9:06
 * @Version 1.0
 */
public class UploadCommentVo {

    @NotNull(message = "传入信息有空值")
    private Integer userID;

    @NotNull(message = "传入信息有空值")
    private Long postID;

    @NotBlank(message = "评论内容不能为空")
    private String content;

    @NotNull(message = "传入信息有空值")
    private Integer toUserID;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getToUserID() {
        return toUserID;
    }

    public void setToUserID(Integer toUserID) {
        this.toUserID = toUserID;
    }

    @Override
    public String toString() {
        return "UploadCommentVo{" +
                "userID=" + userID +
                ", postID=" + postID +
                ", content='" + content + '\'' +
                ", toUserID=" + toUserID +
                '}';
    }
}
