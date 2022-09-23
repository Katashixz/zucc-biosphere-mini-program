package com.biosphere.library.vo;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Author Administrator
 * @Date 2022/9/19 11:01
 * @Version 1.0
 */
public class HotPostVo {

    private Long postID;

    private Integer likeNum;

    private Integer commentNum;

    @TableField(exist = false)
    private Double heat;

    public Double getHeat() {
        return heat;
    }

    public void setHeat(Double heat) {
        this.heat = heat;
    }

    public Long getPostID() {
        return postID;
    }

    public void setPostID(Long postID) {
        this.postID = postID;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }
}
