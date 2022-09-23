package com.biosphere.library.vo;

import java.util.Map;

/**
 * @Author Administrator
 * @Date 2022/9/22 11:22
 * @Version 1.0
 */
public class PostUploadVo {
    // images: res,
    // userID: app.globalData.userInfo.id,
    // theme: that.data.theme,
    // content: that.data.content,
    private String theme;
    private String content;
    private Integer userID;
    private String[] images;

    public void setImages(String[] images) {
        this.images = images;
    }

    public String[] getImages() {
        return images;
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

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }


    @Override
    public String toString() {
        return "PostUploadVo{" +
                "theme='" + theme + '\'' +
                ", content='" + content + '\'' +
                ", userID=" + userID +
                ", images=" + images.length +
                '}';
    }
}
