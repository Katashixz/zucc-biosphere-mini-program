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
 * @since 2023-04-02
 */
public class ViewTodayCondition implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录主键
     */
    @TableField("conditionID")
    private Integer conditionID;

    /**
     * 状况来源的用户ID
     */
    @TableField("userID")
    private Integer userID;

    /**
     * 用户名
     */
    @TableField("userName")
    private String userName;

    /**
     * 动物ID
     */
    @TableField("targetID")
    private Integer targetID;

    /**
     * 若行为类型为喂食(2)，则记录食品id
     */
    @TableField("shopID")
    private Integer shopID;

    /**
     * 商品名称
     */
    @TableField("shopName")
    private String shopName;

    /**
     * 行为，1代表遇见，2代表喂食
     */
    private Integer action;

    /**
     * 创建时间
     */
    @TableField("createdAt")
    private String createdAt;

    /**
     * 状况图片
     */
    @TableField("imageUrl")
    private String imageUrl;

    public Integer getConditionID() {
        return conditionID;
    }

    public void setConditionID(Integer conditionID) {
        this.conditionID = conditionID;
    }
    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Integer getTargetID() {
        return targetID;
    }

    public void setTargetID(Integer targetID) {
        this.targetID = targetID;
    }
    public Integer getShopID() {
        return shopID;
    }

    public void setShopID(Integer shopID) {
        this.shopID = shopID;
    }
    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "ViewTodayCondition{" +
            "conditionID=" + conditionID +
            ", userID=" + userID +
            ", userName=" + userName +
            ", targetID=" + targetID +
            ", shopID=" + shopID +
            ", shopName=" + shopName +
            ", action=" + action +
            ", createdAt=" + createdAt +
            ", imageUrl=" + imageUrl +
        "}";
    }
}
