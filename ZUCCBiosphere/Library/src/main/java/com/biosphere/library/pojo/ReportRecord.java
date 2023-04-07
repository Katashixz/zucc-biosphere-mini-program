package com.biosphere.library.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author hyh
 * @since 2023-04-04
 */
public class ReportRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 举报记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 举报者ID
     */
    @NotNull
    @TableField("userID")
    private Integer userID;

    /**
     * 被举报者ID
     */
    @NotNull
    @TableField("targetID")
    private Integer targetID;

    /**
     * 被举报帖子ID
     */
    @NotNull
    @TableField("postID")
    private Long postID;

    /**
     * 举报内容
     */
    @NotNull
    private String content;

    /**
     * 是否处理,0代表未处理,1代表已处理
     */
    @TableField("isHandled")
    private Integer isHandled;

    /**
     * 创建时间
     */
    @TableField("createdAt")
    private Date createdAt;

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
    public Integer getTargetID() {
        return targetID;
    }

    public void setTargetID(Integer targetID) {
        this.targetID = targetID;
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
    public Integer getIsHandled() {
        return isHandled;
    }

    public void setIsHandled(Integer isHandled) {
        this.isHandled = isHandled;
    }
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ReportRecord{" +
            "id=" + id +
            ", userID=" + userID +
            ", targetID=" + targetID +
            ", postID=" + postID +
            ", content=" + content +
            ", isHandled=" + isHandled +
            ", createdAt=" + createdAt +
        "}";
    }
}
