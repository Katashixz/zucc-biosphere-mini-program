package com.biosphere.library.vo;

import java.util.Date;

/**
 * @Author Administrator
 * @Date 2022/10/19 14:31
 * @Version 1.0
 */
public class SimplePostVo {

    private Long postID;

    private Integer userID;

    private String avatarUrl;

    private String userName;

    private String theme;

    private String content;

    private String imageUrl;

    private String[] imageUrlList;

    private String dateFormat;

    private Date postDate;

    public SimplePostVo() {
    }

    public SimplePostVo(CommunityPostVo postVo) {
        this.postID = postVo.getPostID();
        this.userID = postVo.getUserID();
        this.avatarUrl = postVo.getAvatarUrl();
        this.userName = postVo.getUserName();
        this.theme = postVo.getTheme();
        this.content = postVo.getContent();
        this.imageUrl = postVo.getImageUrl();
        this.imageUrlList = postVo.getImageUrlList();
        this.dateFormat = postVo.getDateformat();
        this.postDate = postVo.getPostDate();
    }

    public String[] getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(String[] imageUrlList) {
        this.imageUrlList = imageUrlList;
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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }
}
