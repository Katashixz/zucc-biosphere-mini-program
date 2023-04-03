package com.biosphere.library.vo;

import io.swagger.models.auth.In;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hyh
 * @description: TODO
 * @date 2023/4/3 16:15
 */
public class DiaryVo implements Serializable {

    private Integer diaryID;

    private Integer userID;

    private String userName;

    private String userAvatar;

    private String content;

    private String animalUrl;

    private String imageUrl;

    private String[] imageUrlList;

    private Date createdAt;

    private String createdAtFormat;

    public Integer getDiaryID() {
        return diaryID;
    }

    public void setDiaryID(Integer diaryID) {
        this.diaryID = diaryID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnimalUrl() {
        return animalUrl;
    }

    public void setAnimalUrl(String animalUrl) {
        this.animalUrl = animalUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String[] getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(String[] imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAtFormat() {
        return createdAtFormat;
    }

    public void setCreatedAtFormat(String createdAtFormat) {
        this.createdAtFormat = createdAtFormat;
    }
}
