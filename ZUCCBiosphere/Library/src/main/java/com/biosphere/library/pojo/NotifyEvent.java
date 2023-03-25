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
 * @since 2023-03-22
 */
public class NotifyEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 产生行为的用户id
     */
    @TableField("userID")
    private Integer userID;

    /**
     * 行为编号，1为点赞，2为评论，3为私聊，4为充电，5为系统
     */
    private Integer action;

    /**
     * 对象ID
     */
    @TableField("objectID")
    private Integer objectID;

    /**
     * 对象所属类型，1为人，2为帖子
     */
    @TableField("objectType")
    private Integer objectType;

    /**
     * 创建时间
     */
    @TableField("createdAt")
    private Date createdAt;

    /**
     * 行为撤销标识符
     */
    @TableField("removeState")
    private Integer removeState;

    /**
     * 数据库中无此字段，用于作为接收Websocket消息的规范
     */
    @TableField(exist = false)
    private Integer code;

    public boolean equalState(NotifyEvent notifyEvent){
        return notifyEvent.getAction() == this.getAction()
                && notifyEvent.getUserID() == this.getUserID()
                && notifyEvent.getObjectID() == this.getObjectID()
                && notifyEvent.getObjectType() == this.getObjectType();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

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
    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }
    public Integer getObjectType() {
        return objectType;
    }

    public void setObjectType(Integer objectType) {
        this.objectType = objectType;
    }
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public Integer getRemoveState() {
        return removeState;
    }

    public void setRemoveState(Integer removeState) {
        this.removeState = removeState;
    }

    @Override
    public String toString() {
        return "NotifyEvent{" +
            "id=" + id +
            ", userID=" + userID +
            ", action=" + action +
            ", objectID=" + objectID +
            ", objectType=" + objectType +
            ", createdAt=" + createdAt +
            ", removeState=" + removeState +
        "}";
    }
}
