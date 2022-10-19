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
 * @since 2022-10-13
 */
public class StarRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 收藏记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 收藏者ID
     */
    @TableField("userID")
    private Integer userID;

    /**
     * 收藏帖子
     */
    @TableField("postID")
    private Long postID;

    /**
     * 收藏日期
     */
    @TableField("starDate")
    private Date starDate;

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
    public Date getStarDate() {
        return starDate;
    }

    public void setStarDate(Date starDate) {
        this.starDate = starDate;
    }

    @Override
    public String toString() {
        return "StarRecord{" +
            "id=" + id +
            ", userID=" + userID +
            ", postID=" + postID +
            ", starDate=" + starDate +
        "}";
    }
}
