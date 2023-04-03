package com.biosphere.library.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author hyh
 * @since 2023-04-03
 */
public class AdoptDiary implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 发布者id
     */
    @TableField("userID")
    private Integer userID;

    /**
     * 宠物图片
     */
    @TableField("targetImage")
    private String targetImage;

    /**
     * 内容
     */
    private String content;

    /**
     * 发布日期
     */
    @TableField("createdAt")
    private Date createdAt;

    /**
     * 图片或视频
     */
    @TableField("imageUrl")
    private String imageUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }
    public String getTargetImage() {
        return targetImage;
    }

    public void setTargetImage(String targetImage) {
        this.targetImage = targetImage;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "AdoptDiary{" +
            "id=" + id +
            ", userID=" + userID +
            ", targetImage=" + targetImage +
            ", content=" + content +
            ", createdAt=" + createdAt +
            ", imageUrl=" + imageUrl +
        "}";
    }
}
