package com.biosphere.library.vo;

import io.swagger.models.auth.In;

import javax.validation.constraints.NotNull;

/**
 * @author hyh
 * @description: TODO
 * @date 2023/4/3 21:18
 */
public class DiaryUploadVo {

    @NotNull
    private Integer userID;

    @NotNull
    private String content;

    @NotNull
    private String createdAt;

    @NotNull
    private String targetImage;

    private String[] images;

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getTargetImage() {
        return targetImage;
    }

    public void setTargetImage(String targetImage) {
        this.targetImage = targetImage;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }
}
