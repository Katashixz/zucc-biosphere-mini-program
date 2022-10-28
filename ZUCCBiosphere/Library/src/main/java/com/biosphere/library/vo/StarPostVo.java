package com.biosphere.library.vo;

/**
 * @Author Administrator
 * @Date 2022/10/20 16:18
 * @Version 1.0
 */
public class StarPostVo {
    public Long postID;

    public String content;

    public String theme;

    public String imageUrl;

    public String[] imageUrlList;

    public String avatarUrl;

    public String dateFormat;

    public String starDateformat;

    public String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getStarDateformat() {
        return starDateformat;
    }

    public void setStarDateformat(String starDateformat) {
        this.starDateformat = starDateformat;
    }
}
