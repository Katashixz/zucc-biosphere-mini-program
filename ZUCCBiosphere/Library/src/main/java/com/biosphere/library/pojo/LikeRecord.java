package com.biosphere.library.pojo;

import com.baomidou.mybatisplus.annotation.TableField;

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
public class LikeRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 点赞ID
     */
    private Integer id;

    /**
     * 点赞者ID
     */
    @TableField("userID")
    private Integer userID;

    /**
     * 点赞帖子ID
     */
    @TableField("postID")
    private Long postID;

    /**
     * 点赞时间
     */
    private Date date;

    /**
     * 0代表未查看，1代表已查看
     */
    private Integer ischecked;

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
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public Integer getIschecked() {
        return ischecked;
    }

    public void setIschecked(Integer ischecked) {
        this.ischecked = ischecked;
    }

    @Override
    public String toString() {
        return "LikeRecord{" +
            "id=" + id +
            ", userID=" + userID +
            ", postID=" + postID +
            ", date=" + date +
            ", ischecked=" + ischecked +
        "}";
    }
}
