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
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 帖子ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("userID")
    private Integer userID;

    /**
     * 帖子主题
     */
    private String theme;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 发布时间
     */
    @TableField("postDate")
    private Date postDate;

    /**
     * 置顶标志
     */
    @TableField("isTop")
    private Integer isTop;

    /**
     * 精华标志
     */
    @TableField("isEssential")
    private Integer isEssential;

    /**
     * 图片地址
     */
    @TableField("imageUrl")
    private String imageUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }
    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }
    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }
    public Integer getIsEssential() {
        return isEssential;
    }

    public void setIsEssential(Integer isEssential) {
        this.isEssential = isEssential;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Post{" +
            "id=" + id +
            ", userID=" + userID +
            ", theme=" + theme +
            ", content=" + content +
            ", postDate=" + postDate +
            ", isTop=" + isTop +
            ", isEssential=" + isEssential +
            ", imageUrl=" + imageUrl +
        "}";
    }
}
