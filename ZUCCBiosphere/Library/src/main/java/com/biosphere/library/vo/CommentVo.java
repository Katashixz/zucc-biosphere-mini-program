package com.biosphere.library.vo;

/**
 * @Author Administrator
 * @Date 2022/9/15 14:09
 * @Version 1.0
 */
public class CommentVo {

    private Long postID;

    private Integer userID;

    private String userName;

    private String userAvatarUrl;

    private String commentAccessName;

    private String commentDate;

    private Integer commentAccessID;

    private String content;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public String getCommentAccessName() {
        return commentAccessName;
    }

    public void setCommentAccessName(String commentAccessName) {
        this.commentAccessName = commentAccessName;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getPostID() {
        return postID;
    }

    public void setPostID(Long postID) {
        this.postID = postID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getCommentAccessID() {
        return commentAccessID;
    }

    public void setCommentAccessID(Integer commentAccessID) {
        this.commentAccessID = commentAccessID;
    }
}
