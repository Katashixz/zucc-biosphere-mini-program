package com.biosphere.library.vo;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author Administrator
 * @Date 2022/9/9 16:14
 * @Version 1.0
 */
public class CommunityPostVo implements Serializable {
    //SELECT p.id as postID, p.userID as userID, u.avatarUrl as avatarUrl, u.userName as userName,p.theme as theme,
    // p.content as content, p.postDate as postDate, p.isTop as isTop, p.isEssential as isEssential, p.imageUrl,
    // COUNT(DISTINCT c.id) as commentNum, COUNT(DISTINCT l.id) as likeNum
    private Long postID;

    private Integer userID;

    private String avatarUrl;

    private String userName;

    private String theme;

    private String content;

    private String imageUrl;

    private Date postDate;

    private Integer isTop;

    private Integer isEssential;

    private Integer commentNum;

    private Integer likeNum;

    private String dateformat;
    @TableField(exist = false)
    private final boolean state = false;

    @TableField(exist = false)
    private final boolean click = false;

    @TableField(exist = false)
    private String[] imageUrlList;

    @TableField(exist = false)
    private boolean postIsLiked = false;

    public boolean isPostIsLiked() {
        return postIsLiked;
    }

    public void setPostIsLiked(boolean postIsLiked) {
        this.postIsLiked = postIsLiked;
    }

    public boolean isState() {
        return state;
    }

    public boolean isClick() {
        return click;
    }

    public String getDateformat() {
        return dateformat;
    }

    public void setDateformat(String dateformat) {
        this.dateformat = dateformat;
    }

    public String[] getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(String[] imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

    public Long getPostID() {
        return postID;
    }

    public void setPostID(Long postID) {
        this.postID = postID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }
}
