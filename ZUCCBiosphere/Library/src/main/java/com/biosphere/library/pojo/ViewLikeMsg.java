package com.biosphere.library.pojo;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author hyh
 * @since 2023-03-28
 */
public class ViewLikeMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableField("notifyID")
    private Integer notifyID;

    /**
     * 主键id
     */
    @TableField("userID")
    private Integer userID;

    /**
     * 帖子ID
     */
    @TableField("postID")
    private Long postID;

    /**
     * 用户名
     */
    @TableField("sourceName")
    private String sourceName;

    /**
     * 用户头像url
     */
    @TableField("sourceAvatar")
    private String sourceAvatar;

    /**
     * 用户ID
     */
    @TableField("targetID")
    private Integer targetID;

    /**
     * 帖子内容
     */
    @TableField("postContent")
    private String postContent;

    /**
     * 图片地址
     */
    @TableField("postImage")
    private String postImage;

    /**
     * 创建时间
     */
    @TableField("createdAt")
    private Date createdAt;

    public Integer getNotifyID() {
        return notifyID;
    }

    public void setNotifyID(Integer notifyID) {
        this.notifyID = notifyID;
    }
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
    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
    public String getSourceAvatar() {
        return sourceAvatar;
    }

    public void setSourceAvatar(String sourceAvatar) {
        this.sourceAvatar = sourceAvatar;
    }
    public Integer getTargetID() {
        return targetID;
    }

    public void setTargetID(Integer targetID) {
        this.targetID = targetID;
    }
    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }
    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ViewLikeMsg{" +
            "notifyID=" + notifyID +
            ", userID=" + userID +
            ", postID=" + postID +
            ", sourceName=" + sourceName +
            ", sourceAvatar=" + sourceAvatar +
            ", targetID=" + targetID +
            ", postContent=" + postContent +
            ", postImage=" + postImage +
            ", createdAt=" + createdAt +
        "}";
    }
}
