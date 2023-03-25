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
 * @since 2023-01-30
 */
public class ChatRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
     * 数据库中无此字段，用于作为接收Websocket消息的规范
     */
    @TableField(exist = false)
    private Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return "ChatRecord{" +
            "id=" + id +
            ", sourceID=" + sourceID +
            ", targetID=" + targetID +
            ", content=" + content +
            ", msgType=" + msgType +
            ", createdAt=" + createdAt +
            ", code=" + code +
        "}";
    }
}
