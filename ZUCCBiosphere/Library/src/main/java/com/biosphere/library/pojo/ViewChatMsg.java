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
 * @since 2023-03-27
 */
public class ViewChatMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableField("chatId")
    private Long chatId;

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

    /**
     * 用户头像url
     */
    @TableField("targetAvatar")
    private String targetAvatar;

    /**
     * 用户名
     */
    @TableField("targetName")
    private String targetName;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
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
    public String getTargetAvatar() {
        return targetAvatar;
    }

    public void setTargetAvatar(String targetAvatar) {
        this.targetAvatar = targetAvatar;
    }
    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    @Override
    public String toString() {
        return "ViewChatMsg{" +
            "chatId=" + chatId +
            ", sourceName=" + sourceName +
            ", sourceAvatar=" + sourceAvatar +
            ", sourceID=" + sourceID +
            ", targetID=" + targetID +
            ", content=" + content +
            ", msgType=" + msgType +
            ", createdAt=" + createdAt +
            ", targetAvatar=" + targetAvatar +
            ", targetName=" + targetName +
        "}";
    }
}
