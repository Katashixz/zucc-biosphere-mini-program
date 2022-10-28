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
 * @since 2022-09-08
 */
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    @TableField("userID")
    private Integer userID;

    /**
     * 帖子ID
     */
    @TableField("postID")
    private Long postID;

    /**
     * 评论时间
     */
    @TableField("commentDate")
    private Date commentDate;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论对象
     */
    @TableField("commentToUser")
    private Integer commentToUser;

    /**
     * 0代表未查看，1代表已查看
     */
    private Integer isChecked;

    /**
     * 0代表未删除，1代表已删除
     */
    private Integer isDeleted;

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
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
    public Long getPostID() {
        return postID;
    }

    public void setPostID(Long postID) {
        this.postID = postID;
    }
    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public Integer getCommentToUser() {
        return commentToUser;
    }

    public void setCommentToUser(Integer commentToUser) {
        this.commentToUser = commentToUser;
    }
    public Integer getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Integer ischecked) {
        this.isChecked = ischecked;
    }

    @Override
    public String toString() {
        return "Comment{" +
            "id=" + id +
            ", userID=" + userID +
            ", postID=" + postID +
            ", commentDate=" + commentDate +
            ", content=" + content +
            ", commentToUser=" + commentToUser +
            ", isChecked=" + isChecked +
        "}";
    }
}
