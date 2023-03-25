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
 * @since 2023-03-25
 */
public class ViewChatMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableField("chatID")
    private Long chatID;

    /**
     * 主键id
     */
    @TableField("userID")
    private Integer userID;

    /**
     * 用户名
     */
    @TableField("userName")
    private String userName;

    /**
     * 用户头像url
     */
    @TableField("avatarUrl")
    private String avatarUrl;

    /**
     * 消息来源用户ID
     */
    @TableField("sourceID")
    private Integer sourceID;

    /**
     * 消息目标用户ID
     */
    @TableField("targetID")
    private Integer targetID;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息类型，1为文字，2为图片
     */
    @TableField("msgType")
    private Integer msgType;

    /**
     * 生成时间
     */
    @TableField("createdAt")
    private Date createdAt;

    public Long getChatID() {
        return chatID;
    }

    public void setChatID(Long chatId) {
        this.chatID = chatId;
    }
    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userId) {
        this.userID = userId;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    public Integer getSourceID() {
        return sourceID;
    }

    public void setSourceID(Integer sourceID) {
        this.sourceID = sourceID;
    }
    public Integer getTargetID() {
        return targetID;
    }

    public void setTargetID(Integer targetID) {
        this.targetID = targetID;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ViewChatMsg{" +
            "chatID=" + chatID +
            ", userID=" + userID +
            ", userName=" + userName +
            ", avatarUrl=" + avatarUrl +
            ", sourceID=" + sourceID +
            ", targetID=" + targetID +
            ", content=" + content +
            ", msgType=" + msgType +
            ", createdAt=" + createdAt +
        "}";
    }
}
